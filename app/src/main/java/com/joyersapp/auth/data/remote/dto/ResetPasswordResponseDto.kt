package com.joyersapp.auth.data.remote.dto

data class ResetPasswordResponseDto(
    var statusCode: Int,
    var message: String = "",
    var user_id: String = ""
)
