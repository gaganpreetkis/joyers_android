package com.joyersapp.core

sealed class SessionState {
    object Initializing : SessionState()
    object Ready : SessionState()
}