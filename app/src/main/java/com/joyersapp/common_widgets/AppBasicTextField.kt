package com.joyersapp.common_widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.forEachChange
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.substring
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack40
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.Red
import com.joyersapp.utils.filterAscii
import com.joyersapp.utils.filterNameCase
import com.joyersapp.utils.filterSentenceCase
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.graphemeCount
import com.joyersapp.utils.highlightWords
import kotlin.math.sin

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    isEnabled: Boolean = true,
    highlightWords: Boolean = false,
    maxLength: Int = 100,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    textOverflow: TextOverflow = TextOverflow.Clip,
    focusRequester: FocusRequester? = null,
    onFocusChanged: (FocusState) -> Unit = {  },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = fontFamilyLato,
        fontWeight = FontWeight.Normal,
        color = LightBlack,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
    placeHolderTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontFamily = fontFamilyLato,
        fontWeight = FontWeight.Normal,
        color = LightBlack60,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    ),
) {

    // 1. Remember a local state to handle immediate cursor updates
    var localValue by remember { mutableStateOf(TextFieldValue(text = text, selection = TextRange(text.length))) }

    // 2. Sync local state when the ViewModel pushes a programmatic change
    LaunchedEffect(text) {
        if (text != localValue.text) {
            localValue.copy(text = text, selection = TextRange(text.length))
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = localValue,
            onValueChange = { newValue ->
                if (newValue.text.graphemeCount() > maxLength) return@BasicTextField

                if (keyboardOptions.capitalization == KeyboardCapitalization.Words) {

                    val caseFiltered = filterNameCase(newValue.text)

                    if (caseFiltered != newValue.text) {
                        val updated = newValue.copy(
                            text = caseFiltered,
                            selection = TextRange(caseFiltered.length)
                        )
                        localValue = updated
                        onValueChange(caseFiltered)
                    } else {
                        localValue = newValue
                        onValueChange(newValue.text)
                    }

                } else {
                    localValue = newValue // Update UI instantly newValue ->
                    onValueChange(newValue.text)
                }
            },
            enabled = isEnabled,
            visualTransformation = if (highlightWords) {
                VisualTransformation { textValue ->
                    TransformedText(
                        highlightWords(textValue.text),
                        OffsetMapping.Identity
                    )
                }
            } else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = Color.Transparent),
            modifier = modifier
//                .focusable()
                .focusRequester(focusRequester ?: FocusRequester.Default)
                .onFocusChanged { onFocusChanged(it) },
            decorationBox = { inner ->
                Box(Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                    contentAlignment = Alignment.Center)
                {
                    // Editable transparent text overlay
                    inner()
                    // Placeholder
                    if (text.isEmpty()) {
                        Text(
                            placeholder,
                            style = placeHolderTextStyle,
                        )
                    } else {
                        Text(
                            text,
                            style = textStyle,
                            overflow = textOverflow,
                            maxLines = if (singleLine) 1 else maxLines,
                        )
                    }
                }
            }
        )
    }
}

