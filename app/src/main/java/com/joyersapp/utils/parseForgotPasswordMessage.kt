package com.joyersapp.utils

fun parseForgotPasswordMessage(message: String): Pair<String, String> {
    val lastSpace = message.lastIndexOf(" ")
    return if (lastSpace != -1) {
        val main = message.substring(0, lastSpace)
        val secondary = message.substring(lastSpace + 1)
        main to secondary
    } else {
        message to ""
    }
}
