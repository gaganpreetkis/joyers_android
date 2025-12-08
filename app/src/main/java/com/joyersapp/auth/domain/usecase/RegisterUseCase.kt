package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: RegisterRequestDto): Result<RegisterResponseDto> {
        return when (params) {
            is RegisterRequestDto.WithEmail ->
                authRepository.registerWithEmail(
                    username = params.username,
                    email = params.email
                )

            is RegisterRequestDto.WithPhone ->
                authRepository.registerWithPhone(
                    username = params.username,
                    mobile = params.mobile,
                    countryCode = params.country_code
                )
        }
    }
}