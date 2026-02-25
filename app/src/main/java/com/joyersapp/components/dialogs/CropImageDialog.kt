package com.joyersapp.components.dialogs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joyersapp.R
import com.joyersapp.feature.profile.presentation.dialogs.CropImageViewModel
import com.joyersapp.feature.profile.presentation.dialogs.CropNavigationEvent
import com.joyersapp.theme.Black
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

@Composable
fun CropImageDialog(
    showDialog: Boolean,
    isImageCropped: Boolean,
    initialScale: Float? = 1f,
    initialOffset: Offset? = Offset(Offset.Zero.packedValue),
    imageUri: Uri?,
    onDismiss: () -> Unit,
    onCropped: (Uri, Float, Offset) -> Unit
) {
    if (!showDialog) return

    val viewmodel: CropImageViewModel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsState()

    if (imageUri == null) return

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val cropSize = configuration.screenWidthDp.dp - 40.dp
    val cropDiameterPx = with(density) { cropSize.toPx() }
    val cropRadiusPx = cropDiameterPx / 2f

    LaunchedEffect(Unit) {
        viewmodel.setCircleRadius(cropRadiusPx)
//        if (uiState.imageUri != imageUri) {
            viewmodel.loadImage(imageUri, initialScale?:1f, initialOffset?: Offset(Offset.Zero.packedValue))
//        }
    }

    LaunchedEffect(Unit) {
        viewmodel.navigationEvent.collect { event ->
            when (event) {
                CropNavigationEvent.Back -> TODO()
                is CropNavigationEvent.OnCropped -> {
                        onCropped(event.uri, event.scale, event.offset)
                }
            }
        }
    }

//    LaunchedEffect(uiState.croppedUri) {
//        uiState.croppedUri?.let {
//            onCropped(it, uiState.croppedPath ?: "")
//        }
//    }


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
                                .pointerInput(uiState.imageBitmap) {
                                    detectTransformGestures { _, pan, zoom, _ ->
                                        viewmodel.updateTransform(pan, zoom, cropDiameterPx)
                                    }
                                }
                        )

                        // Circular crop overlay with border and grid
                        Canvas(
                            modifier = Modifier
                                .size(cropSize)
                                .align(Alignment.Center)
                                .graphicsLayer {
                                    compositingStrategy = CompositingStrategy.Offscreen
                                })
                        {
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Draw Scaled and Panned Image
                            withTransform({
                                translate(center.x + uiState.offset.x, center.y + uiState.offset.y)
                                scale(
                                    uiState.scale,
                                    uiState.scale,
                                    Offset(0f, 0f)
                                ) // Scale from image center
                            }) {
                                // Center the bitmap drawing
                                uiState.imageBitmap?.let { bitmap ->
                                    drawImage(
                                        bitmap,
                                        topLeft = Offset(-bitmap.width / 2f, -bitmap.height / 2f)
                                    )
                                }
                            }

                            // 2. Create a path that is a Rectangle MINUS a Circle
                            val pathWithHole = Path().apply {
                                // Add the outer rectangle
                                addRect(androidx.compose.ui.geometry.Rect(Offset.Zero, size))

                                // Add the inner circle
                                addOval(androidx.compose.ui.geometry.Rect(center, cropRadiusPx))

                                // This is the "Magic" part: it tells Compose to fill the area
                                // between the rect and the circle, leaving the circle empty.
                                fillType = androidx.compose.ui.graphics.PathFillType.EvenOdd
                            }

                            // 3. Draw that specific shape with 30% opacity
                            drawPath(
                                path = pathWithHole,
                                color = LightBlack,
                                alpha = 0.7f
                            )

                            // Draw white circle border
                            drawCircle(
                                color = White,
                                radius = cropRadiusPx - 2,
                                center = center,
                                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
                            )

                            // Draw grid lines (clipped to circle)
                            val stepX = size.width / 3f
                            val stepY = size.height / 3f
                            for (i in 1..2) {
                                // Vertical line at x = stepX * i
                                val x = stepX * i
                                val dx = x - center.x
                                if (dx * dx <= cropRadiusPx * cropRadiusPx) {
                                    val dy = kotlin.math.sqrt(cropRadiusPx * cropRadiusPx - dx * dx)
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
                                if (dy * dy <= cropRadiusPx * cropRadiusPx) {
                                    val dx = kotlin.math.sqrt(cropRadiusPx * cropRadiusPx - dy * dy)
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
                        .clickable { viewmodel.cropAndSave() },
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