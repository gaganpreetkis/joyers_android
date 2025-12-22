package com.synapse.joyers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joyersapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(/*onNavigateToLogin: () -> Unit*/) {

    // Navigate after delay
    /*LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToLogin()
    }*/

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = "Joyers Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
