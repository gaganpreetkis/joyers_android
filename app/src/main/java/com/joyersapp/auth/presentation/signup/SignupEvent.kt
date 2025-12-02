package com.joyersapp.auth.presentation.signup

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignupEvent {
    data class NameChanged(val value: String) : SignupEvent()
    data class EmailChanged(val value: String) : SignupEvent()
    data class UsernameChanged(val value: TextFieldValue) : SignupEvent()
    data class PasswordChanged(val value: String) : SignupEvent()
    data class ConfirmPasswordChanged(val value: String) : SignupEvent()
    object SubmitClicked : SignupEvent()
}