package com.joyersapp.feature.post.presentation.create_joy

import androidx.lifecycle.ViewModel
import com.joyersapp.core.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

data class CreateJoyUiState(

    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,

)

sealed class CreateJoyNavigationEvent {


}

sealed class CreateJoyEvent {

}

@HiltViewModel
class CreateJoyViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateJoyUiState())
    val uiState: StateFlow<CreateJoyUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<CreateJoyNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()


    fun onEvent(event: CreateJoyEvent) {
//        when (event) {
//
//        }
    }

}