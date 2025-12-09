package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.ResetPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ResetPasswordResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: ResetPasswordRequestDto): Result<ResetPasswordResponseDto> = authRepository.resetPasswordVerifyOtp(params)
}