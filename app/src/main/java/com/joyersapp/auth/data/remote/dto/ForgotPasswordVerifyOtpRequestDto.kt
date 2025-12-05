package com.joyersapp.auth.data.remote.dto

data class ForgotPasswordVerifyOtpRequestDto(
    var purpose: String = "",
    var username: String = "",
    var otp_code: String = ""
)