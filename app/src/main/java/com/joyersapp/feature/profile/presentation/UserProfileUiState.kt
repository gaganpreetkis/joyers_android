package com.joyersapp.feature.profile.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.Dp
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta

data class UserProfileUiState(
    val username: String = "",
    val fullname: String = "",
    val location: String = "",
    val profilePicture: String = "",
    val backgroundPicture: String = "",

    val joyerStatus: String = "",
    val titleName: String = "",
    val subTitleName: String = "",
    val areaOfInterest: List<Interests> = emptyList(),

    val gender: String = "",
    val nationality: String = "",
    val ethnicity: String = "",
    val faith: String = "",
    val language: String = "",
    val languages: List<Languages> = emptyList(),
    val educationName: String = "",

    val joySince: String = "",
    val joySinceDuration: String = "",
    val qrCode: String = "",

    val displayName: String = "Sara Spiegel James",
    val joyerType: String = "Typical Joyer",
    val likes: String = "11.1K",
    val following: String = "599",
    val followers: String = "155M",

    val tabs: List<String> = listOf("Status", "Identity", "Sparks", "Cards", "Gallery"),
    var selectedTab: Int = 0,
    val textWidths: MutableMap<Int, Dp> = mutableStateMapOf(),

    val bannerUrl: String? = null,
    val avatarUrl: String? = null,

    val isLoading: Boolean = false,
    val showIdentificationDialog: Boolean = false,
    val showTitlesDialog: Boolean = false,
    val error: String? = null
)