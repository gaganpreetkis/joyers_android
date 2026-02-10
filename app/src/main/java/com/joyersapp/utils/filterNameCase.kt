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
//fun filterSentenceCase(input: String): String {
//    if (input.isEmpty()) return input
//
//    val result = StringBuilder(input)
//    var capitalizeNext = true
//
//    for (i in result.indices) {
//        val c = result[i]
//
//        if (capitalizeNext && c.isLetter()) {
//            result.setCharAt(i, c.uppercaseChar())
//            capitalizeNext = false
//        } else if (c == '.' || c == '!' || c == '?') {
//            capitalizeNext = true
//        }
//    }
//    return result.toString()
//}

fun filterSentenceCase(input: String): String {
    if (input.isEmpty()) return input

    val result = StringBuilder(input.lowercase()) // Start all lowercase for true sentence case
    var capitalizeNext = true

    for (i in result.indices) {
        val c = result[i]

        if (capitalizeNext && c.isLetter()) {
            result.setCharAt(i, c.uppercaseChar())
            capitalizeNext = false
        }

        // Check for sentence enders (. ! ?)
        else if (c == '.' || c == '!' || c == '?') {
            val nextIndex = i + 1

            // Only capitalize if it's the end of the string,
            // followed by a space, or followed by a newline.
            if (nextIndex < result.length) {
                val nextChar = result[nextIndex]
                if (nextChar == ' ' || nextChar == '\n' || nextChar == '\r') {
                    capitalizeNext = true
                } else {
                    // It's likely a link (e.g., .com) or shorthand (e.g., i.e.)
                    capitalizeNext = false
                }
            }
        }
        // Also handle new lines directly as sentence starts
        else if (c == '\n' || c == '\r') {
            capitalizeNext = true
        }
    }
    return result.toString()
}