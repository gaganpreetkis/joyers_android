package com.joyersapp.feature.common

import androidx.compose.runtime.Composable
import com.joyersapp.auth.presentation.JoyersOathScreen
import com.joyersapp.feature.home.presentation.HomeScreen
import com.joyersapp.feature.profile.presentation.UserProfileScreen

@Composable
fun ScreenContentContainer(currentScreen: AppScreen) {
    when (currentScreen) {
        AppScreen.Home -> HomeScreen()
        AppScreen.Profile -> UserProfileScreen()
        AppScreen.Post -> HomeScreen()
        AppScreen.Messages -> HomeScreen()
        AppScreen.Notifications -> HomeScreen()
    }
}