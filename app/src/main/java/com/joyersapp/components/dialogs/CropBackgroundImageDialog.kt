package com.joyersapp.components.dialogs

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.joyersapp.R
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min

@Composable
fun CropBackgroundImageDialog(
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

    // Transform state for pan and zoom
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

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
        // Reset transform when image loads
        scale = 1f
        offsetX = 0f
        offsetY = 0f
    }

    fun cropAndSave() {
        val bitmap = imageBitmap ?: return
        val androidBitmap = bitmap.asAndroidBitmap()

        // Get crop rectangle size in pixels
        val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
        val cropHeightPx = with(density) { 120.dp.toPx() }
        val cropWidthPx = screenWidthPx

        // Original image dimensions
        val imageWidth = androidBitmap.width.toFloat()
        val imageHeight = androidBitmap.height.toFloat()
        val imageAspect = imageWidth / imageHeight

        // The image is displayed in a Box with ContentScale.Fit
        // Calculate the actual displayed image size (after ContentScale.Fit, before graphicsLayer transform)
        val boxWidth = cropWidthPx
        val boxHeight = cropHeightPx
        val displayedImageWidth: Float
        val displayedImageHeight: Float

        if (imageAspect > boxWidth / boxHeight) {
            // Image is wider relative to box - fit to box width
            displayedImageWidth = boxWidth
            displayedImageHeight = boxWidth / imageAspect
        } else {
            // Image is taller relative to box - fit to box height
            displayedImageHeight = boxHeight
            displayedImageWidth = boxHeight * imageAspect
        }

        // The displayed image (after ContentScale.Fit) is centered in the Box
        // Displayed image bounds in Box coordinates (before graphicsLayer transform)
        val displayedImageLeft = (boxWidth - displayedImageWidth) / 2f
        val displayedImageTop = (boxHeight - displayedImageHeight) / 2f
        val displayedImageRight = displayedImageLeft + displayedImageWidth
        val displayedImageBottom = displayedImageTop + displayedImageHeight

        // Now account for graphicsLayer transform (scale and translation)
        // The graphicsLayer transform: scale is applied around pivot (center), then translation
        // For a point (x, y) in Image coordinates: 
        //   transformed = center + (x - center) * scale + translation
        // Inverse: x = center + (transformed - center - translation) / scale
        
        // The Image composable's center (pivot point for scaling)
        val pivotX = boxWidth / 2f
        val pivotY = boxHeight / 2f
        
        // Rectangle center in Box coordinates
        val rectCenterBoxX = boxWidth / 2f
        val rectCenterBoxY = boxHeight / 2f
        
        // Apply inverse transform: find the point in Image's coordinate system that maps to rectangle center
        val pointInImageCoordX = pivotX + (rectCenterBoxX - pivotX - offsetX) / scale
        val pointInImageCoordY = pivotY + (rectCenterBoxY - pivotY - offsetY) / scale

        // Convert to coordinates relative to displayed image's top-left corner
        val relativeX = pointInImageCoordX - displayedImageLeft
        val relativeY = pointInImageCoordY - displayedImageTop

        // Map from displayed image coordinates to original bitmap coordinates
        val scaleX = imageWidth / displayedImageWidth
        val scaleY = imageHeight / displayedImageHeight

        // Center point in original bitmap coordinates
        val cropCenterX = relativeX * scaleX
        val cropCenterY = relativeY * scaleY

        // Calculate crop size in original image coordinates
        // The rectangle size in Box coordinates is cropWidthPx x cropHeightPx
        // After inverse transform: (cropWidthPx / scale) x (cropHeightPx / scale) in displayed image coordinates
        // Map to original bitmap coordinates
        val cropWidthInDisplayed = cropWidthPx / scale
        val cropHeightInDisplayed = cropHeightPx / scale
        val cropWidthInImage = cropWidthInDisplayed * scaleX
        val cropHeightInImage = cropHeightInDisplayed * scaleY

        // Ensure crop size is valid and doesn't exceed image dimensions
        val validCropWidth = cropWidthInImage.coerceAtLeast(1f).coerceAtMost(imageWidth)
        val validCropHeight = cropHeightInImage.coerceAtLeast(1f).coerceAtMost(imageHeight)

        // Calculate crop bounds (rectangle centered at cropCenterX, cropCenterY)
        val halfCropWidth = validCropWidth / 2f
        val halfCropHeight = validCropHeight / 2f
        
        // Calculate crop position, ensuring it stays within image bounds
        val cropX = (cropCenterX - halfCropWidth).coerceIn(0f, imageWidth - validCropWidth)
        val cropY = (cropCenterY - halfCropHeight).coerceIn(0f, imageHeight - validCropHeight)

        // Create rectangular crop from bitmap
        val cropWidthInt = validCropWidth.toInt().coerceAtLeast(1).coerceAtMost(imageWidth.toInt())
        val cropHeightInt = validCropHeight.toInt().coerceAtLeast(1).coerceAtMost(imageHeight.toInt())
        val finalCropX = cropX.toInt().coerceIn(0, (imageWidth.toInt() - cropWidthInt).coerceAtLeast(0))
        val finalCropY = cropY.toInt().coerceIn(0, (imageHeight.toInt() - cropHeightInt).coerceAtLeast(0))
        
        val cropped = Bitmap.createBitmap(
            androidBitmap,
            finalCropX,
            finalCropY,
            cropWidthInt,
            cropHeightInt
        )

        val destFile = File(context.cacheDir, "cropped_background_${System.currentTimeMillis()}.jpg")
        FileOutputStream(destFile).use {
            cropped.compress(Bitmap.CompressFormat.JPEG, 90, it)
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
                .background(color = LightBlack)
                .systemBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .offset(y = (-48).dp), // 48px up from center
                    contentAlignment = Alignment.Center
                ) {
                    val screenWidth = with(density) { configuration.screenWidthDp.dp }
                    val cropWidth = screenWidth
                    val cropHeight = 120.dp
                    val cropWidthPx = with(density) { cropWidth.toPx() }
                    val cropHeightPx = with(density) { cropHeight.toPx() }

                    // Full image area with pan and zoom
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    val newScale = (scale * zoom).coerceIn(1f, 5f)
                                    val newOffsetX = offsetX + pan.x
                                    val newOffsetY = offsetY + pan.y

                                    // Constrain pan to keep crop area within image bounds
                                    imageBitmap?.let { bmp ->
                                        val imgWidth = bmp.width.toFloat()
                                        val imgHeight = bmp.height.toFloat()
                                        val scaledWidth = imgWidth * newScale
                                        val scaledHeight = imgHeight * newScale

                                        val maxOffsetX = max(0f, (scaledWidth - cropWidthPx) / 2f)
                                        val maxOffsetY = max(0f, (scaledHeight - cropHeightPx) / 2f)

                                        offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
                                        offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
                                    }
                                    scale = newScale
                                }
                            }
                    ) {
                        imageBitmap?.let { bmp ->
                            // Image clipped to rectangle
                            Box(
                                modifier = Modifier
                                    .width(cropWidth)
                                    .height(cropHeight)
                                    .align(Alignment.Center)
                                    .clip(RoundedCornerShape(0.dp))
                            ) {
                                Image(
                                    bitmap = bmp,
                                    contentDescription = "Crop background image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer(
                                            scaleX = scale,
                                            scaleY = scale,
                                            translationX = offsetX,
                                            translationY = offsetY
                                        ),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }

                    // Rectangular crop overlay with border and grid
                    Canvas(
                        modifier = Modifier
                            .width(cropWidth)
                            .height(cropHeight)
                            .align(Alignment.Center)
                    ) {
                        // Draw white rectangle border
                        /*drawRect(
                            color = White,
                            topLeft = Offset(0f, 0f),
                            size = androidx.compose.ui.geometry.Size(size.width, size.height),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                        )*/

                        // Draw grid lines (3x3 grid)
                        val stepX = size.width / 3f
                        val stepY = size.height / 3f
                        for (i in 1..2) {
                            // Vertical lines
                            drawLine(
                                color = White.copy(alpha = 0.6f),
                                start = Offset(stepX * i, 0f),
                                end = Offset(stepX * i, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                            
                            // Horizontal lines
                            drawLine(
                                color = White.copy(alpha = 0.6f),
                                start = Offset(0f, stepY * i),
                                end = Offset(size.width, stepY * i),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
                }

                // Done button
                Box(
                    modifier = Modifier
                        .padding(bottom = 60.dp)
                        .width(87.dp)
                        .border(1.dp, White, RoundedCornerShape(18.dp))
                        .clickable { cropAndSave() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Done",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = White,
                        modifier = Modifier.padding(
                            top = 7.dp,
                            bottom = 9.dp
                        )
                    )
                }
            }
            
            // Close icon
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 25.dp, start = 20.dp)
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

            // More options icon
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 29.dp, end = 20.dp)
                    .zIndex(1f)
                    .pointerInput(Unit) {
                        // Add click handler here if needed
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_dots_horizontal_white),
                    contentDescription = "More options",
                )
            }
        }
    }
}

