package com.joyersapp.feature.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.joyersapp.feature.dashboard.presentation.user_profile.CenteredHeader

@Composable
fun AppScaffoldRoot(
    viewModel: AppScaffoldViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FloatingBottomNavHost(
        topBar = {
            CenteredHeader(
                username = uiState.headerTitle!!,

            )
//            TitleHeader(
//                title = uiState.headerTitle,
//                subtitle = uiState.headerSubtitle,
//                onBackClick = viewModel::onBackClicked,
//                onMenuClick = viewModel::onMenuClicked
//            )
        },
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