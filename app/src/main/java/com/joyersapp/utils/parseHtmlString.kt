package com.joyersapp.utils

import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.joyersapp.theme.Golden
import com.joyersapp.theme.LightBlack

// Helper function to parse HTML string with font color and bold tags
fun parseHtmlString(htmlString: String): AnnotatedString {
    // Use Android's Html parser to get Spanned text with color information
    val spanned = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
    val plainText = spanned.toString()

    return buildAnnotatedString {
        // Get all ForegroundColorSpan objects
        val colorSpans = spanned.getSpans(0, plainText.length, ForegroundColorSpan::class.java)
        val styleSpans = spanned.getSpans(0, plainText.length, StyleSpan::class.java)

        // Golden color in Android format
        val goldenColor = Color.parseColor("#D69E3A")

        // Build a map of positions to colors and styles
        val styleMap = mutableMapOf<Int, Pair<androidx.compose.ui.graphics.Color, FontWeight>>()

        // Process color spans
        colorSpans.forEach { span ->
            val start = spanned.getSpanStart(span)
            val end = spanned.getSpanEnd(span)
            val colorInt = span.foregroundColor
            val composeColor = if (colorInt == goldenColor) {
                Golden
            } else {
                LightBlack
            }

            // Check if there's a bold style span at this position
            var isBold = false
            styleSpans.forEach { styleSpan ->
                if (spanned.getSpanStart(styleSpan) <= start && spanned.getSpanEnd(styleSpan) >= end) {
                    if (styleSpan.style == Typeface.BOLD) {
                        isBold = true
                    }
                }
            }

            val fontWeight = if (isBold || composeColor == Golden) {
                FontWeight.Bold
            } else {
                FontWeight.SemiBold
            }

            // Mark this range
            for (i in start until end) {
                styleMap[i] = Pair(composeColor, fontWeight)
            }
        }

        // Default style for text without spans
        val defaultStyle = SpanStyle(color = LightBlack, fontWeight = FontWeight.SemiBold)

        // Build the annotated string character by character
        var currentStyle: Pair<androidx.compose.ui.graphics.Color, FontWeight>? = null

        plainText.forEachIndexed { index, char ->
            val style = styleMap[index] ?: Pair(LightBlack, FontWeight.SemiBold)

            if (currentStyle != style) {
                // Style changed, pop old and push new
                if (currentStyle != null) {
                    pop()
                }
                pushStyle(SpanStyle(color = style.first, fontWeight = style.second))
                currentStyle = style
            }

            append(char)
        }

        // Pop the last style
        if (currentStyle != null) {
            pop()
        }
    }
}