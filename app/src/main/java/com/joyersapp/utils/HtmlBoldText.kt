package com.joyersapp.utils

import android.graphics.Typeface
import android.text.style.StyleSpan
import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat

@Composable
fun HtmlBoldText(
    @StringRes resId: Int,
    color: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val html = context.getString(resId)

    val annotated = remember(resId, html) {
        htmlToAnnotatedString(html)
    }

    Text(
        text = annotated,
        fontSize = 16.sp,
        fontFamily = fontFamilyLato,
        color = color,
        modifier = modifier
    )
}

fun htmlToAnnotatedString(html: String) = buildAnnotatedString {
    val spanned = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    append(spanned.toString())

    spanned.getSpans(0, spanned.length, StyleSpan::class.java).forEach { span ->
        val start = spanned.getSpanStart(span)
        val end = spanned.getSpanEnd(span)

        val isBold = span.style == Typeface.BOLD || span.style == Typeface.BOLD_ITALIC
        if (isBold) {
            addStyle(
                SpanStyle(fontWeight = FontWeight.Bold),
                start,
                end
            )
        }
    }
}
