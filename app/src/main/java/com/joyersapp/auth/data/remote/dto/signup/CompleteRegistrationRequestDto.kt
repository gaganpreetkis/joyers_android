package com.joyersapp.auth.data.remote.dto.signup

sealed class CompleteRegistrationRequestDto {
    data class WithEmail(
        val username: String,
        val email: String,
        val otp_code: String,
        val password: String,
        val confirmPassword: String,
    ) : CompleteRegistrationRequestDto()

    data class WithPhone(
        val username: String,
        val mobile: String,
        val country_code: String,
        val password: String,
        val otp_code: String,
        val confirmPassword: String,
    ) : CompleteRegistrationRequestDto()
}