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
            is SignupEvent.NameChanged ->
                _uiState.update { it.copy(name = event.value, error = null) }

            is SignupEvent.EmailChanged ->
                _uiState.update { it.copy(email = event.value, error = null) }

            is SignupEvent.UsernameChanged -> {
                _uiState.update {
                    it.copy(
                        username = event.value,
                        isUsernameAvailable = null,
                        error = null
                    )
                }
                val cleanUsername = event.value.text.removePrefix("@") // remove @ before sending request
                if (cleanUsername.length >= 3) {
                    checkUsernameDebounced(cleanUsername)
                }
            }

            is SignupEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value, error = null) }

            is SignupEvent.ConfirmPasswordChanged ->
                _uiState.update { it.copy(confirmPassword = event.value, error = null) }

            SignupEvent.SubmitClicked -> signup()
        }
    }

    private fun checkUsernameDebounced(username: String) {
        if (username.isBlank()) return

        usernameJob?.cancel()
        usernameJob = viewModelScope.launch {
            delay(600)
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
        val usernameText = state.username.text  // 👈 important

        if (state.name.isBlank() ||
            state.email.isBlank() ||
            usernameText.isBlank() ||
            state.password.isBlank() ||
            state.confirmPassword.isBlank()
        ) {
            _uiState.update { it.copy(error = "All fields are required") }
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        if (state.isUsernameAvailable == false) {
            _uiState.update { it.copy(error = "Username not available") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = signupUseCase(
//                name = state.name,
                email = state.email,
                password = state.password,
                username = usernameText
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
