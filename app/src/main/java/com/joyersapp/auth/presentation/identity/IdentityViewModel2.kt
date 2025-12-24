package com.joyersapp.auth.presentation.identity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.MultiStepRegisterRequestDto
import com.joyersapp.auth.domain.usecase.MultiStepRegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class IdentityViewModel2 @Inject constructor(
    private val multiStepRegisterUseCase: MultiStepRegisterUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(IdentityUiState())
    val uiState: StateFlow<IdentityUiState> = _uiState
    private var job: Job? = null

    fun onEvent(event: IdentityEvent) {
        when (event) {
            is IdentityEvent.LoadingChanged -> {
                _uiState.update { it.copy(isLoading = event.value) }
            }

            is IdentityEvent.TokenChanged -> {
                _uiState.update { it.copy(token = event.value) }
            }

            is IdentityEvent.UserIdChanged -> {
                _uiState.update { it.copy(userId = event.value) }
            }

            is IdentityEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.value) }
            }

            is IdentityEvent.RemainingCharChanged -> {
                _uiState.update { it.copy(remainingChars = event.value) }
            }

            is IdentityEvent.JoyerLocationChanged -> {
                _uiState.update { it.copy(joyerLocation = event.value) }
            }

            is IdentityEvent.JoyerStatusChanged -> {
                _uiState.update { it.copy(joyerStatus = event.value) }
            }

            is IdentityEvent.TitleIdChanged -> {
                _uiState.update { it.copy(titleId = event.value) }
            }

            is IdentityEvent.SubTitleIdChanged -> {
                _uiState.update { it.copy(subTitleId = event.value) }
            }

            is IdentityEvent.ProfilePicturePathChanged -> {
                _uiState.update { it.copy(profilePicturePath = event.value) }
            }

            is IdentityEvent.BackgroundPicturePathChanged -> {
                _uiState.update { it.copy(backgroundPicturePath = event.value) }
            }

            IdentityEvent.OnMultiStepRegister -> multiStepRegister()
        }
    }

    private fun multiStepRegister() {
        val state = _uiState.value
        val params = MultiStepRegisterRequestDto(
            token = state.token,
            id = state.userId,
            name = state.name,
            joyer_location = state.joyerLocation,
            joyer_status = state.joyerStatus,
            title = state.titleId,
            sub_title = state.subTitleId,
        )
        /*if (state.isPhoneMode) {
            params.country_code = state.selectedCountryCode
            params.mobile = state.identifierValue
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(state.identifierValue).matches()) {
                params.email = state.identifierValue
            } else {
                params.username = state.identifierValue
            }
        }
        if (params.username.isBlank() && params.email.isBlank() && params.country_code.isBlank() && params.mobile.isBlank()) return*/
        onEvent(IdentityEvent.LoadingChanged(true))
        job?.cancel()
        job = viewModelScope.launch {
            val result = multiStepRegisterUseCase(params, state.profilePicturePath, state.backgroundPicturePath)
            Log.e("reset password api", "result is: $result")
            result.fold(
                onSuccess = { response ->
                    // Access your message here
                    Log.e("reset msg", response.message)

                    if (response.statusCode == 200) {
                        //val (mainText, secondaryText) = parseForgotPasswordMessage(response.message)
                        _uiState.update { it.copy(
                            isLoading = false, isMultiSelectRegisterApiSuccess = true,
                        ) }
                    } else {
                        _uiState.update { it.copy(isLoading = false/*, passwordResetErrorMessage = response.message*/) }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false/*, passwordResetErrorMessage = error.message ?: "Something went wrong"*/) }
                }
            )
        }
    }
}