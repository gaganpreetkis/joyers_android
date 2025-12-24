package com.joyersapp.auth.presentation.identity

data class IdentityUiState(
    val isLoading: Boolean = false,
    val isMultiSelectRegisterApiSuccess: Boolean = false,
    val token: String = "",
    val userId: String = "",
    val name: String = "",
    val joyerLocation: String = "",
    val profilePicturePath: String = "",
    val backgroundPicturePath: String = "",
    val joyerStatus: String = "",
    val titleId: String = "",
    val subTitleId: String = "",
    val maxLength: Int = 45,
    val remainingChars: Int = maxLength,
)