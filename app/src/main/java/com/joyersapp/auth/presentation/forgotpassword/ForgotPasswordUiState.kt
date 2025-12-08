package com.joyersapp.auth.presentation.forgotpassword

data class ForgotPasswordUiState (
    val isLoading: Boolean = false,
    val isPhoneMode: Boolean = false,
    val showVerificationCode: Boolean = false,
    val isVerificationSuccess: Boolean = false,
    val usernameEmail: String = "",
    val verificationCode: String = "",
    val usernameEmailError: String? = null,
    val phoneError: String? = null,
    val verificationCodeError: String? = null,
    val tabError: String? = null,
    val selectedCountryCode: String = "+1",
    val phone: String = "",
    val mainText: String = "",
    val secondaryText: String = ""
)