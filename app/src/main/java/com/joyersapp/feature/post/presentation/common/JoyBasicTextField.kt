package com.joyersapp.feature.post.presentation.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.forEachChange
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import com.joyersapp.R
import com.joyersapp.utils.filterNameCase
import com.joyersapp.utils.filterSentenceCase
import com.joyersapp.utils.graphemeCount
import com.joyersapp.utils.highlightWords


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JoyTextField(
    modifier: Modifier = Modifier,
    textState: TextFieldState,
    keyEvent: (String) -> Unit = {},
    placeholder: String = "",
    isEnabled: Boolean = true,
    highlightWords: Boolean = false,
    maxLength: Int = 1000,
    maxLines: Int = 100,
    singleLine: Boolean = true,
    textOverflow: TextOverflow = TextOverflow.Ellipsis,
    focusRequester: FocusRequester? = null,
    onFocusChanged: (FocusState) -> Unit = { },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActionHandler? = null,
    textStyle: TextStyle = TextStyle(/* ... same as your code ... */),
    placeHolderTextStyle: TextStyle = TextStyle(/* ... same as your code ... */),
    contentAlignment: Alignment = Alignment.TopStart
) {

//    Track focus state locally
    var isFocused by remember { mutableStateOf(false) }
    val localFocusRequester = focusRequester?: remember { FocusRequester() }

    BasicTextField(
        state = textState,
        modifier = modifier
            .focusRequester(localFocusRequester)
            .onFocusChanged {
                if (it.isFocused && !isFocused) {
                    textState.edit { placeCursorAtEnd() }
                }
                isFocused = it.isFocused
                onFocusChanged(it)
            },
        enabled = isEnabled,
        lineLimits = if (singleLine) TextFieldLineLimits.SingleLine
        else TextFieldLineLimits.MultiLine(1, maxLines),
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActions,
        textStyle = textStyle,
        // 4. Handle MaxLength and Case Filtering via InputTransformation
        inputTransformation = InputTransformation {
            // Max length check
            if (asCharSequence().toString().graphemeCount() > maxLength) {
                revertAllChanges()
            }

            changes.forEachChange { sourceRange, replacedLength ->
                val string = asCharSequence().toString()
                val newString = asCharSequence().substring(sourceRange)
                if (newString.equals("@") && (string.equals("@") || string.endsWith(" @"))) keyEvent("@")
            }

            // Case filtering logic
            if (keyboardOptions.capitalization == KeyboardCapitalization.Words) {
                val filtered = filterNameCase(asCharSequence().toString())
                if (filtered != asCharSequence().toString()) {
                    replace(0, length, filtered)
                }
            }
            if (keyboardOptions.capitalization == KeyboardCapitalization.Sentences) {
                val filtered = filterSentenceCase(asCharSequence().toString())
                if (filtered != asCharSequence().toString()) {
                    replace(0, length, filtered)
                }
            }
        },
        // 5. Highlighting via OutputTransformation (Auto-handles cursor!)
        outputTransformation = if (highlightWords) {
            OutputTransformation {
                // 1. Get the current text from the buffer
                val rawText = asCharSequence().toString()

                // 2. Process your highlighting logic (returning AnnotatedString)
                val highlighted = highlightWords(rawText)

                // 3. Apply styles directly to the buffer
                // In Compose 1.7+, use addStyle or insertAttributes
                highlighted.spanStyles.forEach { range ->
                    addStyle(range.item, range.start, range.end)
                }
            }
        } else null,
        decorator = { innerTextField ->
            Box(
                modifier = Modifier,
                contentAlignment = contentAlignment
            ) {

                Box(modifier = Modifier.alpha(if (isFocused) 1f else 0f)) {
                    innerTextField()
                }

                if (textState.text.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(placeholder, style = placeHolderTextStyle)
                        Spacer(Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_joy_emoji),
                            contentDescription = "Let’s Joy",
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                if (!isFocused) {
                    Text(
                        if (highlightWords) highlightWords(textState.text.toString())
                        else AnnotatedString(textState.text.toString()),
                        style = textStyle,
                        overflow = textOverflow,
                        maxLines = maxLines
                    )
                }
            }
        }
    )
}