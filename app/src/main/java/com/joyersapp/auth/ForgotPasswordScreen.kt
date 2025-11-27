package com.joyersapp.auth

import android.graphics.Rect
import android.util.Patterns
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.CountryCodePicker
import com.joyersapp.theme.Black
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.R

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun ForgotPasswordScreen(
    onLoginClick: () -> Unit = {},
    onNavigateToResetPassword: (String, String, String, String) -> Unit = { _, _, _, _ -> },
    initialPhoneMode: Boolean = false
) {
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var verificationError by remember { mutableStateOf<String?>(null) }
    var tabError by remember { mutableStateOf<String?>(null) }
    var isPhoneMode by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Email") } // "Email" or "SMS"
    var showVerificationCode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var logoSize by remember { mutableStateOf(155.dp to 59.dp) }
    var view1Height by remember { mutableStateOf(45.dp) }
    var view2Height by remember { mutableStateOf(55.dp) }
    var showGapView by remember { mutableStateOf(false) }
    var selectedCountryCode by remember { mutableStateOf("+1") }
    var selectedCountryNameCode by remember { mutableStateOf("US") }
    val context = LocalContext.current

    // Update isPhoneMode and selectedTab when initialPhoneMode changes
    LaunchedEffect(initialPhoneMode) {
        isPhoneMode = initialPhoneMode
        //selectedTab = if (initialPhoneMode) "SMS" else "Email"
        // Reset form fields when mode changes
        username = ""
        phone = ""
        verificationCode = ""
        usernameError = null
        phoneError = null
        verificationError = null
        tabError = null
        showVerificationCode = false
    }

    // Form validation
    val isFormValid = remember(username, phone, isPhoneMode) {
        if (isPhoneMode) {
            phone.isNotEmpty() && phone.all { it.isDigit() } && phone.length in 10..15
        } else {
            username.isNotEmpty() /*&& Patterns.EMAIL_ADDRESS.matcher(username).matches()*/
        }
    }

    val isVerificationValid = verificationCode.length == 6 && verificationCode.all { it.isDigit() }

    // Function to mask email address
    fun maskEmail(email: String): String {
        if (email.isEmpty() || !email.contains("@")) return email
        val parts = email.split("@")
        val localPart = parts[0]
        val domain = parts[1]

        return when {
            localPart.length <= 2 -> "$localPart*****@$domain"
            else -> {
                val firstChar = localPart[0]
                val lastChar = localPart[localPart.length - 1]
                "$firstChar*****$lastChar@$domain"
            }
        }
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
            view2Height = 30.dp
            showGapView = true
            isKeyboardVisible = true
        } else {
            // Keyboard is hidden
            logoSize = 155.dp to 59.dp
            view1Height = 45.dp
            view2Height = 55.dp
            showGapView = false
            isKeyboardVisible = false
        }
    }

    DisposableEffect(Unit) {
        val activity = context as? ComponentActivity
        val frameLayout = activity?.window?.decorView?.rootView

        if (frameLayout != null) {
            // Initial check
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

    // Handle navigation after successful verification with 1 second delay
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1000) // Show loader for 1 second
            val identifier = if (isPhoneMode) phone else username
            val countryCode = if (isPhoneMode) selectedCountryCode else ""
            val countryNameCode = if (isPhoneMode) selectedCountryNameCode else ""
            onNavigateToResetPassword(identifier, countryCode, countryNameCode, verificationCode)
            isLoading = false
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

            // Forgot password LABEL
            Text(
                text = stringResource(R.string.forgot_password),
                fontSize = 24.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.ExtraBold,
                color = Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Description text
            Text(
                text = if (showVerificationCode) if (isPhoneMode) {
                    stringResource(R.string.number_sent)
                } else {
                    stringResource(R.string.email_sent)
                } else stringResource(R.string.reset_password_in_two_steps),
                fontSize = 16.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
                color = Black
            )

            Spacer(modifier = Modifier.height(if (showVerificationCode) 0.dp else 10.dp))

            // Show message and masked email when verification code is shown
            if (showVerificationCode) {
                /*Spacer(modifier = Modifier.height(20.dp))
                
                // "We have sent a code to your email" message
                Text(
                    text = if (isPhoneMode) {
                        stringResource(R.string.number_sent)
                    } else {
                        stringResource(R.string.email_sent)
                    },
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    color = Black,
                    modifier = Modifier.fillMaxWidth(),
                )*/

                // Masked email/phone display
                Text(
                    text = if (isPhoneMode) {
                        // Mask phone number with country code
                        val fullPhone = "$selectedCountryCode$phone"
                        if (fullPhone.length > 6) {
                            "${fullPhone.take(3)}*****${fullPhone.takeLast(2)}."
                        } else {
                            "$fullPhone."
                        }
                    } else {
                        maskEmail("$username.")
                    },
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

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
                            .background(Gray20, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .border(
                                color = if (usernameError != null) Red else Color.Transparent,
                                width = 1.dp,
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
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

                            //Spacer(modifier = Modifier.width(15.dp))

                            AppBasicTextField(
                                value = username,
                                onValueChange = {
                                    username = it
                                    usernameError = null
                                    if (showVerificationCode) {
                                        showVerificationCode = false
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

                            if (username.isNotEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cancel_grey),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 5.dp, end = 10.dp)
                                        .clickable {
                                            username = ""
                                            if (showVerificationCode) {
                                                showVerificationCode = false
                                            }
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
                                username = ""
                                isPhoneMode = true
                                showVerificationCode = false
                            }
                            .background(Gray20, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
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
                            .background(Gray20, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .border(
                                color = if (phoneError != null) Red else Color.Transparent,
                                width = 1.dp,
                                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
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
                                value = phone,
                                onValueChange = {
                                    if (it.length <= 15) {
                                        phone = it
                                        phoneError = null
                                    }
                                    if (showVerificationCode) {
                                        showVerificationCode = false
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

                            if (phone.isNotEmpty()) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cancel_grey),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(start = 5.dp, end = 10.dp)
                                        .clickable {
                                            phone = ""
                                            if (showVerificationCode) {
                                                showVerificationCode = false
                                            }
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
                                phone = ""
                                isPhoneMode = false
                                showVerificationCode = false
                            }
                            .background(Gray20, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
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
            if (phoneError != null && isPhoneMode) {
                Text(
                    text = phoneError!!,
                    color = Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            if (usernameError != null && !isPhoneMode) {
                Text(
                    text = usernameError!!,
                    color = Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            if (!showVerificationCode) {
                Spacer(modifier = Modifier.height(20.dp))

                // Tab selection section (only shown before verification)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.send_verification_code),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.SemiBold,
                        color = Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tab buttons and underlines - matching XML layout exactly
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Tab buttons container - 165dp wide, centered
                        Row(
                            modifier = Modifier.width(165.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.email),
                                fontSize = 20.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedTab == "Email") Golden60 else Black,
                                modifier = Modifier
                                    .clickable {
                                        selectedTab = "Email"
                                        tabError = null
                                    }
                                    .padding(start = 12.dp, end = 15.dp)
                            )

                            Text(
                                text = stringResource(R.string.sms),
                                fontSize = 20.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = if (selectedTab == "SMS") FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedTab == "SMS") Golden60 else Black,
                                modifier = Modifier
                                    .clickable {
                                        selectedTab = "SMS"
                                        tabError = null
                                    }
                                    .padding(start = 15.dp, end = 13.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.5.dp))

                        // Underline indicators container - 148dp wide, centered, touching
                        Row(
                            modifier = Modifier.width(148.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(if (selectedTab == "Email") 2.dp else 1.dp)
                                    .background(
                                        color = if (selectedTab == "Email") Golden60 else Color(0xFFADADAD)
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(if (selectedTab == "SMS") 2.dp else 1.dp)
                                    .background(
                                        color = if (selectedTab == "SMS") Golden60 else Color(0xFFADADAD)
                                    )
                            )
                        }
                    }

                    if (tabError != null) {
                        Text(
                            text = tabError!!,
                            color = Red,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Verification code input
            if (showVerificationCode) {
                Spacer(modifier = Modifier.height(20.dp))

                AppBasicTextField(
                    value = verificationCode,
                    onValueChange = {
                        if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                            verificationCode = it
                            verificationError = null
                        }
                    },
                    maxLength = 6,
                    isCentered = true,
                    placeholder = stringResource(R.string.enter_verification_code),
                    modifier = Modifier
                        .width(181.dp)
                        .height(40.dp)
                        .align(Alignment.CenterHorizontally),
                    containerColor = Gray20,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                )

                if (verificationError != null) {
                    Text(
                        text = verificationError!!,
                        color = Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            // Next Button
            if (!showVerificationCode) {
                Spacer(modifier = Modifier.height(50.dp))

                Button(
                    onClick = {
                        // Validate and proceed
                        if (!isFormValid) {
                            if (isPhoneMode) {
                                phoneError = context.getString(R.string.invaild_phone)
                            } else {
                                usernameError = context.getString(R.string.invaild_email)
                            }
                        } else if (selectedTab.isEmpty()) {
                            tabError = "Please select a verification method"
                        } else {
                            // Proceed to verification code
                            showVerificationCode = true
                            usernameError = null
                            phoneError = null
                            tabError = null
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Golden60,
                        disabledContainerColor = Golden60,
                        contentColor = White,
                        disabledContentColor = DisabledTextColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Verify and Resend Code Buttons
            if (showVerificationCode) {
                Spacer(modifier = Modifier.height(35.dp))

                Button(
                    onClick = {
                        if (isVerificationValid) {
                            // Verify code - show loader, will navigate after 1 second
                            isLoading = true
                        } else {
                            verificationError = "Please enter a valid 6-digit code"
                        }
                    },
                    enabled = isVerificationValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Golden60,
                        disabledContainerColor = Golden60,
                        contentColor = White,
                        disabledContentColor = DisabledTextColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.verify),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = {
                        // Resend code
                        verificationCode = ""
                        verificationError = null
                        // TODO: Implement resend logic
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Black,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.resend_code),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Already a Joyer? Login
            Text(
                text = stringResource(R.string.already_a_joyer),
                fontSize = 12.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
                color = Gray40,
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
                        .clickable { onLoginClick() }
                        .padding(4.dp),
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
