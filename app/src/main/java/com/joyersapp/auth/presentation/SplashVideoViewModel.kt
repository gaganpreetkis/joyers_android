package com.joyersapp.auth.presentation

import android.util.Patterns
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
import com.joyersapp.auth.data.local.SessionLocalDataSource
import com.joyersapp.auth.data.remote.dto.signup.CompleteRegistrationRequestDto
import com.joyersapp.auth.data.remote.dto.signup.RegisterRequestDto
import com.joyersapp.auth.data.remote.dto.signup.VerifyOtpRequestDto
import com.joyersapp.auth.domain.usecase.CheckUsernameUseCase
import com.joyersapp.auth.domain.usecase.CompleteRegistrationUseCase
import com.joyersapp.auth.domain.usecase.RegisterUseCase
import com.joyersapp.auth.domain.usecase.VerifyOtpUseCase
import com.joyersapp.auth.presentation.signup.SignupEvent
import com.joyersapp.auth.presentation.signup.SignupNavigationEvent
import com.joyersapp.auth.presentation.signup.SignupUiState
import com.joyersapp.core.SessionManager
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
class SplashVideoViewModel @Inject constructor(
    val sessionManager: SessionManager
) : ViewModel() {


    fun navigateToDashboard() {
        viewModelScope.launch {
            sessionManager.login()
        }
    }

}
