package com.joyersapp.feature.profile.domain.repository

import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationResponseDto
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
import com.joyersapp.auth.data.remote.dto.identity.Title
import com.joyersapp.auth.data.remote.dto.identity.TitlesResponseDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.feature.profile.data.remote.dto.GetUserProfileResponseDto
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun fetchTitles(): Result<List<ProfileTitlesData>>


    suspend fun logout()

}