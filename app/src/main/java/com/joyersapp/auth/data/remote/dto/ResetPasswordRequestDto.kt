package com.joyersapp.auth.data.remote.dto

data class ResetPasswordRequestDto(
    var username: String = "",
    var otp_code: String = "",
    var new_password: String = "",
    var confirm_password: String = ""
)
