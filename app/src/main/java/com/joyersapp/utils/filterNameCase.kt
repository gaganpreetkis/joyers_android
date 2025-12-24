package com.joyersapp.utils

fun filterNameCase(input: String): String {
    val result = StringBuilder()
    var startOfWord = true

    for (char in input) {
        when {
            char == ' ' -> {
                result.append(char)
                startOfWord = true
            }
            char.isUpperCase() -> {
                if (startOfWord) {
                    result.append(char) // ✅ allowed
                    startOfWord = false
                }
                // ❌ ignore uppercase mid-word
            }
            else -> {
                result.append(char)
                startOfWord = false
            }
        }
    }
    return result.toString()
}
