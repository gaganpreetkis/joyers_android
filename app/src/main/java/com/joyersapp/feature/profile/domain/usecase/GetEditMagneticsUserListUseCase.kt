package com.joyersapp.feature.profile.domain.usecase

import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.domain.repository.ProfileRepository
import jakarta.inject.Inject

class GetEditMagneticsUserListUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(): Result<List<EditMagneticsUserListData>> {

        return profileRepository.getEditMagneticsUserList(  )

    }
}