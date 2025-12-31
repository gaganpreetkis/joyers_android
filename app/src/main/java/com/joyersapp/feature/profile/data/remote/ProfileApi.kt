package com.joyersapp.feature.profile.data.remote

import com.joyersapp.auth.data.remote.dto.LoginRequestDto
import com.joyersapp.auth.data.remote.dto.LoginResponseDto
import com.joyersapp.di.RequiresAuth
import com.joyersapp.feature.profile.data.remote.dto.GetUserProfileResponseDto
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): LoginResponseDto

    @RequiresAuth
    @GET("user/get-user-profile")
    suspend fun getUserProfile(): GetUserProfileResponseDto

    @GET("auth/title-type")
    suspend fun getTitles(): ProfileTitlesResponseDto

}