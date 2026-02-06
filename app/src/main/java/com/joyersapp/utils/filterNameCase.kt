package com.joyersapp.utils

fun filterNameCase(input: String): String {
    return input
        .trimStart() // optional: avoid leading-space issues
        .split(Regex("\\s+"))
        .joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { c ->
                if (c.isLowerCase()) c.titlecase() else c.toString()
            }
        }
}
fun filterSentenceCase(input: String): String {
    if (input.isEmpty()) return input

    val result = StringBuilder(input)
    var capitalizeNext = true

    for (i in result.indices) {
        val c = result[i]

        if (capitalizeNext && c.isLetter()) {
            result.setCharAt(i, c.uppercaseChar())
            capitalizeNext = false
        } else if (c == '.' || c == '!' || c == '?') {
            capitalizeNext = true
        }
    }
    return result.toString()
}