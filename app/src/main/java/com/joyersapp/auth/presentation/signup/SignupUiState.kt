package com.joyersapp.auth.presentation.signup

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val username: String = "",
    val isUsernameAvailable: Boolean? = null, // null = unknown
    val checkingUsername: Boolean = false,
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)