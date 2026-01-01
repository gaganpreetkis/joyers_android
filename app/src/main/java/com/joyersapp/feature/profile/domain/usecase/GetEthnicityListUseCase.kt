package com.joyersapp.feature.profile.domain.usecase

import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject

class GetEthnicityListUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<ProfileTitlesData>> = profileRepository.getEthenicityList()
}