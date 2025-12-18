package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    viewModel: AppScaffoldViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FloatingBottomNavHost(
        topBar = {},
        selectedTab = uiState.selectedTab,
        onBottomTabSelected = { tab -> viewModel.onBottomTabSelected(tab) },
        content = { innerPadding ->
                // Dynamic screen content
                ScreenContentContainer(
                    innerPadding = innerPadding,
                    currentScreen = uiState.currentScreen
                )
            }
    )
}