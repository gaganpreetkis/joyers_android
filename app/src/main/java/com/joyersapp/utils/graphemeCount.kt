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
    val parts = text.split(" ")

    return buildAnnotatedString {
        parts.forEachIndexed { index, word ->

            val isMention = word.startsWith("@") && word.length > 1
            val isHashtag = word.startsWith("#") && word.length > 1
            val isBullet = false
            val isUrl =
                word.startsWith("http") || word.startsWith("https") || word.startsWith("www")

            val color = if (isMention || isHashtag || isUrl) Golden else LightBlack
            val fontWeight =
                if (isMention || isHashtag || isUrl) FontWeight.SemiBold else FontWeight.Normal

            val fontSize = if (isBullet) 26.sp else 16.sp


            withStyle(
                style = SpanStyle(
                    color = color,
                    fontWeight = fontWeight,
                    fontSize = fontSize,
                )
            ) {
                append(word)
            }

            if (index != parts.lastIndex) append(" ")
        }
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