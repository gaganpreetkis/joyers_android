package com.joyersapp.auth.domain.model

data class AuthUser(
    val userId: String,
    val email: String,
    val token: String
)