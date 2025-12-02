package com.joyersapp.auth.presentation.signup

import androidx.compose.ui.text.input.TextFieldValue

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val username: TextFieldValue = TextFieldValue(""),
    val isUsernameAvailable: Boolean? = null, // null = unknown
    val checkingUsername: Boolean = false,
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUsernameFocused: Boolean? = false
)