package com.joyersapp.auth.presentation.login

import android.util.Patterns
import androidx.compose.runtime.remember
import com.joyersapp.utils.isValidUsername
import com.joyersapp.auth.data.remote.dto.User

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val apiOnlyUsernameErrorMessage: String = "",
    val apiErrorMessage: String = "",
    val apiFailedErrorMessage: String = "",
    val passwordVisible: Boolean = false,
    val emailPhoneError: Boolean = false,
    val passwordError: Boolean = false,
    val isPhoneMode: Boolean = false,
    val showSocialDialog: Boolean = false,
    val rememberMe: Boolean = false,
    val isLoginApiSuccess: Boolean = false,
    val selectedCountryCode: String = "+1",
    val selectedCountryNameCode: String = "US",
    val recentUsersList: MutableList<User> = arrayListOf(),
) {
    val isFormValid: Boolean = if (isPhoneMode) {
        // PHONE VALIDATION
        username.isNotEmpty() &&
                username.all { it.isDigit() } &&
                username.length in 10..15
    } else {
        // USERNAME or EMAIL VALIDATION
        username.isNotEmpty() && (isValidUsername(username) || Patterns.EMAIL_ADDRESS.matcher(username).matches())
    }

    val filteredList: MutableList<User> = if (username.isEmpty()) {
        recentUsersList
    } else {
        recentUsersList.filter { user ->
            val recentValue = if (user.recentType.equals("email", true)) {
                user.email.orEmpty()
            } else {
                user.username.orEmpty()
            }
            recentValue.contains(username, ignoreCase = true)
        }
    }.toMutableList()
}