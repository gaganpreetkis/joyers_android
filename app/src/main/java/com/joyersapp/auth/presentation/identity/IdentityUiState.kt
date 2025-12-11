package com.joyersapp.auth.presentation.identity

data class IdentityUiState(
    val isLoading: Boolean = false,
    val isMultiSelectRegisterApiSuccess: Boolean = false,
    val token: String = "",
    val userId: String = "",
    val name: String = "",
    val joyerLocation: String = "",
    val joyerStatus: String = "",
    val titleId: String = "",
    val subTitleId: String = "",
    val profilePicturePath: String = "",
    val backgroundPicturePath: String = "",
)