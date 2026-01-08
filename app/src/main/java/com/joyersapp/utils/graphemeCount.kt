package com.joyersapp.utils

import android.icu.text.BreakIterator
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

fun countBullets(text: String): Int {
    return Regex("""(?m)^•\s""").findAll(text).count()
}