//@Preview
@Composable
fun CustomTextField2(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit = {},
    placeholder: String = "",
    isEnabled: Boolean = true,
    highlightWords: Boolean = false,
    maxLength: Int = 100,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    textOverflow: TextOverflow = TextOverflow.Clip,
    focusRequester: FocusRequester? = null,
    onFocusChanged: (FocusState) -> Unit = { },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActionHandler? = null,
    textStyle: TextStyle = TextStyle(/* ... same as your code ... */),
    placeHolderTextStyle: TextStyle = TextStyle(/* ... same as your code ... */),
    contentAlignment: Alignment = Alignment.Center
) {
    // 1. Create the new persistent state
    val state = remember { TextFieldState(initialText = text) }

//    Track focus state locally
    var isFocused by remember { mutableStateOf(false) }
    val localFocusRequester = focusRequester?: remember { FocusRequester() }

    // 2. Sync State with external 'text' prop (Programmatic updates)
    LaunchedEffect(text) {
        if (text != state.text.toString()) {
            state.edit {
                replace(0, length, text)
                placeCursorAtEnd()
            }
        }
    }

    // 3. Observe state changes to trigger onValueChange
    LaunchedEffect(state.text) {
        onValueChange(state.text.toString())
    }

    BasicTextField(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(localFocusRequester)
            .onFocusChanged {
                if (it.isFocused && !isFocused) {
                    state.edit { placeCursorAtEnd() }
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
            if (asCharSequence().length > maxLength) {
                revertAllChanges()
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
                modifier = Modifier.fillMaxSize(),
                contentAlignment = contentAlignment
            ) {

                Box(modifier = Modifier.alpha(if (isFocused) 1f else 0f)) {
                    innerTextField()
                }

                if (state.text.isEmpty()) {
                    Text(placeholder, style = placeHolderTextStyle)
                }

                if (!isFocused) {
                    Text(
                        if (highlightWords) highlightWords(state.text.toString())
                        else AnnotatedString(state.text.toString()),
                        style = textStyle,
                        overflow = textOverflow,
                        maxLines = maxLines
                    )
                }
            }
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField3(
    modifier: Modifier = Modifier,
    textState: TextFieldState,
    onValueChange: (TextFieldState) -> Unit,
    keyEvent: (String) -> Unit,
    placeholder: String = "",
    isEnabled: Boolean = true,
    highlightWords: Boolean = false,
    maxLength: Int = 1000,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    textOverflow: TextOverflow = TextOverflow.Clip,
    focusRequester: FocusRequester? = null,
    onFocusChanged: (FocusState) -> Unit = { },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActionHandler? = null,
    textStyle: TextStyle = TextStyle(/* ... same as your code ... */),
    placeHolderTextStyle: TextStyle = TextStyle(/* ... same as your code ... */),
    contentAlignment: Alignment = Alignment.Center
) {
//    Track focus state locally
    var isFocused by remember { mutableStateOf(false) }
    val localFocusRequester = focusRequester?: remember { FocusRequester() }

    // 3. Observe state changes to trigger onValueChange
//    LaunchedEffect(textState.text) {
//        onValueChange(textState)
//    }
    val oldText = textState.text.toString()

    BasicTextField(
        state = textState,
        modifier = modifier
            .fillMaxWidth()
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
            // Case filtering logic
            if (keyboardOptions.capitalization == KeyboardCapitalization.Words) {
                val filtered = filterNameCase(asCharSequence().toString())
                if (filtered != asCharSequence().toString()) {
                    replace(0, length, filtered)
                }
            }

            if (asCharSequence().isEmpty() && changes.changeCount > 0) {
                // Check if the operation was actually a deletion (optional logic)
                keyEvent("back")
            }
            changes.forEachChange { sourceRange, replacedLength ->
                val a = sourceRange
                val b = replacedLength
                val string = asCharSequence().toString()
                val newString = asCharSequence().substring(sourceRange)
                if (newString.equals("@") && (string.equals("@") || string.endsWith(" @"))) keyEvent("@")
                if (newString.equals("") && string.equals("")) keyEvent("back")
                asCharSequence().length < textState.text.length
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
                modifier = Modifier.fillMaxSize(),
                contentAlignment = contentAlignment
            ) {

                Box(modifier = Modifier.alpha(if (isFocused) 1f else 0f)) {
                    innerTextField()
                }

                if (textState.text.isEmpty()) {
                    Text(if (highlightWords) highlightWords(placeholder)
                    else AnnotatedString(textState.text.toString()),
                        style = placeHolderTextStyle,
                        overflow = textOverflow,
                        maxLines = maxLines
                    )
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


@Composable
fun AppBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    isCentered: Boolean = false,
    isEnabled: Boolean = true,
    maxLength: Int = 100,
    onPasswordToggle: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamilyLato,
        fontWeight = FontWeight.Normal,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    containerColor: Color = Gray20,
    contentColor: Color = Black,
    placeholderColor: Color = Gray40,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    // Internal TextFieldValue to control cursor position
    var tfValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }

    // Sync if external value changes - always sync to handle clearing
    LaunchedEffect(value) {
        // Always sync tfValue with external value changes
        // This ensures clearing works even when field is focused
        if (tfValue.text != value) {
            tfValue = tfValue.copy(text = value, selection = TextRange(value.length))
        }
    }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .padding(
                start = if (isCentered) 2.dp else if (keyboardOptions.keyboardType == KeyboardType.Phone) 10.dp else 15.dp,
                end = if (isPassword) 0.dp else 2.dp
            )
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    onFocusChanged?.invoke(focusState.isFocused)
                }
        ) {

            // --------------------------------------------------
            // 1️⃣ SHOW ELLIPSIZED TEXT ONLY WHEN NOT FOCUSED
            // --------------------------------------------------
            if (!isFocused && tfValue.text.isNotEmpty()) {
                Text(
                    text = tfValue.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle.copy(color = contentColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Focus the field
                            focusRequester.requestFocus()

                            // Set cursor to END
                            tfValue = tfValue.copy(
                                selection = TextRange(tfValue.text.length)
                            )
                        }
                )
            }

            // --------------------------------------------------
            // 2️⃣ PLACEHOLDER WHEN EMPTY (show even when focused)
            // --------------------------------------------------
            if (tfValue.text.isEmpty()) {
                Text(
                    text = placeholder,
                    color = placeholderColor,
                    style = textStyle,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --------------------------------------------------
            // 3️⃣ BASIC TEXT FIELD (VISIBLE ONLY WHEN FOCUSED)
            // --------------------------------------------------
            BasicTextField(
                value = tfValue,
                onValueChange = { newValue ->

//                    val asciiFiltered = filterAscii(newValue.text, maxLength)

                    if (keyboardOptions.capitalization == KeyboardCapitalization.Words) {

                        val caseFiltered = filterNameCase(newValue.text)

                        if (caseFiltered != newValue.text) {
                            val updated = newValue.copy(
                                text = caseFiltered,
                                selection = TextRange(caseFiltered.length)
                            )
                            tfValue = updated
                            onValueChange(caseFiltered)
                        } else {
                            tfValue = newValue
                            onValueChange(newValue.text)
                        }

                    } else {

//                        if (asciiFiltered != newValue.text) {
//                            val updated = newValue.copy(
//                                text = asciiFiltered,
//                                selection = TextRange(asciiFiltered.length)
//                            )
//                            tfValue = updated
//                            onValueChange(asciiFiltered)
//                        } else {
                            tfValue = newValue
                            onValueChange(newValue.text)
//                        }
                    }
                },
                singleLine = true,
                enabled = isEnabled,
                textStyle = textStyle.copy(color = contentColor),
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                cursorBrush = SolidColor(Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .alpha(if (isFocused) 1f else 0f) // hide when not focused
            )
        }

        // --------------------------------------------------
        // 4️⃣ PASSWORD TOGGLE BUTTON
        // --------------------------------------------------
        if (isPassword && onPasswordToggle != null && value.trim().isNotEmpty()) {
            Image(
                painter = painterResource(
                    if (passwordVisible) R.drawable.show_password
                    else R.drawable.password_hide),
                contentDescription = "Toggle Password",
                Modifier
                    .padding(start = 5.dp, end = 15.dp)
                    .size(24.dp)
                    .clickable {
                        onPasswordToggle()
                    } )
        }
    }
}


@Composable
fun AppBasicTextField2(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    isCentered: Boolean = false,
    isEnabled: Boolean = true,
    maxLength: Int = 100,
    onPasswordToggle: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamilyLato,
        fontWeight = FontWeight.Normal,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    containerColor: Color = Gray20,
    contentColor: Color = Black,
    placeholderColor: Color = Gray40,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    // Internal TextFieldValue to control cursor position
    var tfValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }

    // Sync if external value changes - always sync to handle clearing
    LaunchedEffect(value) {
        // Always sync tfValue with external value changes
        // This ensures clearing works even when field is focused
        if (tfValue.text != value) {
            tfValue = tfValue.copy(text = value, selection = TextRange(value.length))
        }
    }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    onFocusChanged?.invoke(focusState.isFocused)
                }
        ) {

            // --------------------------------------------------
            // 1️⃣ SHOW ELLIPSIZED TEXT ONLY WHEN NOT FOCUSED
            // --------------------------------------------------
            if (!isFocused && tfValue.text.isNotEmpty()) {
                Text(
                    text = tfValue.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle.copy(color = contentColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Focus the field
                            focusRequester.requestFocus()

                            // Set cursor to END
                            tfValue = tfValue.copy(
                                selection = TextRange(tfValue.text.length)
                            )
                        }
                )
            }

            // --------------------------------------------------
            // 2️⃣ PLACEHOLDER WHEN EMPTY (show even when focused)
            // --------------------------------------------------
            if (tfValue.text.isEmpty()) {
                Text(
                    text = placeholder,
                    color = placeholderColor,
                    style = textStyle,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --------------------------------------------------
            // 3️⃣ BASIC TEXT FIELD (VISIBLE ONLY WHEN FOCUSED)
            // --------------------------------------------------
            BasicTextField(
                value = tfValue,
                onValueChange = { newValue ->

//                    val asciiFiltered = filterAscii(newValue.text, maxLength)

                    if (keyboardOptions.capitalization == KeyboardCapitalization.Words) {

                        val caseFiltered = filterNameCase(newValue.text)

                        if (caseFiltered != newValue.text) {
                            val updated = newValue.copy(
                                text = caseFiltered,
                                selection = TextRange(caseFiltered.length)
                            )
                            tfValue = updated
                            onValueChange(caseFiltered)
                        } else {
                            tfValue = newValue
                            onValueChange(newValue.text)
                        }

                    } else {

//                        if (asciiFiltered != newValue.text) {
//                            val updated = newValue.copy(
//                                text = asciiFiltered,
//                                selection = TextRange(asciiFiltered.length)
//                            )
//                            tfValue = updated
//                            onValueChange(asciiFiltered)
//                        } else {
                            tfValue = newValue
                            onValueChange(newValue.text)
//                        }
                    }
                },
                singleLine = true,
                enabled = isEnabled,
                textStyle = textStyle.copy(color = contentColor),
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                cursorBrush = SolidColor(Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .alpha(if (isFocused) 1f else 0f) // hide when not focused
            )
        }

        // --------------------------------------------------
        // 4️⃣ PASSWORD TOGGLE BUTTON
        // --------------------------------------------------
        if (isPassword && onPasswordToggle != null && value.trim().isNotEmpty()) {
            Image(
                painter = painterResource(
                    if (passwordVisible) R.drawable.show_password
                    else R.drawable.password_hide),
                contentDescription = "Toggle Password",
                Modifier
                    .padding(start = 5.dp, end = 15.dp)
                    .size(24.dp)
                    .clickable {
                        onPasswordToggle()
                    } )
        }
    }
}

@Composable
fun AppBasicTextFieldForLetterSpacing(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    isCentered: Boolean = false,
    isEnabled: Boolean = true,
    maxLength: Int = 100,
    onPasswordToggle: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, fontFamily = fontFamilyLato, fontWeight = FontWeight.Normal, platformStyle = PlatformTextStyle(includeFontPadding = false)),
    containerColor: Color = Gray20,
    contentColor: Color = Black,
    placeholderColor: Color = Gray40,
    letterSpacing: TextUnit = TextUnit.Unspecified
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(5.dp))
            .border(
                color = GrayLightBorder,
                width = 1.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(
                start = if (isCentered) 2.dp else if (keyboardOptions.keyboardType == KeyboardType.Phone) 10.dp else 15.dp,
                end = 0.dp
            )
            .fillMaxHeight(), // No horizontal padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) { // Placeholder
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    color = placeholderColor,
                    style = textStyle,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            BasicTextField(
                value = value,
                onValueChange = { newText ->
                    val filtered = filterAscii(newText, maxLength)
                    /*if (newText.length <= maxLength) {
                        onValueChange(newText)
                    }*/
                    if (filtered != newText) {
                        onValueChange(filtered)
                    } else {
                        onValueChange(newText)
                    }
                },
                singleLine = true,
                enabled = isEnabled,
                textStyle = textStyle.copy(fontFamily = fontFamilyLato, fontWeight = FontWeight.SemiBold, color = contentColor, letterSpacing = letterSpacing),
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                cursorBrush = SolidColor(Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 1.dp),
            )
        } // Password eye button (optional) - only show when text is present
        if (isPassword && onPasswordToggle != null && value.trim().isNotEmpty()) {
            Image(
                painter = painterResource(
                    if (passwordVisible) R.drawable.show_password
                    else R.drawable.password_hide),
                contentDescription = "Toggle Password",
                Modifier
                    .padding(start = 5.dp, end = 15.dp)
                    .size(24.dp)
                    .clickable {
                        onPasswordToggle()
                    } )
        }
    }
}

@Composable
fun AppBasicTextFieldForPassword(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    isCentered: Boolean = false,
    isEnabled: Boolean = true,
    maxLength: Int = 100,
    onPasswordToggle: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, fontFamily = fontFamilyLato, fontWeight = FontWeight.Normal, platformStyle = PlatformTextStyle(includeFontPadding = false)),
    containerColor: Color = Gray20,
    contentColor: Color = Black,
    placeholderColor: Color = Gray40
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .padding(
                start = if (isCentered) 2.dp else if (keyboardOptions.keyboardType == KeyboardType.Phone) 10.dp else 15.dp,
                end = 0.dp
            )
            .fillMaxHeight(), // No horizontal padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) { // Placeholder
            if (value.isEmpty()) {
                Text(text = placeholder, color = placeholderColor, style = textStyle, modifier = Modifier.fillMaxWidth())
            }
            BasicTextField(
                value = value,
                onValueChange = { newText ->
                    val filtered = filterAscii(newText, maxLength)
                    val sanitized = filtered.replace(" ", "")
                    if (sanitized != newText) {
                        onValueChange(sanitized)
                    } else {
                        onValueChange(newText)
                    }
                },
                singleLine = true, enabled = isEnabled, textStyle = textStyle.copy(color = contentColor), visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None, keyboardOptions = keyboardOptions, keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }), cursorBrush = SolidColor(Black), modifier = Modifier.fillMaxWidth(),
            )
        } // Password eye button (optional) - only show when text is present
        if (isPassword && onPasswordToggle != null && value.trim().isNotEmpty()) {
            Image(
                painter = painterResource(
                    if (passwordVisible) R.drawable.show_password
                    else R.drawable.password_hide
                ),
                contentDescription = "Toggle Password",
                Modifier
                    .padding(start = 5.dp, end = 15.dp)
                    .size(24.dp)
                    .clickable {
                        onPasswordToggle()
                    } )
        }
    }
}

