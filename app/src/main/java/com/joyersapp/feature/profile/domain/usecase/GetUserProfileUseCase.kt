package com.joyersapp.feature.profile.domain.usecase

import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationResponseDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterResponseDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import com.joyersapp.feature.profile.data.remote.dto.UserProfile
import com.joyersapp.feature.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<UserProfile> {

        return profileRepository.getUserProfile(  )

    }
}