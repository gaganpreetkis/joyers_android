package com.joyersapp.feature.profile.presentation

sealed class UserProfileEvent {
    object Load : UserProfileEvent()
    object SubmitClicked : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()
    data class Logout(val tab: Int) : UserProfileEvent()
    data class OnDialogClosed(val tab: Int) : UserProfileEvent()
    data class OnEditDescriptionClicked(val tab: Int) : UserProfileEvent()
    data class OnEditTitleClicked(val tab: Int) : UserProfileEvent()

}