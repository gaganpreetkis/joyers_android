package com.joyersapp.auth.presentation

import android.app.Activity
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.joyersapp.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SplashVideoScreen(
    viewModel: SplashVideoViewModel = hiltViewModel(),
    onNavigateToDashboard: (Context) -> Unit = { context ->
        // Default: Navigate to DashboardActivity
        /*val intent = android.content.Intent(context, com.synapse.joyers.ui.dashboard.DashboardActivity::class.java)
        context.startActivity(intent)
        (context as? android.app.Activity)?.finish()*/
    }
) {
    val context = LocalContext.current
    
    // Handle back button press - close the app
    BackHandler {
        (context as? Activity)?.finish()
    }
    
    // Create ImageLoader with GIF support (remember to avoid recreation)
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
    }
    
    var gifLoaded by remember { mutableStateOf(false) }
    
    // Navigate after GIF loads and 3.5 seconds delay (original implementation)
    LaunchedEffect(gifLoaded) {
        if (gifLoaded) {
            delay(3500) // 3.5 seconds delay as per original implementation

            viewModel.navigateToDashboard()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Load GIF using Coil with state callback
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.splash_gif)
                .decoderFactory(GifDecoder.Factory())
                .build(),
            imageLoader = imageLoader,
            contentDescription = "Splash GIF",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            onSuccess = { 
                // Mark GIF as loaded, then start the delay
                gifLoaded = true
            },
            onError = { 
                // On error, still proceed after delay
                gifLoaded = true
            }
        )
    }
}
