package com.joyersapp.utils

fun isAllowedIdentityNameChars(input: String): Boolean {
    // Allows: A–Z, a–z, accented letters, numbers, hyphen, apostrophe
    val regex = Regex("^[A-Za-z0-9'\\-À-ÖØ-öø-ÿ ]+$")
    return regex.matches(input)
}