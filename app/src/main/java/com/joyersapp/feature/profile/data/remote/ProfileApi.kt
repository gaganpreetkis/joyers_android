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

    @GET("auth/title-type")
    suspend fun getSubTitles(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-countries-list")
    suspend fun getCountryList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-education-list")
    suspend fun getEducationList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-ethnicity-list")
    suspend fun getEthenicityList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-faith-religions-list")
    suspend fun getFaithReligionList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-interests-list")
    suspend fun getInterestList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-political-ideology-list")
    suspend fun getPoliticalIdeologyList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-relationship-status-list")
    suspend fun getRelationShipList(): ProfileTitlesResponseDto

    @RequiresAuth
    @POST("user/get-language-list")
    suspend fun getLanguageList(): ProfileTitlesResponseDto

}