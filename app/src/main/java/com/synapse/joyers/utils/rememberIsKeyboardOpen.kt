package com.synapse.joyers.utils

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity

@Composable
fun rememberIsKeyboardOpen(): Boolean {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    var isKeyboardOpen by remember { mutableStateOf(imeBottom > 0) }

    LaunchedEffect(imeBottom) {
        isKeyboardOpen = imeBottom > 0
    }
    return isKeyboardOpen
}