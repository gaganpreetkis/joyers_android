package com.joyersapp.auth.presentation.login

import android.util.Patterns
import com.joyersapp.utils.isValidUsername

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
) {
    val isFormValid : Boolean =
        if (isPhoneMode) {
            // PHONE VALIDATION
            username.isNotEmpty() &&
                    username.all { it.isDigit() } &&
                    username.length in 10..15
        } else {
            // USERNAME or EMAIL VALIDATION
            username.isNotEmpty() && (isValidUsername(username) || Patterns.EMAIL_ADDRESS.matcher(username).matches())
        }
}