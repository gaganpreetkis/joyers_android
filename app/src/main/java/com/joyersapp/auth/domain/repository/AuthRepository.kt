package com.joyersapp.auth.domain.repository

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
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun checkUsername(username: String): Result<CheckUsernameResponseDto>

    suspend fun registerWithEmail(username: String, email: String): Result<RegisterResponseDto>

    suspend fun registerWithPhone(username: String, mobile: String, countryCode: String): Result<RegisterResponseDto>

    suspend fun verifyOtpWithEmail(username: String, email: String, otpCode: String): Result<VerifyOtpResponseDto>

    suspend fun verifyOtpWithPhone(username: String, mobile: String, countryCode: String, otpCode: String): Result<VerifyOtpResponseDto>

    suspend fun completeRegistrationWithPhone(username: String, mobile: String, countryCode: String, otpCode: String, password: String, confirmPassword: String): Result<CompleteRegistrationResponseDto>
    suspend fun completeRegistrationWithEmail(username: String, email: String, otpCode: String, password: String, confirmPassword: String): Result<CompleteRegistrationResponseDto>
    suspend fun completeRegistration(params: CompleteRegistrationRequestDto): Result<CompleteRegistrationResponseDto>

    suspend fun forgotPassword(params: ForgotPasswordRequestDto): Result<ForgotPasswordResponseDto>

    suspend fun forgotPasswordVerifyOtp(params: ForgotPasswordVerifyOtpRequestDto): Result<ForgotPasswordVerifyOtpResponseDto>

    suspend fun resetPasswordVerifyOtp(params: ResetPasswordRequestDto): Result<ResetPasswordResponseDto>

    suspend fun login(params: LoginRequestDto): Result<LoginResponseDto>

    suspend fun multiStepRegister(params: MultiStepRegisterRequestDto): Result<MultiStepRegisterResponseDto>

    suspend fun logout()

    fun observeAuthState(): Flow<AuthState>
}