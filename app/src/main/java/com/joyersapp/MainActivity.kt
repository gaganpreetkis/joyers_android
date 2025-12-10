package com.joyersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.joyersapp.utils.rememberKeyboardHider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {

            // ⭐ NEW NON-DEPRECATED OVERSCROLL API
            CompositionLocalProvider(
                LocalOverscrollFactory provides rememberPlatformOverscrollFactory(
                    glowColor = Color.Unspecified   // removes Android glow, keeps bounce
                )
            ) {

                MaterialTheme {

                    val hideKeyboard = rememberKeyboardHider()
                    val navController = rememberNavController()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    hideKeyboard()
                                }
                            }
                    ) {
                        AppNavGraph(navController)
                    }
                }
            }
        }
    }
}
