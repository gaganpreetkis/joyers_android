package com.joyersapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.domain.usecase.RegisterUseCase
import com.joyersapp.auth.presentation.signup.SignupNavigationEvent
import com.joyersapp.feature.profile.domain.usecase.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    ) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UserProfileUiState(
            bannerUrl = null,
            avatarUrl = null
        )
    )
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        // simulate fetch
        viewModelScope.launch {
            getUserProfileData()
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {

            is UserProfileEvent.Load -> { getUserProfileData() }

            is UserProfileEvent.TabSelected -> {
                _uiState.update {
                    it.copy(
                        selectedTab = event.tab,
                    )
                }
            }

            UserProfileEvent.SubmitClicked -> TODO()
            is UserProfileEvent.OnDialogClosed -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = false,
                    )
                }
            }
            is UserProfileEvent.OnEditDescriptionClicked -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = true,
                    )
                }
            }
        }
    }


    private fun getUserProfileData(){
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = getUserProfileUseCase()

            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            username = response.username!!,
                            displayName = response.titleName!!,
                            location = response.joyerLocation!!,
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

}