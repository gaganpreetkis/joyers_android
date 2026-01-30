package com.joyersapp.feature.profile.presentation

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.Dp
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.data.remote.dto.EditProfileHeaderDialogDto
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileLanguagesData
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData

data class UserProfileUiState(

    val magneticsData: MagneticsData = MagneticsData(),
    val identificationData: IdentificationData = IdentificationData(),

    val profileHeaderData: ProfileHeaderData = ProfileHeaderData(),

    val username: String = "",
    val fullname: String = "",
    val joyerLocation: String? = null,
    val location: ProfileMeta? = null,
    val profilePicture: String? = null,
    val backgroundPicture: String? = null,
    val bio: String? = null,
    val websiteUrl: String? = null,

    val joyerStatus: String? = null,
    val titleName: String? = null,
    val subTitleName: String? = null,
    val title: ProfileMeta? = null,
    val subTitle: ProfileMeta? = null,
    val areaOfInterest: List<Interests>? = null,

    val gender: String? = null,
    val ethnicity: ProfileMeta? = null,
    val faith: ProfileMeta? = null,
    val language: String? = null,
    val languages: List<Languages>? = null,
    val selectedLanguages: List<Languages>? = null,
//    val selectedSignLanguages: List<Languages>? = null,
    val educationName: String? = null,

    val joySince: String? = null,
    val joySinceDuration: String? = null,
    val qrCode: String? = null,

    val displayName: String = "Sara Spiegel James",
    val joyerType: String = "Typical Joyer",
    val likes: String? = "0",
    val following: String? = "0",
    val followers: String? = "0",


    val children: String? = null,
    val birthday: String? = null,
    val education: ProfileMeta? = null,
    val relationship: ProfileMeta? = null,
    val nationality: List<Nationality>? = null,
    val politicalIdeology: List<PoliticalIdeology>? = null,

    val tabs: List<String> = listOf("Status", "Identity", "Sparks", "Cards", "Gallery"),
    var selectedTab: Int = 0,
    val textWidths: MutableMap<Int, Dp> = mutableStateMapOf(),

    val bannerUrl: String? = null,
    val avatarUrl: String? = null,

    val isLoading: Boolean = true,
    val isDataLoaded: Boolean = false,
    val errorMessage: String? = null,
    val dialogHeader: List<String> = emptyList(),
    val editMagneticsUserList: List<EditMagneticsUserListData> = emptyList(),
    val titlesData: List<ProfileTitlesData> = emptyList(),
    val selectedItems: List<ProfileTitlesData> = emptyList(),
    val titles: List<ProfileTitlesData> = emptyList(),
    val countryList: List<ProfileTitlesData> = emptyList(),
    val educationList: List<ProfileTitlesData> = emptyList(),
    val ethenicityList: List<ProfileTitlesData> = emptyList(),
    val faithReligionList: List<ProfileTitlesData> = emptyList(),
    val interestList: List<ProfileTitlesData> = emptyList(),
    val politicalIdeologyList: List<ProfileTitlesData> = emptyList(),
    val relationShipList: List<ProfileTitlesData> = emptyList(),
    val languageList: List<ProfileLanguagesData> = emptyList(),




    val showTitlesDialog: Boolean = false,
    val showIdentificationDialog: Boolean = false,
    val showEditProfileHeaderDialog: Boolean = false,
    val showMultipleSelectionsDialog: Boolean = false,
    val showMentionJoyersDialog: Boolean = false,
    val showDescriptionDialog: Boolean = false,
    val showDatePickerDialog: Boolean = false,
    val showLanguagesDialog: Boolean = false,


    val key: String = "",
    val selectedIds: List<String> = arrayListOf(),
    val isMultiselectEnabled: Boolean = false,
    val isSubTitleMode: Boolean = false,
    val highlightsFocusedId: String = "",
)