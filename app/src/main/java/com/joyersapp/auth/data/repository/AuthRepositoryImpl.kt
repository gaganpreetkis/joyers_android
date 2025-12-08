package com.joyersapp.auth.data.repository

import com.joyersapp.auth.data.local.SessionLocalDataSource
import com.joyersapp.auth.data.remote.AuthApi
import com.joyersapp.auth.data.remote.dto.ApiErrorDto
import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationResponseDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
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
            when (response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            errorBody = ApiErrorDto(response.message, suggestions = response.suggestions ?: emptyList()),
                            message = response.message
                        )
                    )
                }
                else -> Result.failure(IllegalArgumentException("Something went wrong", Exception()))
            }

        }catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun verifyOtpWithEmail(
        username: String,
        email: String,
        otpCode: String,
    ): Result<VerifyOtpResponseDto> =
        try {
            val response = api.verifyOtp(
                VerifyOtpRequestDto.WithEmail(username = username, email = email, otp_code = otpCode)
            )
            when (response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message
                        )
                    )
                }
                else -> Result.failure(
                    ApiErrorException(
                        message = response.message
                    ))
            }
        }catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun verifyOtpWithPhone(
        username: String,
        mobile: String,
        countryCode: String,
        otpCode: String,
    ): Result<VerifyOtpResponseDto> =
        try {
            val response = api.verifyOtp(
                VerifyOtpRequestDto.WithPhone(username = username, mobile = mobile, country_code= countryCode, otp_code = otpCode)
            )
            when (response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message
                        )
                    )
                }
                else -> Result.failure(
                    ApiErrorException(
                        message = response.message
                    ))
            }
        }catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun completeRegistrationWithPhone(
        username: String,
        mobile: String,
        countryCode: String,
        otpCode: String,
        password: String,
        confirmPassword: String
    ): Result<CompleteRegistrationResponseDto> =
        try {
            TODO("Not yet implemented")
//            val response = api.completeRegistration(
//                CompleteRegistrationRequestDto.WithPhone(username = username, mobile = mobile, country_code= countryCode, otp_code = otpCode)
//            )
//            when (response.statusCode) {
//                200 -> {
//                    Result.success(response)
//                }
//                400 -> {
//                    Result.failure(
//                        ApiErrorException(
//                            message = response.message
//                        )
//                    )
//                }
//                else -> Result.failure(
//                    ApiErrorException(
//                        message = response.message
//                    ))
//            }
        }catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun completeRegistrationWithEmail(
        username: String,
        email: String,
        otpCode: String,
        password: String,
        confirmPassword: String
    ): Result<CompleteRegistrationResponseDto> {
        TODO("Not yet implemented")
    }

    override suspend fun completeRegistration(
        params: CompleteRegistrationRequestDto
    ): Result<CompleteRegistrationResponseDto> =
        try {
            val response = when(params) {
                is CompleteRegistrationRequestDto.WithPhone -> api.completeRegistrationWithPhone(params)
                is CompleteRegistrationRequestDto.WithEmail -> api.completeRegistrationWithEmail(params)
            }
            when (response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: ""
                        )
                    )
                }
                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: ""
                    ))
            }
    }catch (e: HttpException) {
        val errorMsg = parseNetworkError(e)
        Result.failure(IllegalArgumentException(errorMsg, e))
    }  catch (e: Exception) {
        Result.failure(e)
    }




    override suspend fun forgotPassword(name: String): Result<Boolean> =
        try {
            val response = api.forgotPassword(ForgotPasswordRequestDto(name))
            Result.success(response.success)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun registerWithEmail(
        username: String,
        email: String
    ): Result<RegisterResponseDto> =
        try {
            val response = api.register(
                RegisterRequestDto.WithEmail(username = username, email = email)
            )
            when (response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message
                        )
                    )
                }
                else -> Result.failure(
                    ApiErrorException(
                        message = response.message
                    ))
            }
        }catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun registerWithPhone(
        username: String,
        mobile: String,
        countryCode: String
    ): Result<RegisterResponseDto> =
        try {
        val response = api.register(
            RegisterRequestDto.WithPhone(username = username, mobile = mobile, country_code = countryCode)
        )
            when (response.statusCode) {
                200 -> {
                    Result.success(response)
                }
                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message
                        )
                    )
                }
                else -> Result.failure(
                    ApiErrorException(
                        message = response.message
                    ))
            }
        }catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        }  catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun logout() {
        sessionLocalDataSource.clearSession()
    }

    override fun observeAuthState(): Flow<AuthState> =
        sessionLocalDataSource.authState



    // auto-login after signup
//            sessionLocalDataSource.storeSession(
//                userId = response.userId,
//                email = response.email,
//                accessToken = response.accessToken
//            )
}