package com.joyersapp.auth.presentation.signup

sealed class SignupNavigationEvent {
    data object RegistrationCompleted : SignupNavigationEvent()
    data object NavigateToLogin : SignupNavigationEvent()
}