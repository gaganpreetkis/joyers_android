package com.joyersapp.auth.data.remote.dto

data class LoginRequestDto(
    var country_code: String = "",
    var mobile: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = ""
)