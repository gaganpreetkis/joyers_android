package com.joyersapp.auth.data.remote.dto.signup

data class RegisterResponseDto(
    val email: Boolean,
    val message: String,
    val statusCode: Int?
)