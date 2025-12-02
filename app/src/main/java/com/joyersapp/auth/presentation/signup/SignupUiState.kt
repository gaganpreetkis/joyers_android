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
    val isValidUsername: Boolean = false,
    val showUsernameError: Boolean = false,
    val showUsernameSuggestions: Boolean = false,
    val usernameError: UiText? = null,
    val usernameSuggestions: List<String> = emptyList(),

    val isPhoneMode: Boolean = false,
    val email: String = "",
    val emailError: String? = null,
    val isValidEmail: Boolean = false,
    val isValidPhone: Boolean = false,
    val phone: String = "",
    val emailPhoneError: String? = null,

    val verificationError: String? = null,
    val showVerification: Boolean = false,
    val verificationCode: String = "",
    val codeSentMessage: UiText = UiText.StringResource(R.string.code_sent_to_email),

    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val showPasswordFields: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    val selectedCountryCode: String = "+1",
    val signupError: String? = null,

    val signInButtonText: String = "Sign up", // you can override from VM using strings

    val isPasswordFocused: Boolean = false,
    val isConfirmPasswordFocused: Boolean = false,
    val isUsernameFocused: Boolean = false,

    val isSuggestionSelected: Boolean = false
)