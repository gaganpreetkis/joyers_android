package com.joyersapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.usecase.ObserveAuthStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthState: ObserveAuthStateUseCase
) : ViewModel() {

    val authState: StateFlow<AuthState> =
        observeAuthState()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AuthState.Unknown
            )
}