package com.joyersapp.auth.domain.repository

import com.joyersapp.auth.data.remote.dto.CheckUsernameResponseDto
import com.joyersapp.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<Unit>


    suspend fun checkUsername(username: String): Result<CheckUsernameResponseDto>

    suspend fun register(name: String, email: String, password: String): Result<Unit>

    suspend fun forgotPassword(name: String): Result<Boolean>

    suspend fun logout()

    fun observeAuthState(): Flow<AuthState>
}