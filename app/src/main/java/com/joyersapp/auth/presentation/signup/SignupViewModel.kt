package com.joyersapp.auth.presentation.signup

import android.util.Patterns
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.domain.usecase.CheckUsernameUseCase
import com.joyersapp.auth.domain.usecase.RegisterUseCase
import com.joyersapp.auth.domain.usecase.VerifyOtpUseCase
import com.joyersapp.utils.ApiErrorException
import com.joyersapp.utils.UiText
import com.joyersapp.utils.UiText.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    private var usernameJob: Job? = null

    fun onEvent(event: SignupEvent) {
        when (event) {

            is SignupEvent.NameChanged ->
                _uiState.update { it.copy(name = event.value, error = null) }

            is SignupEvent.EmailChanged -> {
                val isValidEmail = event.value.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                    event.value
                ).matches()

                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailPhoneError = null,
                        showVerification = false,
                        showPasswordFields = false,
                        isValidEmail = isValidEmail
                    )
                }
            }

            is SignupEvent.PhoneChanged -> {
                val isValidPhone = (
                        event.value.all { it.isDigit() }
                        && event.value.length in 10..15)

                _uiState.update {
                    it.copy(
                        phone = event.value,
                        emailPhoneError = null,
                        showVerification = false,
                        showPasswordFields = false,
                        isValidPhone = isValidPhone
                    )
                }
            }

            is SignupEvent.TogglePhoneMode -> {
                _uiState.update {
                    it.copy(
                        isPhoneMode = !uiState.value.isPhoneMode,
                        emailPhoneError = null,
                        email = "",
                        phone = "",
                        showVerification = false,
                        showPasswordFields = false
                    )
                }
            }

            is SignupEvent.VerificationCodeChanged -> {
                if (event.value.length <= 6 && event.value.all { char -> char.isDigit() }) {
                    _uiState.update {
                        it.copy(
                            verificationCode = event.value,
                            verificationError = null,
                        )
                    }
                }
            }

            is SignupEvent.SendVerificationCode -> {
                register()
            }

            is SignupEvent.VerifyCode -> {
                verifyOtpRequest()
            }

            is SignupEvent.UsernameChanged -> {
                usernameJob?.cancel()
                _uiState.update {
                    it.copy(
                        username = event.value,
                        isUsernameAvailable = null,
                        showUsernameError = false,
                        usernameError = null,
                        isSuggestionSelected = false,
                        usernameSuggestions = emptyList(),
                    )
                }
                val cleanUsername = event.value.text.removePrefix("@") // remove @ before sending request
                if (cleanUsername.length >= 3 && !uiState.value.isSuggestionSelected) {
                    checkUsernameDebounced(cleanUsername)
                }
            }

            is SignupEvent.UsernameSuggestionClicked -> {
                _uiState.update {
                    it.copy(
                        username = TextFieldValue(
                            text = "@${event.suggestion}",
                            selection = TextRange(
                                start = event.suggestion.length + 1,
                                end = event.suggestion.length + 1
                            )
                        ),
                        isSuggestionSelected = true,
                        usernameSuggestions = emptyList(),
                        isValidUsername = true,
                        showUsernameError = false,
                        usernameError = null,
                    )
                }
            }

            is SignupEvent.PasswordChanged ->
                _uiState.update { it.copy(password = event.value, error = null) }

            is SignupEvent.ConfirmPasswordChanged ->
                _uiState.update { it.copy(confirmPassword = event.value, error = null) }

            is SignupEvent.UsernameFocusChanged -> {
                val prevFocused = _uiState.value.isUsernameFocused

                _uiState.update {
                    it.copy(isUsernameFocused = event.isFocused, error = null) }

                if (prevFocused && !event.isFocused&& uiState.value.username.text.length < 4) {
                    _uiState.update {
                        it.copy(
                            showUsernameError = true,
                            usernameError = StringResource(R.string.username_must_be_3_15_characters_only_letters_numbers_and_underscores),
                        )
                    }
                }
            }

            SignupEvent.SubmitClicked -> TODO()

            SignupEvent.CheckUsername -> TODO()
            SignupEvent.ClearSignupError -> TODO()
            is SignupEvent.ConfirmPasswordFocusChanged -> TODO()
            is SignupEvent.CountryCodeChanged -> {

            }
            is SignupEvent.PasswordFocusChanged -> TODO()
            is SignupEvent.SignInButtonTextChanged -> TODO()
            SignupEvent.SubmitSignup -> TODO()
            SignupEvent.ToggleConfirmPasswordVisibility -> TODO()
            SignupEvent.TogglePasswordFieldsVisibility -> TODO()
            SignupEvent.TogglePasswordVisibility -> TODO()
            is SignupEvent.UsernameCheckResult -> TODO()

        }
    }

    private fun verifyOtpRequest() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = if (state.isPhoneMode)
            //            verify with phone number
                verifyOtpUseCase(
                VerifyOtpRequestDto.WithPhone(
                    mobile = state.phone,
                    username = state.username.text.removePrefix("@"),
                    country_code = state.selectedCountryCode,
                    otp_code = state.verificationCode
                )
            ) else
            //            verify with email
            verifyOtpUseCase(
                VerifyOtpRequestDto.WithEmail(
                    email = state.email,
                    username = state.username.text.removePrefix("@"),
                    otp_code = state.verificationCode
                )
            )

            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showVerification = false,
                            showPasswordFields = true,
                            codeSentMessage = DynamicString(response.message)
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationError = error.message,
                        )
                    }
                }
            )
        }
    }

    private fun checkUsernameDebounced(username: String) {
        if (username.isBlank()) return

        usernameJob = viewModelScope.launch {
            delay(600)
            _uiState.update { it.copy(showUsernameLoader = true) }

            val result = checkUsernameUseCase(username)
            result.fold(
                onSuccess = { response ->
                    val updatedUsername = "@${response.username}" // apply API-corrected username with @ prefix
                    _uiState.update {
                        it.copy(
                            username = TextFieldValue(
                                text = updatedUsername,
                                selection = TextRange(updatedUsername.length)
                            ),
                            showUsernameLoader = false,
                            isValidUsername = true,
                            showUsernameError = false,
                            usernameError = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            usernameSuggestions =  if (error is ApiErrorException) error.errorBody?.suggestions!! else emptyList(),
                            showUsernameLoader = false,
                            isValidUsername = false,
                            showUsernameError = true,
                            usernameError = DynamicString(error.message ?: "Something went wrong")
                        )
                    }
                }
            )
        }
    }

    private fun register() {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = if (uiState.value.isPhoneMode)
            //            register with phone number
                registerUseCase(
                RegisterRequestDto.WithPhone(
                    mobile = state.email,
                    username = state.username.text.removePrefix("@"),
                    countryCode = state.selectedCountryCode
                )
            ) else
                registerUseCase(
                RegisterRequestDto.WithEmail(
                    email = state.email,
                    username = state.username.text.removePrefix("@")
                )
            )

            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showVerification = true,
                            verificationError = null,
                            codeSentMessage = DynamicString(response.message),
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            showVerification = false,
                            emailPhoneError = error.message,
                        )
                    }
                }
            )
        }
    }
}
