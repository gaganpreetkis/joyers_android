package com.joyersapp.feature.post.presentation.create_post

import androidx.lifecycle.ViewModel
import com.joyersapp.core.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

data class CreatePostUiState(

    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,

)

sealed class CreatePostNavigationEvent {


}

sealed class CreatePostEvent {

}

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<CreatePostNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()


    fun onEvent(event: CreatePostEvent) {
//        when (event) {
//
//        }
    }

}