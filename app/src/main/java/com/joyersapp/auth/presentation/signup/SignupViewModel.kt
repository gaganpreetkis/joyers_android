package com.joyersapp.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.domain.usecase.CheckUsernameUseCase
import com.joyersapp.auth.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    private var usernameJob: Job? = null

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.NameChanged -> _uiState.update { it.copy(name = event.value) }

            is SignupEvent.EmailChanged -> _uiState.update { it.copy(email = event.value) }

            is SignupEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.value, isUsernameAvailable = null) }
                checkUsernameDebounced(event.value)
            }

            is SignupEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value) }

            is SignupEvent.ConfirmPasswordChanged ->
                _uiState.update { it.copy(confirmPassword = event.value) }

            SignupEvent.SubmitClicked -> signup()
        }
    }

    private fun checkUsernameDebounced(username: String) {
        if (username.isBlank()) return

        usernameJob?.cancel()
        usernameJob = viewModelScope.launch {
            delay(600) // debounce
            _uiState.update { it.copy(checkingUsername = true) }

            val result = checkUsernameUseCase(username)

            _uiState.update {
                it.copy(
                    checkingUsername = false,
                    isUsernameAvailable = result.getOrNull()
                )
            }
        }
    }

    private fun signup() {
        val state = _uiState.value

        if (state.isUsernameAvailable != true) {
            _uiState.update { it.copy(error = "Username not available") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = signupUseCase(
                state.username,
                state.email,
                state.password
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }
/*
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    private val _oneShotEvents = MutableSharedFlow<String>() // snackbars, toasts
    val oneShotEvents: SharedFlow<String> = _oneShotEvents

    fun onEvent(event: SignupEvent) {
        when (event) {
            is SignupEvent.NameChanged ->
                _uiState.update { it.copy(name = event.value, error = null) }

            is SignupEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.value, error = null) }

            is SignupEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value, error = null) }

            is SignupEvent.ConfirmPasswordChanged ->
                _uiState.update { it.copy(confirmPassword = event.value, error = null) }

            SignupEvent.SubmitClicked ->
                signup()
        }
    }

    private fun signup() {
        val current = _uiState.value

        if (current.name.isBlank() ||
            current.email.isBlank() ||
            current.password.isBlank() ||
            current.confirmPassword.isBlank()
        ) {
            _uiState.update { it.copy(error = "All fields are required") }
            return
        }

        if (current.password != current.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = signupUseCase(
                name = current.name,
                email = current.email,
                password = current.password
            )

            if (result.isSuccess) {
                _uiState.update { it.copy(isLoading = false) }
                // Root observes AuthState and will move to main graph automatically
                _oneShotEvents.emit("Account created successfully")
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Signup failed"
                    )
                }
            }
        }
    }*/
}