// Overload that accepts TextFieldValue for cursor control
@Composable
fun AppBasicTextFieldWithCursorHandling(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    isCentered: Boolean = false,
    isEnabled: Boolean = true,
    maxLength: Int = 100,
    onPasswordToggle: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        fontFamily = fontFamilyLato,
        fontWeight = FontWeight.Normal,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    containerColor: Color = Gray20,
    contentColor: Color = Black,
    placeholderColor: Color = Gray40
) {


    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(8.dp))
            .padding(
                start = if (isCentered) 2.dp else if (keyboardOptions.keyboardType == KeyboardType.Phone) 10.dp else 15.dp,
                end = 2.dp
            )
            .fillMaxHeight(), // No horizontal padding
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(1f)) {

            // Placeholder
            if (value.text.isEmpty()) {
                Text(
                    text = placeholder,
                    color = placeholderColor,
                    style = textStyle,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 1. Remember a local state to handle immediate cursor updates
            var localValue by remember { mutableStateOf(value) }

            // 2. Sync local state when the ViewModel pushes a programmatic change
            LaunchedEffect(value) {
                if (value.text != localValue.text || value.selection != localValue.selection) {
                    localValue = value
                }
            }
            BasicTextField(
                value = localValue,
                onValueChange = { newValue ->
                    localValue = newValue // Update UI instantly

                    val filtered = filterAscii(newValue.text, maxLength)
                    if (filtered != newValue.text) {
                        val updated = newValue.copy(
                            text = filtered,
                            selection = TextRange(filtered.length)
                        )
                        onValueChange(updated)
                    } else {
                        onValueChange(newValue)
                    }
                },
                singleLine = true,
                enabled = isEnabled,
                textStyle = textStyle.copy(color = contentColor),
                visualTransformation = if (isPassword && !passwordVisible)
                    PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                cursorBrush = SolidColor(Black),
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    },
//                    onNext = {
//
//                    }
                )
            )
        }

        // Password eye button (optional) - only show when text is present
        if (isPassword && onPasswordToggle != null && value.text.trim().isNotEmpty()) {
            Image(
                painter = painterResource(
                    if (passwordVisible) R.drawable.show_password
                    else R.drawable.password_hide),
                contentDescription = "Toggle Password",
                Modifier
                    .padding(start = 5.dp, end = 15.dp)
                    .size(24.dp)
                    .clickable {
                        onPasswordToggle()
                    } )
        }
    }
}
