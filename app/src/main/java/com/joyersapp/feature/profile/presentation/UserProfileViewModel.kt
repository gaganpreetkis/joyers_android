package com.joyersapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.feature.profile.domain.usecase.FetchTitlesUseCase
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
    private val fetchTitlesUseCase: FetchTitlesUseCase,
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
                        showEditDescriptionDialog = false,
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
            is UserProfileEvent.OnEditDescription -> {
                _uiState.update {
                    it.copy(
                        showEditDescriptionDialog = true,
                        showIdentificationDialog = false,
                    )
                }
            }
            is UserProfileEvent.OnEditIdentification -> {
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
                            username = response.username ?: "",
                            fullname = (response.firstName ?: "") + " " + (response.lastName ?: ""),
                            location = response.joyerLocation ?: "",
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            likes = response.likesCount ?: "",
                            following = response.followingCount ?: "",
                            followers = response.followersCount ?: "",
                            joyerStatus = response.joyerStatus ?: "",
//                            birthday = response.b ?: "",
                            gender = response.gender?: "",
                            relationship = response.relationship?.name?: "",
//                            children = response.ch?.name?: "",
                            politicalIdeology = response.politicalIdeology?.name ?: "",
                            titleName = response.title?.name ?: "",
                            subTitleName = response.subTitle?.name ?: "",
                            areaOfInterest = response.interests,
                            languages = response.languages,
                            joySince = response.joySince ?: "",
                            joySinceDuration = response.joySinceDuration ?: "",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality?.name ?: "",
                            ethnicity = response.ethnicity?.name ?: "",
                            faith = response.faith?.name ?: "",
                            educationName = response.education?.name ?: "",
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

    private fun loadTitles(){
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result =
                fetchTitlesUseCase()

            result.fold(
                onSuccess = { titles ->
                    _uiState.update { old ->
                        old.copy(
                            isLoading = false,
                            titles = titles,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

}