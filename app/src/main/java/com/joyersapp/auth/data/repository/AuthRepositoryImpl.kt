package com.joyersapp.auth.data.repository

import com.joyersapp.auth.data.local.SessionLocalDataSource
import com.joyersapp.auth.data.remote.AuthApi
import com.joyersapp.auth.data.remote.dto.ApiErrorDto
import com.joyersapp.auth.data.remote.dto.CheckUsernameRequestDto
import com.joyersapp.auth.data.remote.dto.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordResponseDto
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.repository.AuthRepository
import com.joyersapp.utils.ApiErrorException
import com.joyersapp.utils.parseNetworkError
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

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

    override suspend fun checkUsername(username: String): Result<CheckUsernameResponseDto> =
        try {
            val response = api.checkUsername(CheckUsernameRequestDto(username))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun forgotPassword(params: ForgotPasswordRequestDto): Result<ForgotPasswordResponseDto> =
        try {
            val response = api.forgotPassword(params)
            when(response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            errorBody = ApiErrorDto(response.message),
                            message = response.message
                        )
                    )
                }
                else -> Result.failure(IllegalArgumentException("Something went wrong", Exception()))
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
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