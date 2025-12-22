package com.joyersapp.feature.dashboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AppScaffoldViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AppScaffoldUiState())
    val uiState = _uiState.asStateFlow()

    fun onBottomTabSelected(tab: BottomTab) {
        _uiState.update {
            it.copy(
                selectedTab = tab,
                currentScreen = tab.toScreen(),
                headerTitle = tab.label,
                headerSubtitle = null
            )
        }
    }

    fun onBackClicked() {
        // handle global back
    }

    fun onMenuClicked() {
        // handle menu actions
    }
}

data class AppScaffoldUiState(
    val headerTitle: String = "Sara_99",
    val headerSubtitle: String? = null,
    val selectedTab: BottomTab = BottomTab.HOME,
    val currentScreen: AppScreen = AppScreen.Home,
)

enum class AppScreen {
    Home, Profile, Post, Messages, Notifications
}

fun BottomTab.toScreen(): AppScreen = when (this) {
    BottomTab.HOME -> AppScreen.Home
    BottomTab.PROFILE -> AppScreen.Profile
    BottomTab.POST -> AppScreen.Post
    BottomTab.CONTACTS -> AppScreen.Messages
    BottomTab.NOTIFICATIONS -> AppScreen.Notifications
}