package com.joyersapp.utils

fun isValidUsername(username: String): Boolean {
    val regex = "^[a-zA-Z0-9_@]{3,15}$".toRegex()
    return username.matches(regex)
}

