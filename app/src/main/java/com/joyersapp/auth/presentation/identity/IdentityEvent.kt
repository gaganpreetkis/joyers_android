package com.joyersapp.auth.presentation.identity

sealed class IdentityEvent {
    data class LoadingChanged(val value: Boolean) : IdentityEvent()
    data class TokenChanged(val value: String) : IdentityEvent()
    data class UserIdChanged(val value: String) : IdentityEvent()
    data class NameChanged(val value: String) : IdentityEvent()
    data class RemainingCharChanged(val value: Int) : IdentityEvent()
    data class JoyerLocationChanged(val value: String) : IdentityEvent()
    data class JoyerStatusChanged(val value: String) : IdentityEvent()
    data class TitleIdChanged(val value: String) : IdentityEvent()
    data class SubTitleIdChanged(val value: String) : IdentityEvent()
    data class ProfilePicturePathChanged(val value: String) : IdentityEvent()
    data class BackgroundPicturePathChanged(val value: String) : IdentityEvent()
    data object OnMultiStepRegister : IdentityEvent()
}