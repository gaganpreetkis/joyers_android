package com.joyersapp.auth.presentation.signup

import android.util.Patterns
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
import com.joyersapp.auth.domain.usecase.CheckUsernameUseCase
import com.joyersapp.auth.domain.usecase.SignupUseCase
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

            is SignupEvent.EmailChanged -> {
                val isValidEmail = uiState.value.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(
                    uiState.value.email
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
                val isValidPhone = (uiState.value.phone.isNotEmpty()
                        && uiState.value.phone.all { it.isDigit() }
                        && uiState.value.phone.length in 10..15)

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

            is SignupEvent.VerifyCode -> {
                _uiState.update {
                    it.copy(
                        verificationError = null,
                        showVerification = false,
                        showPasswordFields = true,
                    )
                }
            }

            is SignupEvent.SendVerificationCode -> {
                _uiState.update {
                    it.copy(
                        verificationError = null,
                        verificationCode = "",
                    )
                }
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

            SignupEvent.SubmitClicked -> signup()

            SignupEvent.CheckUsername -> TODO()
            SignupEvent.ClearSignupError -> TODO()
            is SignupEvent.ConfirmPasswordFocusChanged -> TODO()
            is SignupEvent.CountryCodeChanged -> {

            }
            is SignupEvent.PasswordFocusChanged -> TODO()
            SignupEvent.SendVerificationCode -> TODO()
            is SignupEvent.SignInButtonTextChanged -> TODO()
            SignupEvent.SubmitSignup -> TODO()
            SignupEvent.ToggleConfirmPasswordVisibility -> TODO()
            SignupEvent.TogglePasswordFieldsVisibility -> TODO()
            SignupEvent.TogglePasswordVisibility -> TODO()
            is SignupEvent.UsernameCheckResult -> TODO()
            is SignupEvent.NextClicked -> {
                if (uiState.value.isPhoneMode) {
                    if (uiState.value.phone.length >= 6) {
                        _uiState.update {
                            it.copy(
                                showVerification = true,
                                codeSentMessage = StringResource(R.string.code_sent_to_phone),
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                showVerification = false,
                                codeSentMessage = StringResource(R.string.invaild_phone),
                            )
                        }
                    }
                } else {
                    if (Patterns.EMAIL_ADDRESS.matcher(uiState.value.email).matches()) {
                        _uiState.update {
                            it.copy(
                                showVerification = true,
                                codeSentMessage = StringResource(R.string.code_sent_to_email),
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                showVerification = false,
                                codeSentMessage = StringResource(R.string.invaild_email),
                            )
                        }
                    }
                }
            }
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
                    when (response.statusCode) {
                        200 -> {
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
                        }
                        400 -> {
                            _uiState.update {
                                it.copy(
                                    usernameSuggestions = response.suggestions ?: emptyList(),
                                    showUsernameLoader = false,
                                    isValidUsername = false,
                                    showUsernameError = true,
                                    usernameError = UiText.DynamicString(response.message)
                                )
                            }
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            showUsernameLoader = false,
                            isValidUsername = false,
                            showUsernameError = true,
                            usernameError = UiText.DynamicString(error.message ?: "Something went wrong")
                        )
                    }
                }
            )
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
