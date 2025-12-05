package com.joyersapp.auth.data.remote.dto

data class ApiErrorDto(
    val message: String?,
    val error: String? = "",
    val statusCode: Int? = 0,
    val suggestions: List<String> = emptyList()
)