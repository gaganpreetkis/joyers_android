package com.joyersapp.utils

import android.icu.text.BreakIterator
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.joyersapp.components.dialogs.HighlightBullet
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