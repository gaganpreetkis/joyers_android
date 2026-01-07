package com.joyersapp.feature.profile.presentation

import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData

data class EditMagneticsUiState(


    val selectedIds: List<String> = arrayListOf(),
    val isMultiselectEnabled: Boolean = false,
    val key: String = "",
    val value: List<ProfileTitlesData>? = null,

    val username: String = "",
    val fullname: String = "",
    val location: ProfileMeta? = null,
    val profilePicture: String = "",
    val backgroundPicture: String = "",

    val joyerStatus: String = "",
    val title: ProfileMeta? = null,
    val subTitle: ProfileMeta? = null,
    val areaOfInterest: List<Interests> = emptyList(),

    val gender: String = "",
    val ethnicity: ProfileMeta? = null,
    val faith: ProfileMeta? = null,
    val education: ProfileMeta? = null,
    val languages: List<Languages>? = null,

    val joyerType: String = "",
    val children: String = "",
    val birthday: String = "",
    val relationship: ProfileMeta? = null,
    val nationality: List<Nationality>? = null,
    val politicalIdeology: List<PoliticalIdeology>? = null,

    val dialogHeader: List<String> = emptyList(),
    val titlesData: List<ProfileTitlesData> = emptyList(),
    val titles: List<ProfileTitlesData> = emptyList(),
    val countryList: List<ProfileTitlesData> = emptyList(),
    val educationList: List<ProfileTitlesData> = emptyList(),
    val ethenicityList: List<ProfileTitlesData> = emptyList(),
    val faithReligionList: List<ProfileTitlesData> = emptyList(),
    val interestList: List<ProfileTitlesData> = emptyList(),
    val politicalIdeologyList: List<ProfileTitlesData> = emptyList(),
    val relationShipList: List<ProfileTitlesData> = emptyList(),
    val languageList: List<ProfileTitlesData> = emptyList(),




    val showTitlesDialog: Boolean = false,
    val showIdentificationDialog: Boolean = false,
    val showEditProfileHeaderDialog: Boolean = false,
    val showEditDescriptionDialog: Boolean = false,
    val showMentionJoyersDialog: Boolean = false,
    val error: String? = null,


    )