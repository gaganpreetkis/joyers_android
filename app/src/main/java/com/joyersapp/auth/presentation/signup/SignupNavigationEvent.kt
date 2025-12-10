package com.joyersapp.auth.presentation.signup

sealed class SignupNavigationEvent {
    data class RegistrationCompleted(val token: String, val userId: String) : SignupNavigationEvent()
    data object NavigateToLogin : SignupNavigationEvent()
}