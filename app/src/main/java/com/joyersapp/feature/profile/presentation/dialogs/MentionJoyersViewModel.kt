package com.joyersapp.feature.profile.presentation.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.feature.profile.domain.usecase.GetTitlesUseCase
import com.joyersapp.core.SessionManager
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Language
import com.joyersapp.feature.profile.data.remote.dto.LanguageReq
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto
import com.joyersapp.feature.profile.domain.usecase.GetCountryListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetEducationListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetEthnicityListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetFaithReligionListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetInterstsListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetLanguageListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetPoliticalIdeoogyListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetRelationshipListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetSubTitlesUseCase
import com.joyersapp.feature.profile.domain.usecase.GetEditMagneticsUserListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetUserProfileUseCase
import com.joyersapp.feature.profile.domain.usecase.UploadPictureServerUseCase
import com.joyersapp.feature.profile.domain.usecase.UploadUserProfileUseCase
import com.joyersapp.feature.profile.presentation.EditMagneticsUiState
import com.joyersapp.feature.profile.presentation.MagneticsData
import com.joyersapp.feature.profile.presentation.ProfileHeaderData
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileNavigationEvent
import com.joyersapp.feature.profile.presentation.UserProfileUiState
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.utils.graphemeCount
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MentionJoyersUiState(

    val searchQuery: String = "",
    val userList: List<EditMagneticsUserListData> = emptyList(),
    val filteredUserList: List<EditMagneticsUserListData> = emptyList(),
    val selectedUserList: List<EditMagneticsUserListData> = emptyList(),
) {
    val isApplyEnabled: Boolean
        get() = userList.any { it.isSelected }
}

sealed class MentionJoyersEvent {

    data class OnUserSelectionToggled(val user: EditMagneticsUserListData) : MentionJoyersEvent()
    data class InitUserList(val userList: List<EditMagneticsUserListData>) : MentionJoyersEvent()
    data object OnApply : MentionJoyersEvent()
    data class OnSearchQueryChanged(val query: String) : MentionJoyersEvent()

}

sealed class MentionJoyersNavEvent {
    class OnApply(val selectedUsers: List<EditMagneticsUserListData>) : MentionJoyersNavEvent()

}

@HiltViewModel
class MentionJoyersViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(MentionJoyersUiState() )
    private val _navigationEvents = MutableSharedFlow<MentionJoyersNavEvent>()

    val uiState: StateFlow<MentionJoyersUiState> = _uiState.asStateFlow()
    val navigationEvents = _navigationEvents


    fun onEvent(event: MentionJoyersEvent) {
        when (event) {
            is MentionJoyersEvent.InitUserList -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(
                        userList = event.userList,
                        filteredUserList = event.userList,
                    ) }
                }
            }
            is MentionJoyersEvent.OnUserSelectionToggled -> {
                _uiState.update { state ->

                    // Update selection in FULL LIST
                    val updatedFullList = state.userList.map {
                        if (it.id == event.user.id) it.copy(isSelected = !it.isSelected) else it
                    }

                    // Re-filter based on search query
                    val updatedFiltered = if (state.searchQuery.isBlank()) {
                        updatedFullList
                    } else {
                        updatedFullList.filter {
                            it.getDisplayName().contains(state.searchQuery, ignoreCase = true)
                        }
                    }

                    state.copy(
                        userList = updatedFullList,
                        filteredUserList = updatedFiltered
                    )
                }
            }

            is MentionJoyersEvent.OnApply -> {
                viewModelScope.launch {
                    val selectedUsers = _uiState.value.userList.filter { it.isSelected }
                    _navigationEvents.emit(MentionJoyersNavEvent.OnApply(selectedUsers))
                }
            }

            is MentionJoyersEvent.OnSearchQueryChanged -> {
                viewModelScope.launch(Dispatchers.Default) {
                    _uiState.update { state ->
                        val filtered = if (event.query.isBlank()) {
                            state.userList
                        } else {
                            state.userList.filter {
                                it.getDisplayName().contains(
                                    event.query,
                                    ignoreCase = true
                                )
                            }
                        }
                        state.copy(
                            searchQuery = event.query,
                            filteredUserList = filtered
                        )

                    }
                }
            }
        }
    }
}