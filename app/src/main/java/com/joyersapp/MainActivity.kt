package com.joyersapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberPlatformOverscrollFactory
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.components.layouts.TokenExpiredDialog
import com.joyersapp.core.AppNavGraph
import com.joyersapp.core.SessionManager
import com.joyersapp.core.SessionState
import com.joyersapp.theme.White
import com.joyersapp.utils.rememberKeyboardHider
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

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
                    val mainViewModel = hiltViewModel<MainViewModel>()
                    val sessionState by sessionManager.sessionState.collectAsStateWithLifecycle()
                    val authState by sessionManager.authState.collectAsStateWithLifecycle()
                    var showTokenDialog by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        sessionManager.tokenExpired.collect {
                            showTokenDialog = true
                        }
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = White)
                            .windowInsetsPadding(WindowInsets.systemBars)
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    hideKeyboard()
                                }
                            }
                    ) {
                        AppNavGraph(navController)
                        if (showTokenDialog) {
                            TokenExpiredDialog {
                                showTokenDialog = false
                                CoroutineScope(Dispatchers.IO).launch {
                                    sessionManager.logout()
                                }
                            }
                        }
//                        AppNavHost(mainViewModel = hiltViewModel(), sessionManager)
//                        AuthNavGraph(navController)
                    }

                    LaunchedEffect(authState, sessionState) {

                        // 🚨 Important: wait until session is ready
                        if (sessionState is SessionState.Ready) {

                            when (authState) {

                                // Treat Unknown same as Authenticated OR remove it completely
                                is AuthState.Unknown,
                                is AuthState.Authenticated -> {
                                    navController.navigate("dashboard") {
                                        popUpTo("splash") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }

                                is AuthState.Unauthenticated -> {
                                    navController.navigate("auth") {
                                        popUpTo("splash") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
