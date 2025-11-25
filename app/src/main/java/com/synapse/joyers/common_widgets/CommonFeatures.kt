package com.synapse.joyers.common_widgets

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.hbb20.CountryCodePicker
import com.synapse.joyers.R
import com.synapse.joyers.ui.theme.Black
import com.synapse.joyers.ui.theme.Gray40
import com.synapse.joyers.ui.theme.Red
import com.synapse.joyers.utils.fontFamilyLato


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

fun showCCPDialog(context: Context, showPhoneCode: Boolean, onCodeSelected: (String, String, ImageView,Int) -> Unit) {
    val ccp = CountryCodePicker(context)

    // Optional: configure default country if needed
    ccp.ccpDialogShowFlag = false
    ccp.ccpDialogShowNameCode = false
    ccp.ccpDialogShowTitle = true
    ccp.setCcpDialogShowPhoneCode(showPhoneCode)

    ccp.showFullName(true)

    // Listener to capture the selected country code
    ccp.setOnCountryChangeListener {
        val code = ccp.selectedCountryCodeWithPlus
        val name = ccp.selectedCountryName
        val flag = ccp.imageViewFlag
        val flagId = ccp.selectedCountryFlagResourceId

        onCodeSelected(code, name, flag, flagId)
    }

    // Trigger the CCP dialog
    ccp.launchCountrySelectionDialog()
}


@Composable
fun CountryCodePicker(
    defaultCountry: String = "US",
    onCountrySelected: (String) -> Unit = {},
    onCountryNameCodeSelected: ((String) -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        AndroidView(
            modifier = Modifier.wrapContentHeight(),
            factory = { context ->
                CountryCodePicker(context).apply {
                    setDefaultCountryUsingNameCode(defaultCountry)
                    setCountryForNameCode(defaultCountry)
                    setAutoDetectedCountry(false)
                    showFullName(false)
                    showNameCode(false)
                    setShowPhoneCode(true)
                    showFlag(true)
                    setOnCountryChangeListener {
                        onCountrySelected(selectedCountryCodeWithPlus)
                        onCountryNameCodeSelected?.invoke(selectedCountryNameCode)
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
            color = Gray40,
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
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun myTextField(
    value: String = "",
    onValueChange: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
        ),
        placeholder = { Text("@username",
            modifier = Modifier.fillMaxWidth(),
            color = Gray40,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
            )) },
        modifier = modifier
            .fillMaxWidth(),
//            .focusRequester(focusRequester)
//            .onFocusChanged { focusState ->
//                isUsernameFocused = focusState.isFocused
//            },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String = "tesst",
    onValueChange: (String) -> Unit = {},
    label: String = "@test",
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int? = null
) {
    TextField(
        value = value,
        onValueChange = { newValue ->
            if (maxLength == null || newValue.length <= maxLength) {
                onValueChange(newValue)
            }
        },
        placeholder = { Text("@username",
            modifier = Modifier.fillMaxWidth(),
            color = Gray40,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
            )) },
        label = { Text(text = label) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "$label icon",
                    tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            null
        },
        trailingIcon = trailingIcon,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            } else if (maxLength != null) {
                Text(
                    text = "${value.length} / $maxLength",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },
//        colors = TextFieldDefaults.outlinedTextFieldColors(
//            focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
//            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
//        )
    )
}