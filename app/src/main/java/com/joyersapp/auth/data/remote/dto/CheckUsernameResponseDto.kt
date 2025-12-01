package com.joyersapp.auth.data.remote.dto

data class CheckUsernameResponseDto(
    val available: Boolean,
    val username: String,
    val message: String
)