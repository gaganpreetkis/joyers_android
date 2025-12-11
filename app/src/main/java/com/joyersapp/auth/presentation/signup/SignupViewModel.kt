package com.joyersapp.auth.presentation.signup

import android.util.Patterns
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.domain.usecase.CheckUsernameUseCase
import com.joyersapp.auth.domain.usecase.CompleteRegistrationUseCase
import com.joyersapp.auth.domain.usecase.RegisterUseCase
import com.joyersapp.auth.domain.usecase.VerifyOtpUseCase
import com.joyersapp.utils.ApiErrorException
import com.joyersapp.utils.UiText
import com.joyersapp.utils.UiText.*
import com.joyersapp.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val completeRegistrationUseCase: CompleteRegistrationUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState
    private val _navigationEvents = MutableSharedFlow<SignupNavigationEvent>()
    val navigationEvents = _navigationEvents

    private var usernameJob: Job? = null

    fun onEvent(event: SignupEvent) {
        when (event) {

            is SignupEvent.UsernameChanged -> {
                usernameJob?.cancel()
                _uiState.update {
                    it.copy(
                        username = event.value,
                        isValidUsername = false,
                        showUsernameError = false,
                        verificationCode = "",
                        verificationError = null,
                        usernameError = null,
                        passwordError = null,
                        confirmPasswordError = null,
                        isSuggestionSelected = false,
                        usernameSuggestions = emptyList(),
                    )
                }
                val cleanUsername =
                    event.value.text.removePrefix("@") // remove @ before sending request
                if (cleanUsername.length >= 3 && !uiState.value.isSuggestionSelected) {
                    checkUsernameDebounced(cleanUsername)
                }
            }

            is SignupEvent.UsernameFocusChanged -> {
                val prevFocused = _uiState.value.isUsernameFocused

                _uiState.update {
                    it.copy(isUsernameFocused = event.isFocused, error = null)
                }

                if (prevFocused && !event.isFocused && uiState.value.username.text.length < 4) {
                    _uiState.update {
                        it.copy(
                            showUsernameError = true,
                            usernameError = StringResource(R.string.username_must_be_3_15_characters_only_letters_numbers_and_underscores),
                        )
                    }
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

            is SignupEvent.EmailChanged -> {
                val isValidEmail = event.value.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                    event.value
                ).matches()

                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailPhoneError = null,
                        showVerification = false,
                        verificationCode = "",
                        verificationError = null,
                        showPasswordFields = false,
                        isValidEmail = isValidEmail
                    )
                }
            }

            is SignupEvent.EmailFocusChanged -> {
                val state = _uiState.value
                val isValidEmail = state.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                    state.email
                ).matches()
                val prevFocused = _uiState.value.isEmailFocused

                _uiState.update {
                    it.copy(isEmailFocused = event.isFocused, error = null)
                }

                if (prevFocused && !event.isFocused && !isValidEmail) {
                    _uiState.update {
                        it.copy(
                            emailPhoneError = StringResource(R.string.invaild_email),
                        )
                    }
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
                        verificationCode = "",
                        verificationError = null,
                        showPasswordFields = false,
                        isValidPhone = isValidPhone
                    )
                }
            }

            is SignupEvent.PhoneFocusChanged -> {
                val state = _uiState.value
                val isValidPhone = (
                        state.phone.all { it.isDigit() }
                                && state.phone.length in 10..15)
                val prevFocused = _uiState.value.isPhoneFocused

                _uiState.update {
                    it.copy(isPhoneFocused = event.isFocused, error = null)
                }

                if (prevFocused && !event.isFocused && !isValidPhone) {
                    _uiState.update {
                        it.copy(
                            emailPhoneError = StringResource(R.string.invaild_phone),
                        )
                    }
                }
            }

            is SignupEvent.CountryCodeChanged -> {
                _uiState.update {
                    it.copy(
                        selectedCountryCode = event.code,
                    )
                }
                onEvent(SignupEvent.PhoneChanged(_uiState.value.phone))
            }

            is SignupEvent.CountryNameCodeChanged -> {
                _uiState.update {
                    it.copy(
                        selectedCountryNameCode = event.nameCode,
                    )
                }
            }

            is SignupEvent.TogglePhoneMode -> {
                val isPhoneMode = uiState.value.isPhoneMode
                if (isPhoneMode)
                    onEvent(SignupEvent.PhoneChanged(""))
                else
                    onEvent(SignupEvent.EmailChanged(""))
                _uiState.update {
                    it.copy(
                        isPhoneMode = !isPhoneMode,
                        emailPhoneError = null,
                        showVerification = false,
                        showPasswordFields = false
                    )
                }
            }

            is SignupEvent.SendVerificationCode -> { register() }

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

            is SignupEvent.VerifyCode -> { verifyOtpRequest() }

            is SignupEvent.PasswordChanged -> {
                val state = _uiState.value
                var confirmPasswordError: UiText? = null
                if (state.confirmPassword.isNotEmpty()) {
                    if (event.value == state.confirmPassword) {
                        confirmPasswordError = null
                    } else {
                        confirmPasswordError =
                            StringResource(R.string.password_does_not_match)
                    }
                }
                _uiState.update {
                    it.copy(
                        password = event.value,
                        passwordError = null,
                        isValidPassword = isValidPassword(event.value),
                        confirmPasswordError = confirmPasswordError,
                        error = null
                    )
                }
            }

            is SignupEvent.TogglePasswordVisibility -> {
                _uiState.update {
                    it.copy(
                        isPasswordVisible = !_uiState.value.isPasswordVisible
                    )
                }
            }

            is SignupEvent.PasswordFocusChanged -> {
                val state = _uiState.value
                _uiState.update {
                    val passwordError = if (state.password.isEmpty() || isValidPassword(state.password)) {
                        null
                    } else {
                        if (event.isFocused)
                            state.passwordError
                        else
                            StringResource(R.string.weak_password)
                    }
                    it.copy(
                        isPasswordFocused = event.isFocused,
                        passwordError = passwordError,
                        )
                }
            }

            is SignupEvent.ConfirmPasswordChanged -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = event.value,
                        confirmPasswordError = null,
                        error = null
                    )
                }
            }

            is SignupEvent.ToggleConfirmPasswordVisibility -> {
                _uiState.update {
                    it.copy(
                        isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible
                    )
                }
            }

            is SignupEvent.ConfirmPasswordFocusChanged -> {
                val state = _uiState.value
                _uiState.update {
                    var confirmPasswordError: UiText? = null
                    if (state.confirmPassword.isEmpty() || state.passwordError != null || state.confirmPassword == state.password) {
                        confirmPasswordError = null
                    } else {
                        if (event.isFocused)
                            confirmPasswordError = state.confirmPasswordError
                        else
                            confirmPasswordError = StringResource(R.string.password_does_not_match)
                    }
                    it.copy(
                        isConfirmPasswordFocused = event.isFocused,
                        confirmPasswordError = confirmPasswordError,
                    )
                }
            }

            is SignupEvent.SignInButtonTextChanged -> {
//                _uiState.update {
//                    it.copy(
//                        signInButtonText = event.text
//                    )
//                }
            }

            SignupEvent.SubmitClicked -> { completeRegistrationRequest() }


            SignupEvent.CheckUsername -> TODO()
            SignupEvent.ClearSignupError -> TODO()
            SignupEvent.SubmitSignup -> TODO()
            SignupEvent.TogglePasswordFieldsVisibility -> TODO()
            is SignupEvent.UsernameCheckResult -> TODO()

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
                    val updatedUsername =
                        "@${response.username}" // apply API-corrected username with @ prefix
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
                            usernameSuggestions = if (error is ApiErrorException) error.errorBody?.suggestions!! else emptyList(),
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
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    verificationCode = ""
                )
            }

            val result = if (uiState.value.isPhoneMode)
            //            register with phone number
                registerUseCase(
                    RegisterRequestDto.WithPhone(
                        mobile = state.phone,
                        username = state.username.text.removePrefix("@"),
                        country_code = state.selectedCountryCode
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
                            emailPhoneError = DynamicString(error.message ?: "Something went wrong"),
                        )
                    }
                }
            )
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
                            password = "",
                            confirmPassword = "",
                            passwordError = null,
                            confirmPasswordError = null,
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

    private fun completeRegistrationRequest(){
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = if (state.isPhoneMode)
            //            with phone number
                completeRegistrationUseCase(
                    CompleteRegistrationRequestDto.WithPhone(
                        mobile = state.phone,
                        username = state.username.text.removePrefix("@"),
                        country_code = state.selectedCountryCode,
                        otp_code = state.verificationCode,
                        password = state.password,
                        confirmPassword = state.confirmPassword,
                    )
                ) else
            //            verify with email
                completeRegistrationUseCase(
                    CompleteRegistrationRequestDto.WithEmail(
                        email = state.email,
                        username = state.username.text.removePrefix("@"),
                        otp_code = state.verificationCode,
                        password = state.password,
                        confirmPassword = state.confirmPassword,
                    )
                )

            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    _navigationEvents.emit(SignupNavigationEvent.RegistrationCompleted(response.token ?: "", response.user?.id ?: ""))
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }
}
