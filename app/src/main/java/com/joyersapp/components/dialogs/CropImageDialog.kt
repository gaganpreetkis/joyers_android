package com.joyersapp.components.dialogs

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.joyersapp.R
import com.joyersapp.components.chips.CircleImageCropper
import com.joyersapp.theme.Black
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

@Composable
fun CropImageDialog(
    showDialog: Boolean,
    imageUri: Uri?,
    onDismiss: () -> Unit,
    onCropped: (Uri, String) -> Unit
) {
    if (!showDialog || imageUri == null) return

    val context = LocalContext.current
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

// Circle size: Screen Width - 40px (convert
    val circleDiameterPx = with(density){ (configuration.screenWidthDp - 40).dp.toPx() }
        val circleRadiusPx = circleDiameterPx / 2

        // Transform state for pan and zoom
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val screenWidth = with(density) { configuration.screenWidthDp.dp }
    val cropSize = screenWidth - 40.dp
    val cropSizePx = with(density) { cropSize.toPx() }
    val cropRadius = cropSizePx / 2f

    LaunchedEffect(imageUri) {
        imageBitmap = try {
            // Read EXIF orientation first
            val orientation = context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                try {
                    val exif = ExifInterface(inputStream)
                    exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                } catch (e: Exception) {
                    ExifInterface.ORIENTATION_NORMAL
                }
            } ?: ExifInterface.ORIENTATION_NORMAL

            // Decode bitmap
            val bitmap = context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            } ?: return@LaunchedEffect

            // Apply rotation based on EXIF orientation
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            }

            val rotatedBitmap = if (orientation != ExifInterface.ORIENTATION_NORMAL) {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }

            rotatedBitmap.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Initial scale to ensure image covers the circle
    LaunchedEffect(imageBitmap) {
        imageBitmap?.let {
            val minScaleX = circleDiameterPx / it.width
            val minScaleY = circleDiameterPx / it.height
            scale = maxOf(minScaleX, minScaleY)
        }
    }

    fun cropAndSave() {

        val bitmap = imageBitmap ?: return
        val croppedBitmap = getCircularCroppedBitmap(
            bitmap,
            Offset(x = offsetX, y = offsetY),
            scale,
            circleRadiusPx
        )

        val destFile = File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
        FileOutputStream(destFile).use {
            croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        val newUri = Uri.fromFile(destFile)
        onCropped(newUri, destFile.path)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Black) // LightBlack background
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .offset(y = (-31).dp) // 31px up from center
                            .size(cropSize)
                    ) {
                        // Full image area with pan and zoom
                        Box(
                            modifier = Modifier
                                .size(cropSize)
                                .pointerInput(imageBitmap) {
                                    detectTransformGestures { _, pan, zoom, _ ->

                                        imageBitmap?.let { bitmap ->
                                            // 1. Update Scale (with a minimum floor)
                                            val minScale = maxOf(circleDiameterPx / bitmap.width, circleDiameterPx / bitmap.height)
                                            scale = (scale * zoom).coerceAtLeast(minScale)

                                            // 2. Calculate New Offset
                                            val newOffsetX = offsetX + pan.x
                                            val newOffsetY = offsetY + pan.y

                                            // 3. Clamp Offset to keep image covering the circle
                                            val maxOffsetHorizontal = (bitmap.width * scale - circleDiameterPx) / 2
                                            val maxOffsetVertical = (bitmap.height * scale - circleDiameterPx) / 2

                                            offsetX = newOffsetX.coerceIn(-maxOffsetHorizontal, maxOffsetHorizontal)
                                            offsetY = newOffsetY.coerceIn(-maxOffsetVertical, maxOffsetVertical)
                                        }
                                    }
                                }
                        )

                        // Circular crop overlay with border and grid
                        Canvas(
                            modifier = Modifier
                                .size(cropSize)
                                .align(Alignment.Center).graphicsLayer {
                                    compositingStrategy = CompositingStrategy.Offscreen
                                })
                        {
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Draw Scaled and Panned Image
                            withTransform({
                                translate(center.x + offsetX, center.y + offsetY)
                                scale(scale, scale, Offset(0f, 0f)) // Scale from image center
                            }) {
                                // Center the bitmap drawing
                                imageBitmap?.let { bitmap ->
                                    drawImage(bitmap, topLeft = Offset(-bitmap.width / 2f, -bitmap.height / 2f))
                                }
                            }

                            // Draw white circle border
                            drawCircle(
                                color = White,
                                radius = cropRadius,
                                center = center,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                            )

                            // Draw grid lines (clipped to circle)
                            val stepX = size.width / 3f
                            val stepY = size.height / 3f
                            for (i in 1..2) {
                                // Vertical line at x = stepX * i
                                val x = stepX * i
                                val dx = x - center.x
                                if (dx * dx <= cropRadius * cropRadius) {
                                    val dy = kotlin.math.sqrt(cropRadius * cropRadius - dx * dx)
                                    val y1 = center.y - dy
                                    val y2 = center.y + dy
                                    drawLine(
                                        color = White.copy(alpha = 0.6f),
                                        start = Offset(x, y1),
                                        end = Offset(x, y2),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }

                                // Horizontal line at y = stepY * i
                                val y = stepY * i
                                val dy = y - center.y
                                if (dy * dy <= cropRadius * cropRadius) {
                                    val dx = kotlin.math.sqrt(cropRadius * cropRadius - dy * dy)
                                    val x1 = center.x - dx
                                    val x2 = center.x + dx
                                    drawLine(
                                        color = White.copy(alpha = 0.6f),
                                        start = Offset(x1, y),
                                        end = Offset(x2, y),
                                        strokeWidth = 1.dp.toPx()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {

                // Done button
                Box(
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                        .height(35.dp)
                        .width(87.dp)
                        .border(1.dp, White, RoundedCornerShape(50))
                        .clickable { cropAndSave() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Done",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = White,
                        modifier = Modifier.offset(y = -1.dp)
                    )
                }
            }
            
            // Close icon - placed above everything with pointer input to intercept touches
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 25.dp, end = 20.dp)
                    .zIndex(1f)
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "Close",
                )
            }
        }
    }
}

private fun getCircularCroppedBitmap(
    source: ImageBitmap,
    offset: Offset,
    scale: Float,
    circleRadiusPx: Float
): Bitmap {
    val androidBitmap = source.asAndroidBitmap()
    val diameter = (circleRadiusPx * 2).toInt()

    //  Create a blank square bitmap (the size of your circle)
    val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(output)
    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)

    //  Draw the image with the SAME transformations used in the UI
    val matrix = android.graphics.Matrix()
    matrix.postTranslate(-source.width / 2f, -source.height / 2f) // Center image
    matrix.postScale(scale, scale) // Apply zoom
    matrix.postTranslate(circleRadiusPx + offset.x, circleRadiusPx + offset.y) // Apply pan

    canvas.drawBitmap(androidBitmap, matrix, paint)

    return output
}