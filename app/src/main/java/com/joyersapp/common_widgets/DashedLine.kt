package com.joyersapp.common_widgets

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray30
import com.joyersapp.theme.LightBlack40

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 1f,
    dashLength: Float = 3f,
    gapLength: Float = 3f
) {
    Canvas(modifier = modifier) {
        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength, gapLength),
            phase = 0f
        )
        drawLine(
            color = LightBlack40,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidth,
            pathEffect = pathEffect
        )
    }
}