package com.joyersapp.feature.profile.presentation

import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData

sealed class UserProfileEvent {
    object Load : UserProfileEvent()
    object SubmitClicked : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()
    data class Logout(val tab: Int) : UserProfileEvent()
    data class OnDialogClosed(val id: Int) : UserProfileEvent()
    data class OnEditDescriptionClicked(val tab: Int) : UserProfileEvent()
    data class OnEditTitleClicked(val tab: Int) : UserProfileEvent()
    data class OnEditProfileHeader(val id: Int) : UserProfileEvent()
    data class OnEditDescription(val id: Int, val headers: List<String>, val titlesData: List<ProfileTitlesData>) : UserProfileEvent()
    data class OnEditIdentification(val id: Int) : UserProfileEvent()

}