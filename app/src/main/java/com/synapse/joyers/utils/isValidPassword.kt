package com.synapse.joyers.utils

fun isValidPassword(password: String): Boolean {
    val passwordRegex = Regex("^(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,16}$")
    return passwordRegex.matches(password)
}