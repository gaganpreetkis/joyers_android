package com.synapse.joyers.common_widgets

import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.synapse.joyers.R
import com.synapse.joyers.ui.theme.Black
import com.synapse.joyers.ui.theme.Gray40
import com.synapse.joyers.ui.theme.Red


/*@Composable
fun BasicTextField(
    value: String,
    onValueChange: () -> Unit = {},
    modifier: Modifier) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
//        placeholder = { Text(contactPlaceHolder, color = Gray40) },
//        modifier = Modifier.weight(0.97f),
        textStyle = TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF1F1F1),
            focusedContainerColor = Color(0xFFF1F1F1),
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}*/

@Composable
fun CountryCodePicker(
    defaultCountry: String = "US",
    onCountrySelected: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AndroidView(
            modifier = Modifier.wrapContentHeight(),
            factory = { context ->
                com.hbb20.CountryCodePicker(context).apply {
                    setDefaultCountryUsingNameCode(defaultCountry)
                    setCountryForNameCode(defaultCountry)
                    setAutoDetectedCountry(false)
                    showFullName(false)
                    showNameCode(false)
                    setShowPhoneCode(true)
                    showFlag(true)
                    setOnCountryChangeListener {
                        onCountrySelected(selectedCountryCodeWithPlus)
                    }
                }
            },
            update = { ccp ->
                ccp.setAutoDetectedCountry(false)
                ccp.setCountryForNameCode(defaultCountry)
                //val clickConsumer = ccp.findViewById<View>(com.hbb20.R.id.rlClickConsumer)
                //clickConsumer?.setPadding(ccp.paddingLeft, ccp.paddingTop, 0, ccp.paddingBottom)
                val flagView = ccp.imageViewFlag
                val sizePx = (20 * ccp.resources.displayMetrics.density).toInt()
                flagView.layoutParams = LinearLayout.LayoutParams(sizePx, sizePx)
                flagView.requestLayout()
            }
        )
        VerticalDivider(
            modifier = Modifier
                .height(24.dp),
            color = Color(0xFFBDBDBD),
            thickness = 1.dp
        )
    }
}



/*
@Composable
@Preview
fun PasswordField(password: String = "", passwordError: String = "") {


    var isPasswordVisible by remember { mutableStateOf(false) }

    // Password Input
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (passwordError != null) Color(0xFFFFE5E5) else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (passwordError != null) Red else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.password_icon),
            contentDescription = null,
            modifier = Modifier.size(23.dp, 26.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            placeholder = { Text(context.getString(R.string.password), color = Gray40) },
            modifier = Modifier.weight(0.83f),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = Black,
                unfocusedPlaceholderColor = Gray40,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(5.dp))
        if (password.isNotEmpty()) {
            Image(
                painter = painterResource(id = if (isPasswordVisible) R.drawable.show_password else R.drawable.password_hide),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp, 17.dp)
                    .clickable { isPasswordVisible = !isPasswordVisible }
            )
        }
    }
}*/
