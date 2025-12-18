package com.joyersapp.feature.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joyersapp.theme.White

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = hiltViewModel(),
    onBack: () -> Unit = { /*(LocalContext.current as? ComponentActivity)?.finish()*/ },
    onMenu: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize().background(White)) {
        // fixed header + scrollable content - we don't use Scaffold.bottomBar so content can scroll under floating nav
        Column(modifier = Modifier.fillMaxSize().background(White)) {
            ProfileTopHeader(
                username = state.username,
                onBack = onBack,
                onMenu = onMenu
            )

            // Content (scrollable)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                UserProfileContent(
                    viewModel = viewModel,
                    onEditProfile = viewModel::onEditProfile,
                    onMessage = viewModel::onMessage,
                    onNotify = viewModel::onNotify,
                    onBookmark = viewModel::onBookmark
                )
            }
        }
    }


}