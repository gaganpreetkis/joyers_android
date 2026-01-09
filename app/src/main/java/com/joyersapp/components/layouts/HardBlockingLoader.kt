package com.joyersapp.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden
import com.joyersapp.theme.White

@Composable
fun HardBlockingLoader(show: Boolean) {
    if (show) {
        Dialog(
            onDismissRequest = {}, // Not allowed to dismiss
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false  // full screen
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        enabled = false,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Golden,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}