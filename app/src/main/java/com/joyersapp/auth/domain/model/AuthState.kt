package com.joyersapp.auth.domain.model

sealed class AuthState {
    data object Unknown : AuthState()      // app just launched, still checking
    data object Unauthenticated : AuthState()
    data class Authenticated(val user: AuthUser) : AuthState()
}