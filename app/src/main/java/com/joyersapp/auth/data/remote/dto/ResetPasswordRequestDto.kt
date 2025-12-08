package com.joyersapp.auth.data.remote.dto

data class ResetPasswordRequestDto(
    var email: String = "",
    var username: String = "",
    var country_code: String = "",
    var mobile: String = "",
    var otp_code: String = "",
    var new_password: String = "",
    var confirm_password: String = ""
)
