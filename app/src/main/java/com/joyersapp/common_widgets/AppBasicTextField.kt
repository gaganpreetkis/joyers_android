package com.joyersapp.common_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.utils.fontFamilyLato


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
    placeholderColor: Color = Gray40
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

    // Sync if external value changes
    LaunchedEffect(value) {
        if (!isFocused) {
            tfValue = tfValue.copy(text = value)
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
                .onFocusChanged { isFocused = it.isFocused }
        ) {

            // --------------------------------------------------
            // 1️⃣ SHOW ELLIPSIZED TEXT ONLY WHEN NOT FOCUSED
            // --------------------------------------------------
            if (!isFocused && value.isNotEmpty()) {
                Text(
                    text = value,
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
            // 2️⃣ PLACEHOLDER WHEN EMPTY
            // --------------------------------------------------
            if (value.isEmpty() && !isFocused) {
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
                    if (newValue.text.length <= maxLength) {
                        tfValue = newValue
                        onValueChange(newValue.text)
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
            IconButton(onClick = onPasswordToggle) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) R.drawable.show_password
                        else R.drawable.password_hide
                    ),
                    contentDescription = "Toggle Password",
                )
            }
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
            .padding(start = if (isCentered) 2.dp else if (keyboardOptions.keyboardType == KeyboardType.Phone) 10.dp else 15.dp, end = 0.dp)
            .fillMaxHeight(), // No horizontal padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) { // Placeholder
            if (value.isEmpty()) {
                Text(text = placeholder, color = placeholderColor, style = textStyle, modifier = Modifier.fillMaxWidth())
            }
            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                singleLine = true, enabled = isEnabled, textStyle = textStyle.copy(color = contentColor), visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None, keyboardOptions = keyboardOptions, keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }), cursorBrush = SolidColor(Black), modifier = Modifier.fillMaxWidth(),
            )
        } // Password eye button (optional) - only show when text is present
        if (isPassword && onPasswordToggle != null && value.trim().isNotEmpty()) {
            IconButton(onClick = onPasswordToggle) { Icon(painter = painterResource(if (passwordVisible) R.drawable.show_password else R.drawable.password_hide), contentDescription = "Toggle Password") }
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

            BasicTextField(
                value = value,
                onValueChange = {
                    if (it.text.length <= maxLength) {
                        onValueChange(it)
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
            IconButton(onClick = onPasswordToggle) {
                Icon(
                    painter = painterResource(
                        if (passwordVisible) R.drawable.show_password
                        else R.drawable.password_hide
                    ),
                    contentDescription = "Toggle Password",
                )
            }
        }
    }
}
