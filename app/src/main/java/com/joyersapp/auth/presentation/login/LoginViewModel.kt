package com.joyersapp.auth.presentation.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.ForgotPasswordVerifyOtpRequestDto
import com.joyersapp.auth.data.remote.dto.LoginRequestDto
import com.joyersapp.auth.domain.usecase.LoginUseCase
import com.joyersapp.auth.presentation.forgotpassword.ForgotPasswordEvent
import com.joyersapp.utils.isValidPassword
import com.joyersapp.utils.isValidUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState
    private var job: Job? = null

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.LoadingChanged -> {
                _uiState.update { it.copy(isLoading = event.value) }
            }
            is LoginEvent.UsernameChanged -> {
                val state = _uiState.value
                _uiState.update {
                    it.copy(
                        username = event.value,
                        apiErrorMessage = "",
                        apiFailedErrorMessage = "",
                        apiOnlyUsernameErrorMessage = "",
                    )
                }
                if (state.isFormValid && isValidPassword(state.password) && state.rememberMe) {
                    onEvent(LoginEvent.RememberMeChanged(false))
                }
            }
            is LoginEvent.PasswordChanged -> {
                val state = _uiState.value
                _uiState.update {
                    it.copy(
                        password = event.value,
                        apiErrorMessage = "",
                        apiFailedErrorMessage = "",
                        apiOnlyUsernameErrorMessage = "",
                    )
                }
                if (state.isFormValid && isValidPassword(state.password) && state.rememberMe) {
                    onEvent(LoginEvent.RememberMeChanged(false))
                }
            }
            is LoginEvent.ApiOnlyUsernameErrorMessageChanged -> {
                _uiState.update { it.copy(apiOnlyUsernameErrorMessage = event.value) }
            }
            is LoginEvent.ApiErrorMessageChanged -> {
                _uiState.update { it.copy(apiErrorMessage = event.value) }
            }
            is LoginEvent.ApiFailedErrorMessageChanged -> {
                _uiState.update { it.copy(apiFailedErrorMessage = event.value) }
            }
            is LoginEvent.PasswordVisibleChanged -> {
                _uiState.update { it.copy(passwordVisible = event.value) }
            }
            is LoginEvent.EmailPhoneErrorChanged -> {
                _uiState.update { it.copy(emailPhoneError = event.value) }
            }
            is LoginEvent.PasswordErrorChanged -> {
                _uiState.update { it.copy(passwordError = event.value) }
            }
            is LoginEvent.IsPhoneModeChanged -> {
                _uiState.update {
                    it.copy(
                        isPhoneMode = event.value,
                        username = "",
                        rememberMe = false,
                        password = "",
                        apiErrorMessage = "",
                        apiFailedErrorMessage = "",
                        apiOnlyUsernameErrorMessage = "",
                    )
                }
            }
            is LoginEvent.ShowSocialDialogChanged -> {
                _uiState.update { it.copy(showSocialDialog = event.value) }
            }
            is LoginEvent.RememberMeChanged -> {
                _uiState.update { it.copy(rememberMe = event.value) }
            }
            is LoginEvent.IsLoginApiSuccessChanged -> {
                _uiState.update { it.copy(isLoginApiSuccess = event.value) }
            }
            is LoginEvent.SelectedCountryCodeChanged -> {
                _uiState.update { it.copy(selectedCountryCode = event.value) }
            }
            is LoginEvent.SelectedCountryNameCodeChanged -> {
                _uiState.update { it.copy(selectedCountryNameCode = event.value) }
            }
            is LoginEvent.OnLoginButtonClicked -> {
                login()
            }
        }
    }


    private fun login() {
        val state = _uiState.value
        val params = LoginRequestDto(
            country_code = "",
            mobile = "",
            email = "",
            username = "",
            password = state.password
        )
        if (state.isPhoneMode) {
            params.country_code = state.selectedCountryCode
            params.mobile = state.username
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(state.username).matches()) {
                params.email = state.username
            } else {
                if (state.username.startsWith("@")) {
                    params.username = state.username.removePrefix("@")
                } else {
                    params.username = state.username
                }
            }
        }
        if (params.username.isBlank() && params.email.isBlank() && params.country_code.isBlank() && params.mobile.isBlank()) return
        onEvent(LoginEvent.LoadingChanged(true))
        job?.cancel()
        job = viewModelScope.launch {
            val result = loginUseCase(params)
            Log.e("login api", "result is: $result")
            result.fold(
                onSuccess = { response ->
                    // Access your message here
                    Log.e("login msg", response.message)

                    if (response.statusCode == 200) {
                        _uiState.update { it.copy(isLoading = false, apiErrorMessage = "", apiFailedErrorMessage = "", apiOnlyUsernameErrorMessage = "", /*isLoginApiSuccess = true*//*, isVerificationSuccess = true*/) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, apiErrorMessage = response.message/*, verificationCodeError = response.message*/) }
                    }
                },
                onFailure = { error ->
                    if (error.message != null) {
                        if (error.message!!.contains("password", true)) {
                            _uiState.update { it.copy(isLoading = false, apiErrorMessage = error.message ?: "Something went wrong"/*, verificationCodeError = error.message ?: "Something went wrong"*/) }
                        } else {
                            _uiState.update { it.copy(isLoading = false, apiOnlyUsernameErrorMessage = error.message ?: "Something went wrong"/*, verificationCodeError = error.message ?: "Something went wrong"*/) }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, apiFailedErrorMessage = "Login failed. Please try again."/*, verificationCodeError = error.message ?: "Something went wrong"*/) }
                    }

                }
            )
        }
    }
}