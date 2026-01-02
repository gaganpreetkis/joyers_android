package com.joyersapp.feature.profile.data.remote

import com.joyersapp.auth.data.remote.dto.MultiStepRegisterResponseDto
import com.joyersapp.di.RequiresAuth
import com.joyersapp.feature.profile.data.remote.dto.UserProfileResponseDto
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesResponseDto
import com.joyersapp.feature.profile.data.remote.dto.UploadPictureServerResponse
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApi {

    @RequiresAuth
    @POST("user/update-user-profile-graph")
    suspend fun uploadUserProfile(
        @Body requestDto: UserProfileGraphRequestDto
    ): UserProfileResponseDto

    @RequiresAuth
    @Multipart
    @POST("user/upload-picture-server")
    suspend fun uploadPictureServer(
        @Part("image_status") id: RequestBody,
        @Part("profile_picture") profilePicture: MultipartBody.Part,
        @Part("background_picture") backgroundPicture: MultipartBody.Part
    ): UploadPictureServerResponse

    @RequiresAuth
    @GET("user/get-user-profile")
    suspend fun getUserProfile(): UserProfileResponseDto

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