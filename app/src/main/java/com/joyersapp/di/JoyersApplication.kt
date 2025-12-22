package com.joyersapp.di

import android.app.Application
import com.joyersapp.auth.data.local.SessionLocalDataSource
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject

@HiltAndroidApp
class JoyersApplication : Application() {

    @Inject
    lateinit var sessionManager: SessionLocalDataSource

    override fun onCreate() {
        super.onCreate()

        // ✅ Runs only on cold start / process restart
        sessionManager.refreshAuthState()
    }
}