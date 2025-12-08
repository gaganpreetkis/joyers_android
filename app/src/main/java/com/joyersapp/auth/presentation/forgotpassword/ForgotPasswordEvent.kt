package com.joyersapp.auth.presentation.forgotpassword

sealed class ForgotPasswordEvent {
    data class LoadingChanged(val value: Boolean) : ForgotPasswordEvent()
    data class IsPhoneModeChanged(val value: Boolean) : ForgotPasswordEvent()
    data class ShowVerificationCodeChanged(val value: Boolean) : ForgotPasswordEvent()
    data class UsernameEmailChanged(val value: String) : ForgotPasswordEvent()
    data class PhoneChanged(val value: String) : ForgotPasswordEvent()
    data class VerificationCodeChanged(val value: String) : ForgotPasswordEvent()
    data class UsernameEmailErrorChanged(val value: String?) : ForgotPasswordEvent()
    data class PhoneErrorChanged(val value: String?) : ForgotPasswordEvent()
    data class VerificationCodeErrorChanged(val value: String?) : ForgotPasswordEvent()
    data class TabErrorChanged(val value: String?) : ForgotPasswordEvent()
    data class SelectedCountryCodeChanged(val value: String) : ForgotPasswordEvent()
    data object OnNextButtonClicked : ForgotPasswordEvent()
    data object OnVerifyButtonClicked : ForgotPasswordEvent()
}