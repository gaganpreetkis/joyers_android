package com.joyersapp.auth.data.remote

import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpResponseDto
import com.joyersapp.auth.data.remote.dto.LoginRequestDto
import com.joyersapp.auth.data.remote.dto.LoginResponseDto
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterRequestDto
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterResponseDto
import com.joyersapp.auth.data.remote.dto.ResetPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ResetPasswordResponseDto
import com.joyersapp.auth.data.remote.dto.identity.TitlesResponseDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationResponseDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

//    @POST("auth/login")
//    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @POST("auth/check-username")
    suspend fun checkUsername(
        @Body body: CheckUsernameRequestDto
    ): CheckUsernameResponseDto

    @POST("auth/verify-otp")
    suspend fun verifyOtp(
        @Body body: VerifyOtpRequestDto.WithEmail
    ): VerifyOtpResponseDto

    @POST("auth/verify-otp")
    suspend fun verifyOtp(
        @Body body: VerifyOtpRequestDto.WithPhone
    ): VerifyOtpResponseDto
    @POST("auth/complete-registration")
    suspend fun completeRegistrationWithEmail(
        @Body body: CompleteRegistrationRequestDto.WithEmail
    ): CompleteRegistrationResponseDto
    @POST("auth/complete-registration")
    suspend fun completeRegistrationWithPhone(
        @Body body: CompleteRegistrationRequestDto.WithPhone
    ): CompleteRegistrationResponseDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequestDto.WithEmail): RegisterResponseDto

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequestDto.WithPhone): RegisterResponseDto

    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body body: ForgotPasswordRequestDto
    ): ForgotPasswordResponseDto

    @POST("auth/verify-otp")
    suspend fun forgotPasswordVerifyOtp(
        @Body body: ForgotPasswordVerifyOtpRequestDto
    ): ForgotPasswordVerifyOtpResponseDto

    @POST("auth/change-password")
    suspend fun resetPassword(
        @Body body: ResetPasswordRequestDto
    ): ResetPasswordResponseDto

    @POST("auth/title-joyerType")
    suspend fun titleType(): TitlesResponseDto

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): LoginResponseDto

    @Multipart
    @POST("auth/multistep-register")
    suspend fun multiStepRegister(
        @Header("Authorization") token: String,
        @Part("id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("joyer_location") joyerLocation: RequestBody,
        @Part("joyer_status") joyerStatus: RequestBody,
        @Part("title") title: RequestBody,
        @Part("sub_title") subTitle: RequestBody,
        @Part profilePicture: MultipartBody.Part,
        @Part backgroundPicture: MultipartBody.Part
    ): MultiStepRegisterResponseDto
}