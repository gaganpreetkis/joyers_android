package com.joyersapp.feature.profile.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.Dp

data class UserProfileUiState(
    val username: String = "Sara_99",
    val displayName: String = "Sara Spiegel James",
    val title: String = "Typical Joyer",
    val location: String = "New York, USA",
    val likes: String = "11.1K",
    val following: String = "599",
    val followers: String = "155M",

    val tabs: List<String> = listOf("Status", "Identity", "Sparks", "Cards", "Gallery"),
    var selectedTab: Int = 0,
    val textWidths: MutableMap<Int, Dp> = mutableStateMapOf(),

    val bannerUrl: String? = null,
    val avatarUrl: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null
)