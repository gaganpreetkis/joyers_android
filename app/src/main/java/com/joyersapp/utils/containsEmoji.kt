package com.joyersapp.utils

import java.util.regex.Pattern

fun containsEmoji(text: String): Boolean {
    // This regex matches "Other Symbols" Unicode category, which includes most emojis
    val emojiPattern = Pattern.compile("\\p{So}+", Pattern.CASE_INSENSITIVE)
    return emojiPattern.matcher(text).find()
}