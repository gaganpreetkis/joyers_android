package com.joyersapp.feature.post.presentation.create_joy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.core.SessionManager
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.domain.usecase.GetEditMagneticsUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateJoyUiState(

    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,
    val editMagneticsUserList: List<EditMagneticsUserListData> = emptyList(),
    val errorMessage: String? = null,

    )

sealed class CreateJoyNavigationEvent {


}

sealed class CreateJoyEvent {

    object LoadData : CreateJoyEvent()
    data class OnApplyMentionedJoyers(val list: List<EditMagneticsUserListData>) : CreateJoyEvent()


}

@HiltViewModel
class CreateJoyViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getEditMagneticsUserListUseCase: GetEditMagneticsUserListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateJoyUiState())
    val uiState: StateFlow<CreateJoyUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<CreateJoyNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()


    fun onEvent(event: CreateJoyEvent) {
        when (event) {
            is CreateJoyEvent.LoadData -> {
                if (!uiState.value.isDataLoaded) {
                    // simulate fetch
                    viewModelScope.launch {

                        // show loader
                        _uiState.update { it.copy(isLoading = true) }

                        awaitAll(
                            async { getEditMagneticsUserListData() },
                        )

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isDataLoaded = true
                            )
                        }
                    }
                }
            }

            is CreateJoyEvent.OnApplyMentionedJoyers -> TODO()
        }
    }


    private suspend fun getEditMagneticsUserListData() {

        val result = getEditMagneticsUserListUseCase()

        result.fold(
            onSuccess = { list ->
                _uiState.update { old ->
                    old.copy(
                        editMagneticsUserList = list,
                        errorMessage = null
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        errorMessage = error.message
                    )
                }
            }
        )
    }

}