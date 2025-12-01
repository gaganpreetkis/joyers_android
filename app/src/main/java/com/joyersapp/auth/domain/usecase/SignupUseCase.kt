package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Result<Unit> = authRepository.register(username, email, password)
}