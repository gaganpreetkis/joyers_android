package com.joyersapp.feature.profile.data.repository

import android.util.Log
import com.google.gson.Gson
import com.joyersapp.auth.data.local.SessionLocalDataSource
import com.joyersapp.auth.data.remote.AuthApi
import com.joyersapp.auth.data.remote.dto.ApiErrorDto
import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpResponseDto
import com.joyersapp.auth.data.remote.dto.LoginRequestDto
import com.joyersapp.auth.data.remote.dto.LoginResponseDto
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterRequestDto
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterResponseDto
import com.joyersapp.auth.data.remote.dto.ResetPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ResetPasswordResponseDto
import com.joyersapp.auth.data.remote.dto.identity.Title
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.repository.AuthRepository
import com.joyersapp.feature.profile.data.remote.ProfileApi
import com.joyersapp.feature.profile.data.remote.dto.GetUserProfileResponseDto
import com.joyersapp.feature.profile.data.remote.dto.UserProfile
import com.joyersapp.feature.profile.domain.repository.ProfileRepository
import com.joyersapp.utils.ApiErrorException
import com.joyersapp.utils.emptyPart
import com.joyersapp.utils.parseNetworkError
import java.io.File
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.net.URLConnection

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val sessionLocalDataSource: SessionLocalDataSource
) : ProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile> =
        try {
            val response = api.getUserProfile()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data!!)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun logout() {
        sessionLocalDataSource.clearUserSession()
    }


    // auto-login after signup
//            sessionLocalDataSource.saveUser(
//                userId = response.userId,
//                email = response.email,
//                accessToken = response.accessToken
//            )
}