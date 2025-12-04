package com.joyersapp.auth.presentation

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isValidPassword
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.res.colorResource
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.AppBasicTextFieldForPassword
import com.joyersapp.common_widgets.AutoResizeText
import com.joyersapp.common_widgets.CountryCodePicker
import com.joyersapp.theme.Green
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun ResetPasswordScreen(
    identifier: String = "",
    countryCode: String = "",
    countryNameCode: String = "",
    verificationCode: String = "",
    onLoginClick: () -> Unit = {},
    onVerifyClick: (String, String) -> Unit = { _, _ -> },
    onBackToForgotPassword: (Boolean) -> Unit = { _ -> }
) {
    var isPhoneMode by remember { mutableStateOf(countryCode.isNotEmpty()) }

    var identifierValue by remember { mutableStateOf(identifier) }
    var selectedCountryCode by remember { mutableStateOf(countryCode) }
    var selectedCountryNameCode by remember { mutableStateOf(countryNameCode.ifEmpty { "US" }) }
    var identifierError by remember { mutableStateOf<String?>(null) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isConfirmPasswordFocused by remember { mutableStateOf(false) }
    var isPasswordResetSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var logoSize by remember { mutableStateOf(155.dp to 59.dp) }
    var view1Height by remember { mutableStateOf(45.dp) }
    var view2Height by remember { mutableStateOf(45.dp) }
    var showGapView by remember { mutableStateOf(false) }
    var showPasswordStrength by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Validation
    val isPasswordValid = remember(password) {
        isValidPassword(password)
    }

    val isFormValid = remember(password, confirmPassword) {
        isValidPassword(password) &&
                password == confirmPassword &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty()
    }

    fun updateKeyboardState(frameLayout: View) {
        val rect = Rect()
        frameLayout.getWindowVisibleDisplayFrame(rect)
        val screenHeight = frameLayout.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight > screenHeight * 0.15) {
            // Keyboard is visible
            logoSize = 125.dp to 48.dp
            view1Height = 20.dp
            view2Height = 20.dp
            showGapView = true
            isKeyboardVisible = true
        } else {
            // Keyboard is hidden
            logoSize = 155.dp to 59.dp
            view1Height = 45.dp
            view2Height = 45.dp
            showGapView = false
            isKeyboardVisible = false
        }
    }

    DisposableEffect(Unit) {
        val activity = context as? ComponentActivity
        val frameLayout = activity?.window?.decorView?.rootView

        if (frameLayout != null) {
            updateKeyboardState(frameLayout)

            val listener = ViewTreeObserver.OnGlobalLayoutListener { updateKeyboardState(frameLayout) }
            frameLayout.viewTreeObserver.addOnGlobalLayoutListener(listener)

            onDispose {
                frameLayout.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
        } else {
            onDispose { }
        }
    }

    // Handle password focus changes - show/hide strength indicator
    LaunchedEffect(isPasswordFocused, password) {
        if (!isPasswordResetSuccess) {
            if (isPasswordFocused && password.isNotEmpty()) {
                showPasswordStrength = isValidPassword(password)
            } else if (!isPasswordFocused) {
                showPasswordStrength = false
                passwordError = if (password.isNotEmpty() && !isValidPassword(password)) {
                    context.getString(R.string.weak_password)
                } else {
                    null
                }
            }
        }
    }

    // Handle confirm password focus changes
    LaunchedEffect(isConfirmPasswordFocused, confirmPassword, password, passwordError) {
        if (!isPasswordResetSuccess) {
            if (passwordError != null) {
                confirmPasswordError = null
            } else if (!isConfirmPasswordFocused && confirmPassword.isNotEmpty() && password.isNotEmpty()) {
                confirmPasswordError = if (password != confirmPassword) {
                    context.getString(R.string.password_does_not_match)
                } else {
                    null
                }
            } else if (isConfirmPasswordFocused) {
                confirmPasswordError = null
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 35.dp)
                .statusBarsPadding()
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(view1Height))

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.joyer_logo),
                contentDescription = stringResource(R.string.joyers),
                modifier = Modifier
                    .size(logoSize.first, logoSize.second)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(view2Height))

            // Reset Password LABEL
            Text(
                text = stringResource(R.string.reset_password),
                fontSize = 18.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.SemiBold,
                color = LightBlack
            )

            Spacer(modifier = Modifier.height(15.dp))

            // USERNAME / EMAIL BOX (LEFT BIG + RIGHT ICON)
            if (!isPhoneMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    // LEFT PART
                    Box(
                        modifier = Modifier
                            .weight(0.85f)
                            .height(50.dp)
                            .background(Gray20, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                            .border(
                                color = if (identifierError != null) Red else colorResource(id = R.color.color_border_light),
                                width = 1.dp,
                                shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                            ),
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.user_icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )

                            AppBasicTextField(
                                value = identifierValue,
                                onValueChange = {
                                    if (identifierValue != it) {
                                        onBackToForgotPassword(isPhoneMode)
                                    }
                                },
                                maxLength = 100,
                                placeholder = stringResource(R.string.username_email),
                                modifier = Modifier.weight(1f),
                                containerColor = Color.Transparent,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email
                                ),
                            )

                            if (identifierValue.isNotEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cancel_grey),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 5.dp, end = 10.dp)
                                        .clickable {
                                            onBackToForgotPassword(isPhoneMode)
                                        }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    // RIGHT PHONE ICON PART
                    Box(
                        modifier = Modifier
                            .weight(0.15f)
                            .fillMaxHeight()
                            .clickable {
                                onBackToForgotPassword(true)
                            }
                            .background(Gray20, RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                            .border(
                                color = colorResource(id = R.color.color_border_light),
                                width = 1.dp,
                                shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.telephone_icon_golden),
                            contentDescription = "Toggle",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Golden60)
                        )
                    }
                }
            }

            // PHONE INPUT FIELD
            if (isPhoneMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    // LEFT PART
                    Box(
                        modifier = Modifier
                            .weight(0.85f)
                            .background(Gray20, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                            .border(
                                color = if (identifierError != null) Red else colorResource(id = R.color.color_border_light),
                                width = 1.dp,
                                shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                            ),
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_telephone_gray),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(7.dp))

                            CountryCodePicker(
                                defaultCountry = selectedCountryNameCode,
                                onCountrySelected = { code ->
                                    selectedCountryCode = code
                                },
                                onCountryNameCodeSelected = { nameCode ->
                                    selectedCountryNameCode = nameCode
                                }
                            )

                            AppBasicTextField(
                                value = identifierValue,
                                onValueChange = {
                                    if (identifierValue != it) {
                                        onBackToForgotPassword(isPhoneMode)
                                    }
                                },
                                maxLength = 15,
                                placeholder = stringResource(R.string.phone_number),
                                modifier = Modifier.weight(1f),
                                containerColor = Color.Transparent,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone
                                ),
                            )

                            if (identifierValue.isNotEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cancel_grey),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 5.dp, end = 10.dp)
                                        .clickable {
                                            onBackToForgotPassword(isPhoneMode)
                                        }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    // RIGHT USER ICON PART
                    Box(
                        modifier = Modifier
                            .weight(0.15f)
                            .fillMaxHeight()
                            .clickable {
                                onBackToForgotPassword(false)
                            }
                            .background(Gray20, RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                            .border(
                                color = colorResource(id = R.color.color_border_light),
                                width = 1.dp,
                                shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_icon_golden),
                            contentDescription = "Toggle",
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Golden60)
                        )
                    }
                }
            }

            // Error messages
            if (identifierError != null) {
                Text(
                    text = identifierError!!,
                    color = Red,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = fontFamilyLato,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Password Input Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        color = if (passwordError != null) Red else colorResource(id = R.color.color_border_light),
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(Gray20, RoundedCornerShape(5.dp))
            ) {
                Row(
                    modifier = Modifier.padding(start = 19.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.password_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(0.dp))

                        AppBasicTextFieldForPassword(
                            value = password,
                            onValueChange = {
                                password = it.take(16)
                                passwordError = null
                                if (isPasswordFocused && it.isNotEmpty()) {
                                    showPasswordStrength = isValidPassword(it)
                                }
                                if (confirmPassword.isNotEmpty()) {
                                    if (password == confirmPassword) {
                                        confirmPasswordError = null
                                    } else {
                                        confirmPasswordError = context.getString(R.string.password_does_not_match)
                                    }
                                }
                            },
                            maxLength = 16,
                            isEnabled = !isPasswordResetSuccess,
                            placeholder = stringResource(R.string.password),
                            isPassword = true,
                            passwordVisible = isPasswordVisible,
                            onPasswordToggle = {
                                isPasswordVisible = !isPasswordVisible
                                //passwordError = null

                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .onFocusChanged { focusState ->
                                    isPasswordFocused = focusState.isFocused
                                },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }
                }
            }

            if (passwordError != null) {
                Text(
                    text = passwordError!!,
                    color = Red,
                    fontSize = 14.sp,
                    fontFamily = fontFamilyLato,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            // Password strength indicator
            if (showPasswordStrength && isPasswordFocused && password.isNotEmpty() && confirmPasswordError == null) {
                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.strong),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.SemiBold,
                        color = Green,
                        modifier = Modifier.padding()
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                LinearProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = Green,
                    trackColor = Gray20
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Confirm Password Input Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        color = if (confirmPasswordError != null) Red else colorResource(id = R.color.color_border_light),
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(Gray20, RoundedCornerShape(5.dp))
            ) {
                Row(
                    modifier = Modifier.padding(start = 19.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.password_icon),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(0.dp))

                        AppBasicTextFieldForPassword(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it.take(16)
                                confirmPasswordError = null
                                isPasswordFocused = false
                                isConfirmPasswordFocused = true
                            },
                            maxLength = 16,
                            isEnabled = !isPasswordResetSuccess,
                            placeholder = stringResource(R.string.confirm_password),
                            isPassword = true,
                            passwordVisible = isConfirmPasswordVisible,
                            onPasswordToggle = {
                                isConfirmPasswordVisible = !isConfirmPasswordVisible
                                confirmPasswordError = null
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .onFocusChanged { focusState ->
                                    isConfirmPasswordFocused = focusState.isFocused
                                },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                        )
                    }
                }
            }

            if (confirmPasswordError != null) {
                Text(
                    text = confirmPasswordError!!,
                    color = Red,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = fontFamilyLato,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            // Success message
            if (isPasswordResetSuccess) {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.checkbox_checked),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(7.dp))

                        /*Text(
                            text = context.getString(R.string.your_password_has_been_successfully_reset),
                            fontSize = textSize,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = Black,
                            maxLines = 1,
                            overflow = TextOverflow.Clip,
                            modifier = Modifier.fillMaxWidth(),
                            onTextLayout = { layoutResult ->
                                // Auto-size: if text overflows, reduce size (min 12sp, max 16sp, step 1sp)
                                if (layoutResult.didOverflowWidth && textSize.value > 6f) {
                                    val newSize = (textSize.value - 1f).coerceAtLeast(6f)
                                    textSize = newSize.sp
                                } else if (!layoutResult.didOverflowWidth && textSize.value < 16f) {
                                    // If text fits and we can increase, do so (up to 16sp)
                                    val newSize = (textSize.value + 1f).coerceAtMost(16f)
                                    textSize = newSize.sp
                                }
                            }
                        )*/
                        AutoResizeText(
                            text = context.getString(R.string.your_password_has_been_successfully_reset),
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Verify Button
            Button(
                onClick = {
                    if (isPasswordResetSuccess) {
                        // Navigate to login
                        onLoginClick()
                    } else {
                        // Verify password reset
                        if (isFormValid) {
                            // Clear any errors before showing loader
                            passwordError = null
                            confirmPasswordError = null
                            isLoading = true
                            onVerifyClick(password, confirmPassword)
                            // Simulate API call
                            coroutineScope.launch {
                                delay(1000)
                                isLoading = false
                                // Clear errors and reset focus states when success
                                passwordError = null
                                confirmPasswordError = null
                                isPasswordFocused = false
                                isConfirmPasswordFocused = false
                                showPasswordStrength = false
                                isPasswordResetSuccess = true
                            }
                        }
                    }
                },
                enabled = if (isPasswordResetSuccess) true else isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Golden60,
                    disabledContainerColor = Golden60,
                    contentColor = White,
                    disabledContentColor = DisabledTextColor
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = if (isPasswordResetSuccess) {
                        context.getString(R.string.get_started)
                    } else {
                        stringResource(R.string.verify)
                    },
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Already a Joyer? Login
            Text(
                text = stringResource(R.string.already_a_joyer),
                fontSize = 12.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
                color = LightBlack60,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.login),
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    color = Golden60,
                    modifier = Modifier
                        .clickable { onLoginClick() },
                    textAlign = TextAlign.Center
                )
            }

            if (showGapView) {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Progress bar overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Golden60,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
