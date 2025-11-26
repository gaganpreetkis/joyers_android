package com.synapse.joyers.common_widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.synapse.joyers.utils.fontFamilyLato

@Composable
fun AutoSizingHtmlText(
    annotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    minTextSize: Float? = null,
    maxTextSize: Float = 19f,
    onTextSizeMeasured: (Float) -> Unit = {},
    key: Int = 0
) {
    var textSize by remember(key) { mutableStateOf(maxTextSize.sp) }
    var readyToDraw by remember(key) { mutableStateOf(false) }
    var useMinSize by remember(key) { mutableStateOf(false) }
    var hasReportedSize by remember(key) { mutableStateOf(false) }
    var appliedMinSize by remember(key) { mutableStateOf<Float?>(null) }

    // When minTextSize becomes available, switch to using it
    LaunchedEffect(minTextSize) {
        if (minTextSize != null && readyToDraw && !useMinSize) {
            useMinSize = true
            appliedMinSize = minTextSize
            // Reset readyToDraw to allow re-measurement with min size
            readyToDraw = false
        }
    }

    // Determine the final text size to use
    val currentTextSize = if (useMinSize && appliedMinSize != null) {
        appliedMinSize!!.sp
    } else {
        textSize
    }

    Text(
        text = annotatedString,
        fontSize = currentTextSize,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        fontFamily = fontFamilyLato,
        softWrap = false,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        onTextLayout = { result ->
            if (!readyToDraw) {
                if (result.didOverflowWidth) {
                    // Text overflows - reduce size
                    if (useMinSize && appliedMinSize != null) {
                        // Using min size but still overflows - reduce the applied size
                        if (appliedMinSize!! > 12f) {
                            appliedMinSize = appliedMinSize!! - 1f
                        } else {
                            readyToDraw = true
                        }
                    } else {
                        // First pass: auto-size from 19sp down to 12sp
                        if (textSize > 12.sp) {
                            textSize = (textSize.value - 1f).sp
                        } else {
                            // Reached minimum, mark as ready
                            readyToDraw = true
                            if (!hasReportedSize) {
                                hasReportedSize = true
                                onTextSizeMeasured(textSize.value)
                            }
                        }
                    }
                } else {
                    // Text fits at current size
                    readyToDraw = true
                    if (!hasReportedSize && !useMinSize) {
                        hasReportedSize = true
                        onTextSizeMeasured(textSize.value)
                    }
                }
            }
        }
    )
}