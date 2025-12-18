package com.joyersapp.feature.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppScaffoldRoot(
    viewModel: AppScaffoldViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FloatingBottomNavHost(
        topBar = {},
        selectedTab = uiState.selectedTab,
        onBottomTabSelected = { tab ->
            viewModel.onBottomTabSelected(tab)
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Dynamic screen content
            ScreenContentContainer(
                currentScreen = uiState.currentScreen
            )
        }
    }
}