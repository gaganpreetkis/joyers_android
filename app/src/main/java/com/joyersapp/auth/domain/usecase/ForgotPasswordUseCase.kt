package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String): Result<Boolean> = authRepository.forgotPassword(username)
}