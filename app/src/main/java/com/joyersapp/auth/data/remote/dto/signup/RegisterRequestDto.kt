package com.joyersapp.auth.data.remote.dto.signup


sealed class RegisterRequestDto {
    data class WithEmail(
        val username: String,
        val email: String
    ) : RegisterRequestDto()

    data class WithPhone(
        val username: String,
        val mobile: String,
        val country_code: String
    ) : RegisterRequestDto()
}