package com.joyersapp.feature.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.presentation.UserProfileScreen

@Composable
fun ScreenContentContainer(currentScreen: AppScreen, innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (currentScreen) {
            AppScreen.Home -> HomeScreen()
            AppScreen.Profile -> UserProfileScreen()
            AppScreen.Post -> HomeScreen()
            AppScreen.Messages -> HomeScreen()
            AppScreen.Notifications -> HomeScreen()
        }
    }
}