package com.joyersapp.feature.profile.presentation

import com.joyersapp.auth.presentation.identity.IdentityEvent
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.feature.profile.data.remote.dto.EditProfileHeaderDialogDto
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto

sealed class UserProfileEvent {
    object Load : UserProfileEvent()
    data class UpdateUserData(val requestDto: UserProfileGraphRequestDto) : UserProfileEvent()
    data class UpdateProfileHeaderData(val profileHeaderData: EditProfileHeaderDialogDto) : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()
    data class Logout(val tab: Int) : UserProfileEvent()
    data class ToggleProfileHeaderDialog(val show: Boolean, val updateProfileHeaderData: Boolean) : UserProfileEvent()
    data class ToggleDescriptionDialog(val key: String = "", val isMultiSelectEnabled: Boolean = false, val show: Boolean, val headers: List<String>, val titlesData: List<ProfileTitlesData>, val selectedItems: List<ProfileTitlesData> = emptyList()) : UserProfileEvent()
    data class ToggleIdentificationDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleMentionJoyersDialog(val show: Boolean) : UserProfileEvent()
    data class BackgroundPicturePathChanged(val value: String) : UserProfileEvent()
    data class ProfilePicturePathChanged(val value: String) : UserProfileEvent()
    data class OnApplyIdentification(val value: IdentificationData) : UserProfileEvent()
    data class OnApplyDescription(val key: String, val value: List<ProfileTitlesData>) : UserProfileEvent()

}