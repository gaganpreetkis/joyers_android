package com.joyersapp.auth.data.remote.dto

data class ForgotPasswordVerifyOtpRequestDto(
    var purpose: String = "",
    var email: String = "",
    var username: String = "",
    var mobile: String = "",
    var country_code: String = "",
    var otp_code: String = ""
)