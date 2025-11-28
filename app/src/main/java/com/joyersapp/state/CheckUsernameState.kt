package com.joyersapp.state

import com.joyersapp.response.CheckUserNameModel

sealed class CheckUsernameState {
    object Idle : CheckUsernameState()
    object Loading : CheckUsernameState()
    data class Success(val response: CheckUserNameModel) : CheckUsernameState()
    data class Error(val message: String) : CheckUsernameState()
}