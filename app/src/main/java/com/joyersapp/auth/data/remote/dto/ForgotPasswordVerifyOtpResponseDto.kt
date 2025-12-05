package com.joyersapp.auth.data.remote.dto

data class ForgotPasswordVerifyOtpResponseDto(
    val statusCode: Int,
    val success: Boolean,
    val message: String
)