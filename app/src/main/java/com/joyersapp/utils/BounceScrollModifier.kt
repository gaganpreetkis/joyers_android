package com.joyersapp.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs

/**
 * Modifier extension that adds iOS-like bounce overscroll behavior
 * Requires the ScrollState to detect boundaries
 */
@Composable
fun Modifier.iosBounceScroll(
    scrollState: ScrollState,
    orientation: Orientation = Orientation.Vertical
): Modifier {
    val density = LocalDensity.current
    var rawOverscroll by remember { mutableFloatStateOf(0f) }
    
    // Animate the overscroll for smooth bounce
    val overscrollOffset by animateFloatAsState(
        targetValue = rawOverscroll,
        animationSpec = tween(durationMillis = 100),
        label = "overscroll"
    )
    
    val nestedScrollConnection = remember(scrollState) {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val scrollDelta = if (orientation == Orientation.Vertical) {
                    available.y
                } else {
                    available.x
                }
                
                // Check if we're at a boundary and trying to overscroll
                val isAtTop = scrollState.value <= 0 && scrollDelta > 0
                val isAtBottom = scrollState.value >= scrollState.maxValue && scrollDelta < 0
                
                if (isAtTop || isAtBottom) {
                    // We're at a boundary trying to overscroll - apply bounce
                    val resistance = calculateElasticResistance(abs(scrollDelta))
                    val bounceAmount = scrollDelta * resistance
                    
                    // Update overscroll offset for visual bounce
                    rawOverscroll = bounceAmount
                    
                    // Limit overscroll to prevent excessive movement
                    val maxOverscroll = with(density) { 80.dp.toPx() }
                    rawOverscroll = rawOverscroll.coerceIn(-maxOverscroll, maxOverscroll)
                    
                    // Consume the bounce amount to prevent normal scroll
                    return if (orientation == Orientation.Vertical) {
                        Offset(0f, bounceAmount)
                    } else {
                        Offset(bounceAmount, 0f)
                    }
                } else {
                    // Not at boundary - reset overscroll
                    if (abs(rawOverscroll) > 0.5f) {
                        rawOverscroll *= 0.8f // Smooth spring back
                    } else {
                        rawOverscroll = 0f
                    }
                    return Offset.Zero
                }
            }
            
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // Spring back if no more scroll
                if (available == Offset.Zero) {
                    if (abs(rawOverscroll) > 0.5f) {
                        rawOverscroll *= 0.8f
                    } else {
                        rawOverscroll = 0f
                    }
                }
                return Offset.Zero
            }
            
            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity {
                // Reset overscroll after fling
                rawOverscroll = 0f
                return Velocity.Zero
            }
        }
    }
    
    return this
        .nestedScroll(nestedScrollConnection)
        .graphicsLayer {
            // Apply visual bounce transformation
            if (orientation == Orientation.Vertical) {
                translationY = overscrollOffset
            } else {
                translationX = overscrollOffset
            }
        }
}

/**
 * Calculate elastic resistance for iOS-like bounce effect
 * Creates smooth, natural bounce similar to iOS SwiftUI
 */
private fun calculateElasticResistance(distance: Float): Float {
    if (distance <= 0f) return 1f
    
    // Elasticity factor: lower = more elastic/bouncy
    val elasticityFactor = 25f
    
    // Calculate resistance: allows movement but with increasing resistance
    // Formula provides smooth curve similar to iOS
    val resistance = elasticityFactor / (abs(distance) + elasticityFactor)
    
    // Clamp to ensure visible bounce (allows 70% to 95% of movement)
    return resistance.coerceIn(0.7f, 0.95f)
}
