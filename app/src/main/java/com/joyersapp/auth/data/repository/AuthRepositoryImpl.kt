package com.joyersapp.auth.data.repository

import com.joyersapp.auth.data.local.SessionLocalDataSource
import com.joyersapp.auth.data.remote.AuthApi
import com.joyersapp.auth.data.remote.dto.CheckUsernameRequestDto
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionLocalDataSource: SessionLocalDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Unit> =
        try {
//            val response = api.login(LoginRequestDto(email, password))
//            sessionLocalDataSource.storeSession(
//                userId = response.userId,
//                email = response.email,
//                accessToken = response.accessToken
//            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun checkUsername(username: String): Result<Boolean> =
        try {
            val response = api.checkUsername(CheckUsernameRequestDto(username))
            Result.success(response.available)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun forgotPassword(name: String): Result<Boolean> =
        try {
            val response = api.forgotPassword(ForgotPasswordRequestDto(name))
            Result.success(response.success)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<Unit> =
        try {
//            val response = api.register(
//                RegisterRequestDto(name = name, email = email, password = password)
//            )
//            // auto-login after signup
//            sessionLocalDataSource.storeSession(
//                userId = response.userId,
//                email = response.email,
//                accessToken = response.accessToken
//            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun logout() {
        sessionLocalDataSource.clearSession()
    }

    override fun observeAuthState(): Flow<AuthState> =
        sessionLocalDataSource.authState
}