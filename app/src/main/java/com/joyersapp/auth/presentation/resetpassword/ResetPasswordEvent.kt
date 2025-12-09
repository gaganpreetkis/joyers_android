package com.joyersapp.auth.presentation.resetpassword

sealed class ResetPasswordEvent {
    data class LoadingChanged(val value: Boolean) : ResetPasswordEvent()
    data class IsPasswordResetSuccessChanged(val value: Boolean) : ResetPasswordEvent()
    data class IsPhoneModeChanged(val value: Boolean) : ResetPasswordEvent()
    data class IsPasswordFocusedChanged(val value: Boolean) : ResetPasswordEvent()
    data class IsConfirmPasswordFocusedChanged(val value: Boolean) : ResetPasswordEvent()
    data class IdentifierValueChanged(val value: String) : ResetPasswordEvent()
    data class SelectedCountryCodeChanged(val value: String) : ResetPasswordEvent()
    data class SelectedCountryNameCodeChanged(val value: String) : ResetPasswordEvent()
    data class PasswordErrorChanged(val value: String?) : ResetPasswordEvent()
    data class ConfirmPasswordErrorChanged(val value: String?) : ResetPasswordEvent()
    data class IdentifierErrorChanged(val value: String) : ResetPasswordEvent()
    data class PasswordChanged(val value: String) : ResetPasswordEvent()
    data class ConfirmPasswordChanged(val value: String) : ResetPasswordEvent()
    data class VerificationCodeChanged(val value: String) : ResetPasswordEvent()
    data class PasswordResetSuccessMessageChanged(val value: String) : ResetPasswordEvent()
    data class PasswordResetErrorMessageChanged(val value: String) : ResetPasswordEvent()
    data object OnVerifyButtonClicked : ResetPasswordEvent()
}