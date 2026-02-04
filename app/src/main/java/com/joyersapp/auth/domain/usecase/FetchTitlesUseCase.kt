package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.identity.Title
import com.joyersapp.auth.data.remote.dto.identity.TitlesResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import jakarta.inject.Inject

class FetchTitlesUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<ProfileTitlesData>> = authRepository.fetchTitles()
}