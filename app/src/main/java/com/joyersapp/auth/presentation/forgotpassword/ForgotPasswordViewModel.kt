package com.joyersapp.auth.presentation.forgotpassword

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.domain.usecase.ForgotPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState
    private var job: Job? = null

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.LoadingChanged -> {
                _uiState.update { it.copy(isLoading = event.value) }
            }

            is ForgotPasswordEvent.IsPhoneModeChanged -> {
                _uiState.update { it.copy(isPhoneMode = event.value) }
            }

            is ForgotPasswordEvent.UsernameEmailChanged -> {
                _uiState.update { it.copy(usernameEmail = event.value) }
            }

            is ForgotPasswordEvent.PhoneChanged -> {
                _uiState.update { it.copy(phone = event.value) }
            }

            is ForgotPasswordEvent.VerificationCodeChanged -> {
                _uiState.update { it.copy(verificationCode = event.value) }
            }

            is ForgotPasswordEvent.UsernameEmailErrorChanged -> {
                _uiState.update { it.copy(usernameEmailError = event.value) }
            }

            is ForgotPasswordEvent.PhoneErrorChanged -> {
                _uiState.update { it.copy(phoneError = event.value) }
            }

            is ForgotPasswordEvent.VerificationCodeErrorChanged -> {
                _uiState.update { it.copy(verificationCodeError = event.value) }
            }

            is ForgotPasswordEvent.TabErrorChanged -> {
                _uiState.update { it.copy(tabError = event.value) }
            }

            is ForgotPasswordEvent.SubmitClicked -> forgotPassword(event.value)
        }
    }

    private fun forgotPassword(value: String) {
        //val state = _uiState.value
        if (value.isBlank()) return
        onEvent(ForgotPasswordEvent.LoadingChanged(true))
        job?.cancel()
        job = viewModelScope.launch {
            val result = forgotPasswordUseCase(value)
            Log.e("forgot password api", "result is: $result")
        }
        onEvent(ForgotPasswordEvent.LoadingChanged(false))
    }
}