package com.joyersapp.utils

fun Int.toPrettyNumber(): String {
    return when {
        this >= 1_000_000_000 -> "${this / 1_000_000_000}B"
        this >= 1_000_000     -> "${this / 1_000_000}M"
        this >= 1_000         -> "${this / 1_000}K"
        else -> this.toString()
    }
}