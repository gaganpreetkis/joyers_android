package com.joyersapp.core

import com.joyersapp.auth.data.local.SessionLocalDataSource
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.model.AuthUser
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

@Singleton
class SessionManager @Inject constructor(
    private val localDataSource: SessionLocalDataSource
) {
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )
    private val _sessionState =
        MutableStateFlow<SessionState>(SessionState.Initializing)
    val sessionState = _sessionState.asStateFlow()

    private val _tokenExpired = MutableSharedFlow<Unit>()
    val tokenExpired = _tokenExpired.asSharedFlow()
    val authState: StateFlow<AuthState> =
        localDataSource.authState
            .stateIn(
                CoroutineScope(Dispatchers.IO),
                SharingStarted.Eagerly,
                AuthState.Unauthenticated
            )

    init {
        scope.launch {
            authState.first()
            delay(1500)
            _sessionState.value = SessionState.Ready
        }
    }


    suspend fun saveUser(userId: String, email: String, accessToken: String, ) {
        localDataSource.saveUser(userId,email, accessToken)
    }

    suspend fun login() {
        localDataSource.login()
    }

    suspend fun logout() {
        localDataSource.clearSession()
        localDataSource.setAuthState(AuthState.Unauthenticated)
    }


    fun onTokenExpired() {
        scope.launch {
            _tokenExpired.emit(Unit)
            logout()
        }
    }

    fun getAccessToken(): String {
//        CoroutineScope(Dispatchers.IO).launch {
            return localDataSource.getAccessToken() ?: ""
//            localDataSource.saveAuthState(AuthState.Unauthenticated)
//        }
    }
}