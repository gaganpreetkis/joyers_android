package com.joyersapp.auth.data.remote

import com.joyersapp.auth.data.remote.dto.CheckUsernameRequestDto
import com.joyersapp.auth.data.remote.dto.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

//    @POST("auth/login")
//    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @POST("auth/check-username")
    suspend fun checkUsername(
        @Body body: CheckUsernameRequestDto
    ): CheckUsernameResponseDto

    @POST("auth/register")
    suspend fun register(@Body body: CheckUsernameRequestDto): CheckUsernameResponseDto

    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body body: ForgotPasswordRequestDto
    ): ForgotPasswordResponseDto
}