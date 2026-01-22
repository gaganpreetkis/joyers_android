package com.joyersapp.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun convertDate(input: String?): String {
    if (input.isNullOrEmpty()) return ""
    try {
        val inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)

        val outputFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy")

        return LocalDate.parse(input, inputFormatter)
            .format(outputFormatter)
    } catch (e: Exception) {
        return input
    }
}