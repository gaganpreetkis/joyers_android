package com.joyersapp.feature.profile.presentation

sealed class UserProfileEvent {
    object SubmitClicked : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()

}