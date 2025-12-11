package com.joyersapp.auth.data.remote.dto.signup

data class CheckUsernameResponseDto(
    val available: Boolean,
    val username: String,
    val message: String,
    val suggestions: List<String>?,
    val statusCode: Boolean
)