package com.joyersapp.feature.profile.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.Dp
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData

data class UserProfileUiState(
    val username: String = "",
    val fullname: String = "",
    val location: String = "",
    val profilePicture: String = "",
    val backgroundPicture: String = "",

    val joyerStatus: String = "",
    val titleName: String = "",
    val subTitleName: String = "",
    val title: ProfileMeta? = null,
    val subTitle: ProfileMeta? = null,
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


    val children: String = "",
    val birthday: String = "",
    val relationship: String = "",
    val politicalIdeology: String = "",

    val tabs: List<String> = listOf("Status", "Identity", "Sparks", "Cards", "Gallery"),
    var selectedTab: Int = 0,
    val textWidths: MutableMap<Int, Dp> = mutableStateMapOf(),

    val bannerUrl: String? = null,
    val avatarUrl: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val dialogHeader: List<String> = emptyList(),
    val titles: List<ProfileTitlesData> = emptyList(),
    val countryList: List<ProfileTitlesData>? = null,
    val educationList: List<ProfileTitlesData>? = null,
    val ethenicityList: List<ProfileTitlesData>? = null,
    val faithReligionList: List<ProfileTitlesData>? = null,
    val interestList: List<ProfileTitlesData>? = null,
    val politicalIdeologyList: List<ProfileTitlesData>? = null,
    val relationShipList: List<ProfileTitlesData>? = null,
    val languageList: List<ProfileTitlesData>? = null,




    val showTitlesDialog: Boolean = false,
    val showIdentificationDialog: Boolean = false,
    val showEditProfileHeaderDialog: Boolean = false,
    val showEditDescriptionDialog: Boolean = false,
    val error: String? = null
)