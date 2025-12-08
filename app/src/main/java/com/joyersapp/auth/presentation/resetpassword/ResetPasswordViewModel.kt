package com.joyersapp.auth.presentation.resetpassword

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.ResetPasswordRequestDto
import com.joyersapp.auth.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ResetPasswordViewModel @Inject constructor (
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState
    private var job: Job? = null

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.LoadingChanged -> {
                _uiState.update { it.copy(isLoading = event.value) }
            }
            is ResetPasswordEvent.IsPasswordResetSuccessChanged -> {
                _uiState.update { it.copy(isPasswordResetSuccess = event.value) }
            }
            is ResetPasswordEvent.IsPhoneModeChanged -> {
                _uiState.update { it.copy(isPhoneMode = event.value) }
            }
            is ResetPasswordEvent.IsPasswordFocusedChanged -> {
                _uiState.update { it.copy(isPasswordFocused = event.value) }
            }
            is ResetPasswordEvent.IsConfirmPasswordFocusedChanged -> {
                _uiState.update { it.copy(isConfirmPasswordFocused = event.value) }
            }
            is ResetPasswordEvent.IdentifierValueChanged -> {
                _uiState.update { it.copy(identifierValue = event.value) }
            }
            is ResetPasswordEvent.SelectedCountryCodeChanged -> {
                _uiState.update { it.copy(selectedCountryCode = event.value) }
            }
            is ResetPasswordEvent.SelectedCountryNameCodeChanged -> {
                _uiState.update { it.copy(selectedCountryNameCode = event.value) }
            }
            is ResetPasswordEvent.PasswordErrorChanged -> {
                _uiState.update { it.copy(passwordError = event.value) }
            }
            is ResetPasswordEvent.ConfirmPasswordErrorChanged -> {
                _uiState.update { it.copy(confirmPasswordError = event.value) }
            }
            is ResetPasswordEvent.IdentifierErrorChanged -> {
                _uiState.update { it.copy(identifierError = event.value) }
            }
            is ResetPasswordEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value) }
            }
            is ResetPasswordEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.value) }
            }
            is ResetPasswordEvent.VerificationCodeChanged -> {
                _uiState.update { it.copy(verificationCode = event.value) }
            }
            is ResetPasswordEvent.PasswordResetSuccessMessageChanged -> {
                _uiState.update { it.copy(passwordResetSuccessMessage = event.value) }
            }
            is ResetPasswordEvent.PasswordResetErrorMessageChanged -> {
                _uiState.update { it.copy(passwordResetErrorMessage = event.value) }
            }
            is ResetPasswordEvent.OnVerifyButtonClicked -> {
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        val state = _uiState.value
        val params = ResetPasswordRequestDto(
            username = "",
            otp_code = state.verificationCode,
            new_password = state.password,
            confirm_password = state.confirmPassword
        )
        if (state.isPhoneMode) {
            //params.country_code = state.selectedCountryCode
            //params.mobile = state.identifierValue
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(state.identifierValue).matches()) {
                //params.email = state.identifierValue
            } else {
                params.username = state.identifierValue
            }
        }
        if (params.username.isBlank()) return
        onEvent(ResetPasswordEvent.LoadingChanged(true))
        job?.cancel()
        job = viewModelScope.launch {
            val result = resetPasswordUseCase(params)
            Log.e("reset password api", "result is: $result")
            result.fold(
                onSuccess = { response ->
                    // Access your message here
                    Log.e("reset msg", response.message)

                    if (response.statusCode == 200) {
                        //val (mainText, secondaryText) = parseForgotPasswordMessage(response.message)
                        _uiState.update { it.copy(
                            isLoading = false,
                            isPasswordResetSuccess = true,
                            passwordResetSuccessMessage = response.message
                        ) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, passwordResetErrorMessage = response.message) }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, passwordResetErrorMessage = error.message ?: "Something went wrong") }
                }
            )
        }
    }
}