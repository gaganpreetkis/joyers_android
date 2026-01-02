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
