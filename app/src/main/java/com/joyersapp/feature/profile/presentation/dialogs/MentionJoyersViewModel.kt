package com.joyersapp.feature.profile.presentation.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MentionJoyersUiState(

    val searchQuery: String = "",
    val selectedUsersCount: String = "",
    val isAddMentionsMode: Boolean = false,
    val isClearMentionsMode: Boolean = false,
    val userList: List<EditMagneticsUserListData> = emptyList(),
    val filteredUserList: List<EditMagneticsUserListData> = emptyList(),
    val filteredSelectedUserList: List<EditMagneticsUserListData> = emptyList(),
) {
    val isApplyEnabled: Boolean
        get() = userList.any { it.isSelected }
    val isAddMentionsEnabled: Boolean
        get() = userList.any { it.isSelected } && !isAddMentionsMode
}

sealed class MentionJoyersEvent {

    data class OnUserSelectionToggled(val user: EditMagneticsUserListData) : MentionJoyersEvent()
    data class InitUserList(val userList: List<EditMagneticsUserListData>) : MentionJoyersEvent()
    data object OnApply : MentionJoyersEvent()
    data object OnBackPressed : MentionJoyersEvent()
    data class OnSearchQueryChanged(val query: String) : MentionJoyersEvent()
    data object OnAddMentionsClicked : MentionJoyersEvent()
    data object OnSelectionsCleared : MentionJoyersEvent()
    data class OnToggleClearMentionsMode(val value: Boolean) : MentionJoyersEvent()

}

sealed class MentionJoyersNavEvent {
    class OnApply(val selectedUsers: List<EditMagneticsUserListData>) : MentionJoyersNavEvent()
    object OnDismiss : MentionJoyersNavEvent()

}

@HiltViewModel
class MentionJoyersViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(MentionJoyersUiState() )
    private val _navigationEvents = MutableSharedFlow<MentionJoyersNavEvent>()

    val uiState: StateFlow<MentionJoyersUiState> = _uiState.asStateFlow()
    val navigationEvents = _navigationEvents

    override fun onCleared() {
        super.onCleared()
        _uiState.update { MentionJoyersUiState() }
    }

    fun onEvent(event: MentionJoyersEvent) {
        when (event) {
            is MentionJoyersEvent.InitUserList -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(
                        isAddMentionsMode = false,
                        userList = event.userList,
                        filteredUserList = event.userList,
                    ) }
                }
            }
            is MentionJoyersEvent.OnUserSelectionToggled -> {
                _uiState.update { state ->
                    var selectedUsersCount = ""
                    var isAddMentionsMode = state.isAddMentionsMode

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

                    if (isAddMentionsMode) {
                        selectedUsersCount = updatedFullList.filter { it.isSelected }.size.toString()
                        if (selectedUsersCount.toInt() == 0) {
                            selectedUsersCount = ""
                            isAddMentionsMode = false
                        }
                    }

                    state.copy(
                        selectedUsersCount = selectedUsersCount,
                        userList = updatedFullList,
                        filteredUserList = updatedFiltered,
                        filteredSelectedUserList = updatedFullList.filter { it.isSelected },
                        isAddMentionsMode = isAddMentionsMode,
                    )
                }
            }

            is MentionJoyersEvent.OnBackPressed -> {
                viewModelScope.launch {
                    if (uiState.value.isAddMentionsMode) {
                        _uiState.update { state ->
                            state.copy(
                                isAddMentionsMode = false,
                                isClearMentionsMode = false,
                                selectedUsersCount = ""
                            )
                        }
                    } else {
                        _navigationEvents.emit(MentionJoyersNavEvent.OnDismiss)
                        onCleared()
                    }
                }
            }

            is MentionJoyersEvent.OnApply -> {
                viewModelScope.launch {
                    val selectedUsers = _uiState.value.userList.filter { it.isSelected }
                    _navigationEvents.emit(MentionJoyersNavEvent.OnApply(selectedUsers))
                }
            }

            is MentionJoyersEvent.OnSelectionsCleared -> {
                viewModelScope.launch {
                    _uiState.update { state ->
                        state.copy(
                            searchQuery = "",
                            userList = state.userList.map { it.copy(isSelected = false) },
                            filteredUserList = state.userList.map { it.copy(isSelected = false) },
                            filteredSelectedUserList = emptyList(),
                            selectedUsersCount = "",
                            isClearMentionsMode = false,
                            isAddMentionsMode = false,
                        )
                    }
                }
            }

            is MentionJoyersEvent.OnToggleClearMentionsMode -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isClearMentionsMode = event.value,
                        )
                    }
                }
            }

            is MentionJoyersEvent.OnAddMentionsClicked -> {
                viewModelScope.launch {
                    _uiState.update {
                            val selectedUsersCount = it.userList.filter { it.isSelected }.size.toString()

                        it.copy(
                        selectedUsersCount = selectedUsersCount,
                        filteredSelectedUserList = it.filteredUserList.filter { it.isSelected },
                        isAddMentionsMode = true,
                    ) }
                }
            }

            is MentionJoyersEvent.OnSearchQueryChanged -> {
                viewModelScope.launch(Dispatchers.Default) {
                    _uiState.update { state ->
                        if (state.isAddMentionsMode) {
                            val filtered = if (event.query.isBlank()) {
                                state.userList.filter { it.isSelected }
                            } else {
                                state.userList.filter {
                                    it.isSelected && it.getDisplayName().contains(
                                        event.query,
                                        ignoreCase = true
                                    )
                                }
                            }
                            state.copy(
                                searchQuery = event.query,
                                filteredSelectedUserList = filtered
                            )
                        } else {
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
}