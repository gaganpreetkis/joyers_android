package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardScreen(
    viewModel: AppScaffoldViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    FloatingBottomNavHost(
        topBar = {},
        selectedTab = uiState.selectedTab,
        onBottomTabSelected = { tab ->
            viewModel.onBottomTabSelected(tab)
            navController.navigate(tab.route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
                              },
        content = { innerPadding ->
                // Dynamic screen content
//                ScreenContentContainer(
//                    innerPadding = innerPadding,
//                    currentScreen = uiState.currentScreen
//                )

            DashboardNavGraph(navController)
            }
    )
}