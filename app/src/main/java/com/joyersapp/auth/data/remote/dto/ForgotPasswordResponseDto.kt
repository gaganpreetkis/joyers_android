package com.joyersapp.auth.data.remote.dto

data class ForgotPasswordResponseDto(
    val statusCode: Int,
    val success: Boolean,
    val message: String
)