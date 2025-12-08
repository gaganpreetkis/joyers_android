package com.joyersapp.auth.data.remote.dto.signup

data class VerifyOtpResponseDto(
    val user_id: String?,
    val username: String,
    val message: String,
    val purpose: String?,
    val statusCode: Int?
)