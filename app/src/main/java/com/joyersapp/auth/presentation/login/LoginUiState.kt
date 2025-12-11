package com.joyersapp.auth.presentation.login

data class LoginUiState (
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val apiOnlyUsernameErrorMessage: String = "",
    val apiErrorMessage: String = "",
    val apiFailedErrorMessage: String = "",
    val passwordVisible: Boolean = false,
    val emailPhoneError: Boolean = false,
    val passwordError: Boolean = false,
    val isPhoneMode: Boolean = false,
    val showSocialDialog: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoginApiSuccess: Boolean = false,
    val selectedCountryCode: String = "+1",
    val selectedCountryNameCode: String = "US",
)