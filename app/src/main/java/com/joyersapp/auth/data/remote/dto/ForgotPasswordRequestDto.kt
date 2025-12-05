package com.joyersapp.auth.data.remote.dto

data class ForgotPasswordRequestDto(
    var country_code: String = "",
    var mobile: String = "",
    var username: String = "",
    var email: String = ""
)