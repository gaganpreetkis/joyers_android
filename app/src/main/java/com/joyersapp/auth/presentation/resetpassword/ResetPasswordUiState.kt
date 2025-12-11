package com.joyersapp.auth.presentation.resetpassword

data class ResetPasswordUiState (
    val isLoading: Boolean = false,
    val isPasswordResetSuccess: Boolean = false,
    val isPhoneMode: Boolean = false,
    val isPasswordFocused: Boolean = false,
    val isConfirmPasswordFocused: Boolean = false,
    val isLoginApiSuccess: Boolean = false,
    val identifierValue: String = "",
    val selectedCountryCode: String = "",
    val selectedCountryNameCode: String = "",
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val identifierError: String? = null,
    val password: String = "",
    val confirmPassword: String = "",
    val verificationCode: String = "",
    val passwordResetSuccessMessage: String = "",
    val passwordResetErrorMessage: String = ""
)