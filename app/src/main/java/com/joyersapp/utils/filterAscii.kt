package com.joyersapp.utils

fun filterAscii(input: String, maxLength: Int): String {
    return input.filter { it.code in 32..126 }.take(maxLength)
}