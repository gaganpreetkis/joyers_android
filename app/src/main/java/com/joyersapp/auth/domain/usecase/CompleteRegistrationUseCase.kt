package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationResponseDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class CompleteRegistrationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: CompleteRegistrationRequestDto): Result<CompleteRegistrationResponseDto> {

        return authRepository.completeRegistration( params )

//        return when (params) {
//            is CompleteRegistrationRequestDto.WithEmail ->
//                authRepository.completeRegistrationWithEmail(
//                    username = params.username,
//                    email = params.email,
//                    otpCode = params.otp_code,
//                    password = params.password,
//                    confirmPassword = params.confirmPassword,
//                )
//
//            is CompleteRegistrationRequestDto.WithPhone ->
//                authRepository.completeRegistrationWithPhone(
//                    username = params.username,
//                    mobile = params.mobile,
//                    countryCode = params.country_code,
//                    otpCode = params.otp_code,
//                    password = params.otp_code,
//                    confirmPassword = params.otp_code,
//                )
//        }
    }
}