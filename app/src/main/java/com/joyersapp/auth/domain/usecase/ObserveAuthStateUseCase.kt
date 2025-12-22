package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState> = authRepository.observeAuthState()
}