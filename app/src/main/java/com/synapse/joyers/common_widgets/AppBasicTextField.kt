package com.synapse.joyers.common_widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synapse.joyers.R
import com.synapse.joyers.ui.theme.Black
import com.synapse.joyers.ui.theme.Gray20
import com.synapse.joyers.ui.theme.Gray40
import com.synapse.joyers.utils.fontFamilyLato

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
                onValueChange = {
                    if (it.length <= maxLength) {
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
            )
        }

        // Password eye button (optional) - only show when text is present
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
                modifier = Modifier.fillMaxWidth()
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
