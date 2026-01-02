package com.joyersapp.feature.profile.presentation

import com.joyersapp.auth.presentation.identity.IdentityEvent
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto

sealed class UserProfileEvent {
    object Load : UserProfileEvent()
    data class UpdateUserData(val requestDto: UserProfileGraphRequestDto) : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()
    data class Logout(val tab: Int) : UserProfileEvent()
    data class ToggleProfileHeaderDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleDescriptionDialog(val show: Boolean, val headers: List<String>, val titlesData: List<ProfileTitlesData>) : UserProfileEvent()
    data class ToggleIdentificationDialog(val show: Boolean) : UserProfileEvent()
    data class ToggleMentionJoyersDialog(val show: Boolean) : UserProfileEvent()
    data class BackgroundPicturePathChanged(val value: String) : UserProfileEvent()
    data class ProfilePicturePathChanged(val value: String) : UserProfileEvent()

}