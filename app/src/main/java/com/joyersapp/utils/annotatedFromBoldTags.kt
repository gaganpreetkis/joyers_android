package com.joyersapp.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun annotatedFromBoldTags(text: String): AnnotatedString {
    return buildAnnotatedString {
        var cursor = 0

        while (true) {
            val start = text.indexOf("<b>", cursor)
            if (start == -1) {
                // No more bold tags → append all remaining text normally
                append(text.substring(cursor))
                break
            }

            // Append normal text before <b>
            append(text.substring(cursor, start))

            val end = text.indexOf("</b>", start)
            if (end == -1) {
                // Malformed tag → append rest normally
                append(text.substring(start))
                break
            }

            // Extract bold text
            val boldText = text.substring(start + 3, end)

            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = fontFamilyLato)) {
                append(boldText)
            }

            // Move cursor after </b>
            cursor = end + 4
        }
    }
}

