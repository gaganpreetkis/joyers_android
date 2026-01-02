package com.joyersapp.feature.profile.domain.usecase

import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject

class GetEducationListUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<ProfileTitlesData>> = profileRepository.getEducationList()
}