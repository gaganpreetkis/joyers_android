package com.joyersapp.feature.dashboard.presentation.user_profile

data class UserProfileUiState(
    val username: String = "Sara_99",
    val displayName: String = "Sara Spiegel James",
    val title: String = "Typical Joyer",
    val location: String = "New York, USA",
    val likes: String = "11.1K",
    val following: String = "599",
    val followers: String = "155M",

    val bannerUrl: String? = null,
    val avatarUrl: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null
)
