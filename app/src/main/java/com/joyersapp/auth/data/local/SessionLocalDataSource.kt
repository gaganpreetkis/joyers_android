package com.joyersapp.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.model.AuthUser
import com.joyersapp.auth.data.remote.dto.User
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Singleton
class SessionLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val MIN_SPLASH_DURATION: Int = 1500
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    val authState: StateFlow<AuthState> = _authState

    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val USER_NAMES = stringPreferencesKey("user_names")
    }

    fun refreshAuthState() {
        CoroutineScope(Dispatchers.IO).launch {

            val startTime = System.currentTimeMillis()

            // Resolve auth state
            val token = dataStore.data
                .map { prefs -> prefs[Keys.ACCESS_TOKEN] }.firstOrNull()
            val userId = dataStore.data
                .map { prefs -> prefs[Keys.USER_ID] }.firstOrNull()
            val email = dataStore.data
                .map { prefs -> prefs[Keys.EMAIL] }.firstOrNull()

            val resolvedState =
                if (!token.isNullOrEmpty() && !userId.isNullOrEmpty() && !email.isNullOrEmpty()) {
                    AuthState.Authenticated(AuthUser(userId, email))
                } else {
                    AuthState.Unauthenticated
                }

            // nsure minimum splash duration
            val elapsed = System.currentTimeMillis() - startTime
            val remaining = MIN_SPLASH_DURATION - elapsed
            if (remaining > 0) {
                delay(remaining)
            }

            // Emit final state
            _authState.value = resolvedState
        }
    }

    suspend fun saveUserNames(names: List<User>) {
        val json = Gson().toJson(names)
        dataStore.edit { prefs ->
            prefs[Keys.USER_NAMES] = json
        }
    }

    fun getUserNames(): Flow<List<User>> {
        return dataStore.data.map { prefs ->
            val json = prefs[Keys.USER_NAMES] ?: "[]"
            Gson().fromJson(json, Array<User>::class.java).toList()
        }
    }

    suspend fun saveSession(
        userId: String,
        email: String,
        accessToken: String
    ) {
        dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.EMAIL] = email
            prefs[Keys.ACCESS_TOKEN] = accessToken
            _authState.value = AuthState.Authenticated(AuthUser(userId, email))
        }
    }

    suspend fun clearAccessToken() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.ACCESS_TOKEN)
        }
    }

    fun getAccessToken(): String? = runBlocking {
        dataStore.data
            .map { it[Keys.ACCESS_TOKEN] }
            .first()
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}