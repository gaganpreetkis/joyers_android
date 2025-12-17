package com.joyersapp.feature.dashboard.presentation.user_profile

import androidx.compose.ui.text.input.TextFieldValue

sealed class UserProfileEvent {
    object SubmitClicked : UserProfileEvent()
    data class TabSelected(val tab: Int) : UserProfileEvent()

}