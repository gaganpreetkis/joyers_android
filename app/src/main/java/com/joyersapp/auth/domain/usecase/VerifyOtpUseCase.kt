package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class VerifyOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: VerifyOtpRequestDto): Result<VerifyOtpResponseDto> {
        return when (params) {
            is VerifyOtpRequestDto.WithEmail ->
                authRepository.verifyOtpWithEmail(
                    username = params.username,
                    email = params.email,
                    otpCode = params.otp_code
                )

            is VerifyOtpRequestDto.WithPhone ->
                authRepository.verifyOtpWithPhone(
                    username = params.username,
                    mobile = params.mobile,
                    countryCode = params.country_code,
                    otpCode = params.otp_code
                )
        }
    }
}