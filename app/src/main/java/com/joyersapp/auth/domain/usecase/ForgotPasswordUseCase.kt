package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.ForgotPasswordResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String): Result<ForgotPasswordResponseDto> = authRepository.forgotPassword(username)
}