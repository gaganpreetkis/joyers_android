package com.joyersapp.feature.dashboard.presentation.user_profile

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onBack: () -> Unit = { /*(LocalContext.current as? ComponentActivity)?.finish()*/ },
    onMenu: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        // fixed header + scrollable content - we don't use Scaffold.bottomBar so content can scroll under floating nav
        Column(modifier = Modifier.fillMaxSize()) {
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
                    state = state,
                    onEditProfile = viewModel::onEditProfile,
                    onMessage = viewModel::onMessage,
                    onNotify = viewModel::onNotify,
                    onBookmark = viewModel::onBookmark
                )
            }
        }
    }


}