package com.joyersapp.auth.domain.repository

import com.joyersapp.auth.data.remote.dto.signup.CheckUsernameResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordResponseDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpResponseDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String): Result<Unit>


    suspend fun checkUsername(username: String): Result<CheckUsernameResponseDto>

    suspend fun registerWithEmail(username: String, email: String): Result<RegisterResponseDto>

    suspend fun registerWithPhone(username: String, mobile: String, countryCode: String): Result<RegisterResponseDto>

    suspend fun verifyOtpWithEmail(username: String, email: String, otpCode: String): Result<VerifyOtpResponseDto>

    suspend fun verifyOtpWithPhone(username: String, mobile: String, countryCode: String, otpCode: String): Result<VerifyOtpResponseDto>

    suspend fun forgotPassword(params: ForgotPasswordRequestDto): Result<ForgotPasswordResponseDto>

    suspend fun forgotPasswordVerifyOtp(params: ForgotPasswordVerifyOtpRequestDto): Result<ForgotPasswordVerifyOtpResponseDto>

    suspend fun logout()

    fun observeAuthState(): Flow<AuthState>
}