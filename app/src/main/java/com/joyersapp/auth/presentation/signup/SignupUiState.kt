package com.joyersapp.auth.presentation.signup

import androidx.compose.ui.text.input.TextFieldValue
import com.joyersapp.R
import com.joyersapp.utils.UiText

data class SignupUiState(
    val name: String = "",
    val isUsernameAvailable: Boolean? = null, // null = unknown
//    val checkingUsername: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,

    val username: TextFieldValue = TextFieldValue(""),
    val password: String = "",
    val confirmPassword: String = "",

    val showUsernameLoader: Boolean = false,
    val showUsernameError: Boolean = false,
    val showUsernameSuggestions: Boolean = false,
    val usernameError: UiText? = null,
    val usernameSuggestions: List<String> = emptyList(),

    val isValidUsername: Boolean = false,
    val isValidEmail: Boolean = false,
    val isValidPhone: Boolean = false,
    val isValidPassword: Boolean = false,

    val isPhoneMode: Boolean = false,
    val email: String = "",
    val emailError: String? = null,
    val phone: String = "",
    val emailPhoneError: String? = null,

    val verificationError: String? = null,
    val showVerification: Boolean = false,
    val verificationCode: String = "",
    val codeSentMessage: UiText = UiText.StringResource(R.string.code_sent_to_email),

    val passwordError: UiText? = null,
    val confirmPasswordError: UiText? = null,
    val showPasswordFields: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    val selectedCountryCode: String = "+1",
    val selectedCountryNameCode: String = "US",
    val signupError: String? = null,



    val isPasswordFocused: Boolean = false,
    val isConfirmPasswordFocused: Boolean = false,
    val isUsernameFocused: Boolean = false,

    val isSuggestionSelected: Boolean = false
) {
    val isSignupButtonEnabled: Boolean
        get() = if (showPasswordFields) {
            isValidPassword && confirmPassword == password
        } else {
            isValidUsername && (isValidEmail || isValidPhone)
        }
    val signInButtonText: String
        get() = if (!showPasswordFields && isSignupButtonEnabled) {
            "Next"
        } else {
            "Sign up"
        }
}