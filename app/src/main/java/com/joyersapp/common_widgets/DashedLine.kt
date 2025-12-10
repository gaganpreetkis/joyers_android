package com.joyersapp.common_widgets

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray30
import com.joyersapp.theme.LightBlack40

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 1f,     // dp
    dashLength: Float = 3f,      // dp
    gapLength: Float = 3f        // dp
) {
    val density = LocalDensity.current

    Canvas(modifier = modifier) {

        // Convert dp → px
        val strokePx = with(density) { strokeWidth.dp.toPx() }
        val dashPx = with(density) { dashLength.dp.toPx() }
        val gapPx = with(density) { gapLength.dp.toPx() }

        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashPx, gapPx),
            phase = 0f
        )

        drawLine(
            color = LightBlack40,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokePx,
            pathEffect = pathEffect
        )
    }
}
