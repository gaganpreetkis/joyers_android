package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class CheckUsernameUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String): Result<Boolean> =
        repository.checkUsername(username)
}