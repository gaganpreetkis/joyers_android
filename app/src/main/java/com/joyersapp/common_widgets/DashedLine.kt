package com.joyersapp.common_widgets

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import com.joyersapp.theme.Gray30

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 3f,
    dashLength: Float = 10f,
    gapLength: Float = 7f
) {
    Canvas(modifier = modifier) {
        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength, gapLength),
            phase = 0f
        )
        drawLine(
            color = Gray30,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidth,
            pathEffect = pathEffect
        )
    }
}