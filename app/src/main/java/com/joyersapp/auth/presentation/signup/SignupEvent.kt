package com.joyersapp.auth.presentation.signup

import androidx.compose.ui.text.input.TextFieldValue

sealed class SignupEvent {
    object SubmitClicked : SignupEvent()
    data class UsernameChanged(val value: TextFieldValue) : SignupEvent()
    data class EmailChanged(val value: String) : SignupEvent()
    data class PhoneChanged(val value: String) : SignupEvent()
    data class VerificationCodeChanged(val value: String) : SignupEvent()
    data class PasswordChanged(val value: String) : SignupEvent()
    data class ConfirmPasswordChanged(val value: String) : SignupEvent()

    // Focus changes
    data class UsernameFocusChanged(val isFocused: Boolean) : SignupEvent()
    data class PasswordFocusChanged(val isFocused: Boolean) : SignupEvent()
    data class ConfirmPasswordFocusChanged(val isFocused: Boolean) : SignupEvent()
    data class EmailFocusChanged(val isFocused: Boolean) : SignupEvent()
    data class PhoneFocusChanged(val isFocused: Boolean) : SignupEvent()

    // Mode toggles
    data object TogglePhoneMode : SignupEvent()          // switch email <-> phone
    data object TogglePasswordVisibility : SignupEvent()
    data object ToggleConfirmPasswordVisibility : SignupEvent()
    data object TogglePasswordFieldsVisibility : SignupEvent()

    // Username check / suggestions
    data object CheckUsername : SignupEvent()
    data class UsernameCheckResult(
        val available: Boolean,
        val error: String?,
        val suggestions: List<String>
    ) : SignupEvent()

    data class UsernameSuggestionClicked(val suggestion: String) : SignupEvent()

    // Verification
    data object SendVerificationCode : SignupEvent()
    data object VerifyCode : SignupEvent()

    // Signup
    data object SubmitSignup : SignupEvent()
    data object ClearSignupError : SignupEvent()

    // Country code
    data class CountryCodeChanged(val code: String) : SignupEvent()

    data class CountryNameCodeChanged(val nameCode: String) : SignupEvent()

    // Button text / general UI triggers (optional)
    data class SignInButtonTextChanged(val text: String) : SignupEvent()
}