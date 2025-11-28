package com.joyersapp.api

import com.joyersapp.api.request.CheckUserNameRequest
import com.joyersapp.response.CheckUserNameModel
import retrofit2.http.*

interface AuthApiService {
    /*@POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): SignUpResponse

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse*/

    @POST("auth/check-username")
    suspend fun checkUsername(@Body request: CheckUserNameRequest): CheckUserNameModel

    /*@GET("titles")  // If you need this
    suspend fun getTitles(): TitlesApiResponse*/
}