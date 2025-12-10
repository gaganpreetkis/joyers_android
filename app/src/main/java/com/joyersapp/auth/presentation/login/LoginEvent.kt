package com.joyersapp.auth.presentation.login

sealed class LoginEvent {
    data class LoadingChanged(val value: Boolean) : LoginEvent()
    data class UsernameChanged(val value: String) : LoginEvent()
    data class PasswordChanged(val value: String) : LoginEvent()
    data class ApiOnlyUsernameErrorMessageChanged(val value: String) : LoginEvent()
    data class ApiErrorMessageChanged(val value: String) : LoginEvent()
    data class ApiFailedErrorMessageChanged(val value: String) : LoginEvent()
    data class PasswordVisibleChanged(val value: Boolean) : LoginEvent()
    data class EmailPhoneErrorChanged(val value: Boolean) : LoginEvent()
    data class PasswordErrorChanged(val value: Boolean) : LoginEvent()
    data class IsPhoneModeChanged(val value: Boolean) : LoginEvent()
    data class ShowSocialDialogChanged(val value: Boolean) : LoginEvent()
    data class RememberMeChanged(val value: Boolean) : LoginEvent()
    data class IsLoginApiSuccessChanged(val value: Boolean) : LoginEvent()
    data class SelectedCountryCodeChanged(val value: String) : LoginEvent()
    data class SelectedCountryNameCodeChanged(val value: String) : LoginEvent()
    data object OnLoginButtonClicked : LoginEvent()
}