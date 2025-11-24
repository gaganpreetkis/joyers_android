package com.synapse.joyers.utils

fun isValidNameAdvanced(name: String): Boolean {
    // Check length
    if (name.length > 45) return false

    // Disallow emojis and symbols using character types
    val containsEmojiOrSymbol = name.any {
        val type = Character.getType(it)
        type == Character.SURROGATE.toInt() || // Emojis
                type == Character.OTHER_SYMBOL.toInt() ||
                type == Character.MATH_SYMBOL.toInt() ||
                type == Character.CURRENCY_SYMBOL.toInt() ||
                type == Character.MODIFIER_SYMBOL.toInt() ||
                type == Character.MODIFIER_LETTER.toInt() ||
                Character.isISOControl(it) ||
                Character.isWhitespace(it) && it != ' ' // disallow tabs, newlines
    }
    if (containsEmojiOrSymbol) return false

    // Allowed characters: Unicode letters, digits, space, hyphen, apostrophe
    val regex = "^[\\p{L}0-9\\-' ]{1,45}$".toRegex()
    if (!regex.matches(name)) return false

    return true
}
