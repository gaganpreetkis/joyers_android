package com.joyersapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {
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
            // replace with repository calls
            _uiState.update {
                it.copy(
                    bannerUrl = null,
                    avatarUrl = null,
                    // optionally update fields from real data
                )
            }
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {

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

    fun onEditProfile() { /* navigate to edit */ }
    fun onMessage() { /* open chat */ }
    fun onNotify() { /* notifications */ }
    fun onBookmark() { /* bookmark */ }
}