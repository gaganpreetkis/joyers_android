package com.joyersapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.domain.usecase.RegisterUseCase
import com.joyersapp.auth.presentation.signup.SignupNavigationEvent
import com.joyersapp.core.SessionManager
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
    private val sessionManager: SessionManager,
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

            is UserProfileEvent.Logout -> {
                viewModelScope.launch {
                    sessionManager.logout()
                }
            }

            UserProfileEvent.SubmitClicked -> TODO()
            is UserProfileEvent.OnDialogClosed -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = false,
                        showTitlesDialog = false,
                        showEditProfileHeaderDialog = false,
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
            is UserProfileEvent.OnEditTitleClicked -> {
                _uiState.update {
                    it.copy(
                        showTitlesDialog = true,
                    )
                }
            }
            is UserProfileEvent.OnEditProfileHeader -> {
                _uiState.update {
                    it.copy(
                        showEditProfileHeaderDialog = true,
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
                            username = response.username ?: "N/A",
                            fullname = (response.firstName ?: "") + (response.lastName ?: ""),
                            location = response.joyerLocation ?: "N/A",
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            likes = response.likesCount ?: "N/A",
                            following = response.followingCount ?: "N/A",
                            followers = response.followersCount ?: "N/A",
                            joyerStatus = response.joyerStatus ?: "N/A",
                            titleName = response.title?.name ?: "N/A",
                            subTitleName = response.subTitle?.name ?: "N/A",
                            areaOfInterest = response.interests,
                            languages = response.languages,
                            joySince = response.joySince ?: "N/A",
                            joySinceDuration = response.joySinceDuration ?: "N/A",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality?.name ?: "N/A",
                            ethnicity = response.ethnicity?.name ?: "N/A",
                            faith = response.faith?.name ?: "N/A",
                            educationName = response.education?.name ?: "N/A",
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