package com.joyersapp.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joyersapp.AuthNavGraph
import com.joyersapp.feature.dashboard.DashboardScreen
import com.synapse.joyers.ui.screens.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        composable("splash") { SplashScreen() }
        composable("auth") { AuthNavGraph() }
        composable("dashboard") { DashboardScreen() }
    }
}