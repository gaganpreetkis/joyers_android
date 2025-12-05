package com.joyersapp.auth.presentation.forgotpassword

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.ForgotPasswordRequestDto
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpRequestDto
import com.joyersapp.auth.domain.usecase.ForgotPasswordUseCase
import com.joyersapp.auth.domain.usecase.ForgotPasswordVerifyOtpUseCase
import com.joyersapp.utils.UiText
import com.joyersapp.utils.parseForgotPasswordMessage
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
    private val forgotPasswordVerifyOtpUseCase: ForgotPasswordVerifyOtpUseCase,
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

            is ForgotPasswordEvent.ShowVerificationCodeChanged -> {
                _uiState.update { it.copy(showVerificationCode = event.value) }
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

            is ForgotPasswordEvent.SelectedCountryCodeChanged -> {
                _uiState.update { it.copy(selectedCountryCode = event.value) }
            }

            is ForgotPasswordEvent.OnNextButtonClicked -> forgotPassword()

            is ForgotPasswordEvent.OnVerifyButtonClicked -> verifyOtp()
        }
    }

    private fun forgotPassword() {
        val state = _uiState.value
        val params = ForgotPasswordRequestDto(
            country_code = "",
            mobile = "",
            username = "",
            email = ""
        )
        if (state.isPhoneMode) {
            params.country_code = state.selectedCountryCode
            params.mobile = state.phone
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(state.usernameEmail).matches()) {
                params.email = state.usernameEmail
            } else {
                params.username = state.usernameEmail
            }
        }
        if (params.username.isBlank() && params.email.isBlank() && params.country_code.isBlank() && params.mobile.isBlank()) return
        onEvent(ForgotPasswordEvent.LoadingChanged(true))
        job?.cancel()
        job = viewModelScope.launch {
            val result = forgotPasswordUseCase(params)
            Log.e("forgot password api", "result is: $result")
            result.fold(
                onSuccess = { response ->
                    // Access your message here
                    Log.e("forgot msg", response.message)

                    if (response.statusCode == 200) {
                        val (mainText, secondaryText) = parseForgotPasswordMessage(response.message)
                        _uiState.update { it.copy(isLoading = false, showVerificationCode = true, mainText = mainText, secondaryText = secondaryText) }
                    } else {
                        if (uiState.value.isPhoneMode) {
                            _uiState.update { it.copy(isLoading = false, phoneError = response.message) }
                        } else {
                            _uiState.update { it.copy(isLoading = false, usernameEmailError = response.message) }
                        }
                    }
                },
                onFailure = { error ->
                    if (uiState.value.isPhoneMode) {
                        _uiState.update { it.copy(isLoading = false, phoneError = error.message ?: "Something went wrong") }
                    } else {
                        _uiState.update { it.copy(isLoading = false, usernameEmailError = error.message ?: "Something went wrong") }
                    }
                }
            )
        }
    }

    private fun verifyOtp() {
        val state = _uiState.value
        val params = ForgotPasswordVerifyOtpRequestDto(
            purpose = "password_reset",
            username = "",
            otp_code = state.verificationCode
        )
        if (state.isPhoneMode) {
            //params.country_code = state.selectedCountryCode
            //params.mobile = state.phone
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(state.usernameEmail).matches()) {
                //params.email = state.usernameEmail
            } else {
                params.username = state.usernameEmail
            }
        }
        if (params.username.isBlank() /*&& params.email.isBlank() && params.country_code.isBlank() && params.mobile.isBlank()*/) return
        onEvent(ForgotPasswordEvent.LoadingChanged(true))
        job?.cancel()
        job = viewModelScope.launch {
            val result = forgotPasswordVerifyOtpUseCase(params)
            Log.e("forgot password verify otp api", "result is: $result")
            result.fold(
                onSuccess = { response ->
                    // Access your message here
                    Log.e("forgot msg", response.message)

                    if (response.statusCode == 200) {
                        //val (mainText, secondaryText) = parseForgotPasswordMessage(response.message)
                        //_uiState.update { it.copy(isLoading = false, showVerificationCode = true, mainText = mainText, secondaryText = secondaryText) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, verificationCodeError = response.message) }
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, verificationCodeError = error.message ?: "Something went wrong") }
                }
            )
        }
    }
}