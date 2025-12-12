package com.joyersapp.feature.dashboard

import androidx.compose.runtime.Composable
import com.joyersapp.auth.presentation.JoyersOathScreen
import com.joyersapp.feature.dashboard.presentation.user_profile.UserProfileScreen

@Composable
fun ScreenContentContainer(currentScreen: AppScreen) {
    when (currentScreen) {
        AppScreen.Home -> UserProfileScreen()
        AppScreen.Search -> JoyersOathScreen()
        AppScreen.Create -> UserProfileScreen()
        AppScreen.Messages -> JoyersOathScreen()
        AppScreen.Profile -> UserProfileScreen()
    }
}