package com.joyersapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.components.layouts.CustomProgressIndicator
import com.joyersapp.feature.dashboard.DashboardScreen
import com.synapse.joyers.ui.screens.SplashScreen

@Composable
fun AppNavHost(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val authState by mainViewModel.authState.collectAsState()
    val navController = rememberNavController()

    when (authState) {
        AuthState.Unknown -> {
            SplashScreen()
        }

        is AuthState.Unauthenticated -> {
            AppNavGraph(navController = navController)
        }

        is AuthState.Authenticated -> {
            DashboardScreen(navController = navController)
        }
    }
}