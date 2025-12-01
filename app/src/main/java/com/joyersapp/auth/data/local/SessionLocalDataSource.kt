package com.joyersapp.auth.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.joyersapp.auth.domain.model.AuthState
import com.joyersapp.auth.domain.model.AuthUser
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class SessionLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    val authState: Flow<AuthState> = dataStore.data
        .map { prefs ->
            val userId = prefs[Keys.USER_ID]
            val email = prefs[Keys.EMAIL]
            if (userId != null && email != null) {
                AuthState.Authenticated(AuthUser(userId, email))
            } else {
                AuthState.Unauthenticated
            }
        }
        .onStart { emit(AuthState.Unknown) }

    suspend fun storeSession(
        userId: String,
        email: String,
        accessToken: String
    ) {
        dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.EMAIL] = email
            prefs[Keys.ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}