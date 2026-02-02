package com.joyersapp.utils

import android.icu.text.BreakIterator
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.joyersapp.components.dialogs.HighlightBullet
import com.joyersapp.theme.Golden
import com.joyersapp.theme.LightBlack
import java.util.Locale

fun String.graphemeCount(): Int {
    val iterator = BreakIterator.getCharacterInstance(Locale.getDefault())
    iterator.setText(this)

    var count = 0
    var start = iterator.first()
    while (iterator.next() != BreakIterator.DONE) {
        count++
        start = iterator.current()
    }
    return count
}

fun String.takeGraphemes(max: Int): String {
    val iterator = BreakIterator.getCharacterInstance(Locale.getDefault())
    iterator.setText(this)

    val result = StringBuilder()
    var count = 0

    var start = iterator.first()
    var end = iterator.next()

    while (end != BreakIterator.DONE && count < max) {
        result.append(this.substring(start, end))
        count++
        start = end
        end = iterator.next()
    }

    return result.toString()
}

fun highlightWords(text: String): AnnotatedString {

    val mentionHashtagUrlRegex = Regex(
        "(@[A-Za-z0-9_]+)|" +
                "(#[A-Za-z0-9_]+)|" +
                "(www\\.[A-Za-z0-9][A-Za-z0-9\\-]*\\.(com|org|net|co|in)(?=[^A-Za-z0-9]|$)(/[^\\s]*)?)",
        RegexOption.IGNORE_CASE
    )

    return buildAnnotatedString {

        var lastIndex = 0

        mentionHashtagUrlRegex.findAll(text).forEach { match ->

            val start = match.range.first
            val end = match.range.last + 1
            val value = match.value

            // Append normal text before match
            append(text.substring(lastIndex, start))

            val isMention = value.startsWith("@")
            val isHashtag = value.startsWith("#")
            val isUrl = value.startsWith("www", true)

            val color =
                if (isMention || isHashtag || isUrl) Golden else LightBlack

            val fontWeight =
                if (isMention || isHashtag || isUrl)
                    FontWeight.SemiBold
                else
                    FontWeight.Normal

            pushStyle(
                SpanStyle(
                    color = color,
                    fontWeight = fontWeight,
                    fontSize = 16.sp
                )
            )

            append(value)

            pop()

            lastIndex = end
        }

        // Append remaining text
        append(text.substring(lastIndex))
    }
}

fun String?.toHighlightBullets(): List<HighlightBullet> {

    if (isNullOrEmpty()) return listOf(HighlightBullet())

    return lines()
        .map { line ->
            line.removePrefix("•")
                .trimStart()
        }
        .filter { it.isNotEmpty() }
        .map { text ->
            val tt = text.takeGraphemes(25)
            HighlightBullet(textValue = TextFieldValue(
                tt,
                TextRange(tt.length)
            ))
        }
        .ifEmpty { listOf(HighlightBullet()) }
}

fun String.filteredBio(): String {
    var text = ""
    val prefix = "Highlights\n"
    if (this.startsWith(prefix) == true) {
        text = this.removePrefix(prefix)
    } else {
        text = this
    }
    return text
}

fun countBullets(text: String): Int {
    return Regex("""(?m)^•\s""").findAll(text).count()
}