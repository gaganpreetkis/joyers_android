package com.joyersapp.utils

fun flagEmoji(countryCode: String): String {
    return countryCode.uppercase().map {
        Character.toChars(it.code + 127397).concatToString()
    }.joinToString("")
}