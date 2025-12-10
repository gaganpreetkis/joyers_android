package com.joyersapp.auth.domain.usecase

import com.joyersapp.auth.data.remote.dto.LoginRequestDto
import com.joyersapp.auth.data.remote.dto.LoginResponseDto
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterRequestDto
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterResponseDto
import com.joyersapp.auth.domain.repository.AuthRepository
import jakarta.inject.Inject

class MultiStepRegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(params: MultiStepRegisterRequestDto): Result<MultiStepRegisterResponseDto> = authRepository.multiStepRegister(params)
}