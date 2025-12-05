package com.joyersapp.auth.data.remote.dto.signup

sealed class VerifyOtpRequestDto {
    data class WithEmail(
        val username: String,
        val email: String,
        val otp_code: String,
    ) : VerifyOtpRequestDto()

    data class WithPhone(
        val username: String,
        val mobile: String,
        val country_code: String,
        val otp_code: String,
    ) : VerifyOtpRequestDto()
}