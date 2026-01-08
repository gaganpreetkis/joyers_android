package com.joyersapp.feature.profile.presentation

import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto

sealed class UserProfileEvent {
    object Load : UserProfileEvent()
    data object InitMagneticsData : UserProfileEvent()
    data class UpdateUserData(val requestDto: UserProfileGraphRequestDto) : UserProfileEvent()
    data class UpdateProfileHeaderData(val profileHeaderData: ProfileHeaderData) : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()
    data class Logout(val tab: Int) : UserProfileEvent()
    data class ToggleProfileHeaderDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleMultipleSelectionsDialog(val key: String = "", val show: Boolean, val isMultiSelectEnabled: Boolean = false, val headers: List<String> = emptyList(), val titlesData: List<ProfileTitlesData> = emptyList(), val selectedIds: List<String> = emptyList()) : UserProfileEvent()
    data class ToggleDescriptionDialog(val show: Boolean, val titlesData: List<ProfileTitlesData> = emptyList()) : UserProfileEvent()
    data class ToggleIdentificationDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleMentionJoyersDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleDatePickerDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleLanguageDialog(val show: Boolean) : UserProfileEvent()
    data class BackgroundPicturePathChanged(val value: String) : UserProfileEvent()
    data class ProfilePicturePathChanged(val value: String) : UserProfileEvent()
    data class OnApplyIdentification(val value: IdentificationData) : UserProfileEvent()
    data class OnApplyProfileHeader(val value: ProfileHeaderData) : UserProfileEvent()
    data class OnApplyMultipleSelections(val key: String, val value: List<ProfileTitlesData>) : UserProfileEvent()
    data class OnApplyDescription(val selectedTitle: ProfileMeta?, val selectedSubTitle: ProfileMeta?) : UserProfileEvent()
    data class OnNameChanged(val value: String) : UserProfileEvent()
    data class OnApplyBirthday(val value: String) : UserProfileEvent()
    data class OnApplyLanguage(val value: List<ProfileTitlesData>) : UserProfileEvent()
    data class OnClearMultipleSelections(val key: String) : UserProfileEvent()
    data class OnGenderSelected(val value: String) : UserProfileEvent()
    data class OnBioChanged(val value: String) : UserProfileEvent()
    data class OnWebsiteUrlChanged(val value: String) : UserProfileEvent()

}