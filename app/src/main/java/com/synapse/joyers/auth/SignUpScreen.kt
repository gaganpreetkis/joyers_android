package com.synapse.joyers.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synapse.joyers.R
import com.synapse.joyers.common_widgets.CountryCodePicker
import com.synapse.joyers.isValidUsername
import com.synapse.joyers.ui.theme.Black
import com.synapse.joyers.ui.theme.Golden60
import com.synapse.joyers.ui.theme.Gray20
import com.synapse.joyers.ui.theme.Gray40
import com.synapse.joyers.ui.theme.Gray80
import com.synapse.joyers.ui.theme.Red
import com.synapse.joyers.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.text.isNotEmpty
import kotlin.text.replace


@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit = {},
    onLogInClick: () -> Unit = {},
//    isValidUsername: (String) -> Boolean,
) {

    val context = LocalContext.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()
    var username by remember { mutableStateOf("") }
    var showUsernameLoader by remember { mutableStateOf(false) }
    var showUsernameTick by remember { mutableStateOf(false) }
    var showUsernameError by remember { mutableStateOf(false) }
    var showUsernameSuggestions by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var usernameSuggestions by remember { mutableStateOf<List<String>>(emptyList()) }

    var isPhoneMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var verificationError by remember { mutableStateOf<String?>(null) }
    var showVerification by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var showPasswordFields by remember { mutableStateOf(false) }
    var selectedCountryCode by remember { mutableStateOf("+1") }
    var signupError by remember { mutableStateOf<String?>(null) }
    var codeSentMessage by remember { mutableStateOf("") }

    // Debounced username validation
    LaunchedEffect(username) {
        val cleanUsername = username.replace("@", "")
        if (cleanUsername.isEmpty() || cleanUsername == "@") {
            showUsernameLoader = false
            showUsernameTick = false
            showUsernameError = false
            showUsernameSuggestions = false
            usernameSuggestions = emptyList()
        } else if (cleanUsername.length < 3) {
            showUsernameLoader = false
            showUsernameTick = false
            showUsernameError = false
            showUsernameSuggestions = false
        } else {
            showUsernameLoader = true
            delay(400)
            // API call would go here - for now, simulate validation
//            if (isValidUsername(cleanUsername)) {
            if (true) {
                showUsernameLoader = false
                showUsernameTick = true
                showUsernameError = false
                showUsernameSuggestions = false
            } else {
                showUsernameLoader = false
                showUsernameTick = false
                showUsernameError = true
                usernameError = context.getString(R.string.username_must_be_3_15_characters_only_letters_numbers_and_underscores)
            }
        }
    }

    // Form validation
    val isFormValid = remember(username, email, phone, isPhoneMode) {
        val cleanUsername = username.replace("@", "")
        if (isPhoneMode) {
            cleanUsername.isNotEmpty() && isValidUsername(cleanUsername) && phone.isNotEmpty()
        } else {
            cleanUsername.isNotEmpty() && isValidUsername(cleanUsername) && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

//    val isPasswordFormValid = remember(password, confirmPassword) {
//        isValidPassword(password) && password == confirmPassword
//    }

    val isVerificationValid = verificationCode.length == 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 35.dp)
            .statusBarsPadding()
    ) {

        Spacer(modifier = Modifier.height(if (isKeyBoardOpen) 20.dp else 45.dp))

        AppLogo(isKeyBoardOpen, Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(45.dp))

        // SIGNUP LABEL
        Text(
            text = "Sign Up",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // USERNAME INPUT
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = Gray20,
//                    color = if (showUsernameError) Red else Gray20,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = if (showUsernameError) 1.dp else 0.dp,
                    color = Red,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Spacer(Modifier.width(4.dp))
                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        platformStyle = PlatformTextStyle(includeFontPadding = false)
                    ),
                    placeholder = { Text("Username", color = Gray40) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.71f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF1F1F1),
                        focusedContainerColor = Color(0xFFF1F1F1),
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )

            Spacer(modifier = Modifier.weight(0.29f))

            if (showUsernameLoader) {
                // Loader GIF would go here - using placeholder for now
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Golden60
                )
            } else if (showUsernameTick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tick_green),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            } else if (showUsernameError) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel_grey),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(15.dp)
                            .clickable { username = "" }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_reload),
                        contentDescription = null, alignment = Alignment.Center,
                        modifier = Modifier
                            .size(15.dp)
                            .clickable {
                                val cleanUsername = username.replace("@", "")
//                                if (cleanUsername.length >= 3) {
//                                    signupViewModel.checkUserName(UsernameRequest(cleanUsername))
//                                }
                            }
                    )
                }
            }
        }

        if (usernameError != null && showUsernameError) {
            Text(
                text = usernameError!!,
                color = Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        // Username suggestions
        if (showUsernameSuggestions && usernameSuggestions.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                LazyColumn {
                    items(usernameSuggestions.size) { index ->
                        Text(
                            text = "@${usernameSuggestions[index]}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    username = "@${usernameSuggestions[index]}"
                                    showUsernameSuggestions = false
                                    showUsernameTick = true
                                    showUsernameError = false
                                }
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                            fontSize = 16.sp,
                            color = Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // EMAIL

        var isEmailSelected by remember { mutableStateOf(true) }
        val toggleIcon = if (isEmailSelected) R.drawable.telephone_icon_golden else R.drawable.ic_mail_golden
        val contactIcon = if (isEmailSelected) R.drawable.ic_mail else R.drawable.ic_telephone_gray
        val contactPlaceHolder = if (isEmailSelected) "Email" else "Phone Number"

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            // LEFT PART
            Row(
                modifier = Modifier
                    .weight(0.85f)
                    .fillMaxHeight()
                    .background(Gray20, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {



                if (isEmailSelected) {
                    Image(
                        painter = painterResource(id = contactIcon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(Gray80)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text(contactPlaceHolder, color = Gray40) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F1F1),
                            focusedContainerColor = Color(0xFFF1F1F1),
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(Modifier.width(50.dp))

                    if (email.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cancel_grey),
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                                .clickable {
                                    email = ""
                                    showVerification = false
                                    showPasswordFields = false
                                }
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                } else {
                    Image(
                        painter = painterResource(id = contactIcon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(Gray80)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                        CountryCodePicker { code ->
                            selectedCountryCode = code
                        }

                    TextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text(contactPlaceHolder, color = Gray40) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.71f),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF1F1F1),
                            focusedContainerColor = Color(0xFFF1F1F1),
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )

                    Spacer(Modifier.weight(0.29f))

                    if (phone.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cancel_grey),
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                                .clickable {
                                    email = ""
                                    showVerification = false
                                    showPasswordFields = false
                                }
                        )
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }

            Spacer(Modifier.width(5.dp))

                    // RIGHT TOGGLE ICON PART
                    Box(
                        modifier = Modifier
                            .clickable {
                                isEmailSelected = !isEmailSelected
                                isPhoneMode = false
                                email = ""
                                phone = ""
                                showVerification = false
                                showPasswordFields = false
                            }
                            .weight(0.15f)
                            .fillMaxHeight()
                            .background(Gray20, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = toggleIcon),
                            contentDescription = "Toggle",
                            modifier = Modifier.size(22.dp),
                            colorFilter = ColorFilter.tint(Golden60)
                )
            }
        }

        // Error messages
        if (emailError != null && !isPhoneMode) {
            Text(
                text = emailError!!,
                color = Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
        if (phoneError != null && isPhoneMode) {
            Text(
                text = phoneError!!,
                color = Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        // Verification Code Input
        if (showVerification) {
            Spacer(modifier = Modifier.height(20.dp))

            if (codeSentMessage.isNotEmpty()) {
                Text(
                    text = codeSentMessage,
                    fontSize = 16.sp,
                    color = Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            TextField(
                value = verificationCode,
                onValueChange = {
                    if (it.length <= 6) {
                        verificationCode = it
                        verificationError = null
                    }
                },
                placeholder = { Text(context.getString(R.string.enter_verification_code), color = Gray40) },
                modifier = Modifier
                    .width(181.dp)
                    .align(Alignment.CenterHorizontally),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = Black,
                    unfocusedPlaceholderColor = Gray40,
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontWeight = if (verificationCode.isNotEmpty()) FontWeight.SemiBold else FontWeight.Normal,
                    letterSpacing = if (verificationCode.isNotEmpty()) 0.2.sp else 0.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                singleLine = true
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

            Spacer(modifier = Modifier.height(15.dp))

            // Verify Button
            Button(
                onClick = {

                },
                enabled = isVerificationValid,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Golden60,
                    disabledContainerColor = Color(0xFFD3D3D3),
                    contentColor = White,
                    disabledContentColor = colorResource(id = R.color.color_button_login_alt)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = context.getString(R.string.verify),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Resend Code Button
            Button(
                onClick = {
                    verificationCode = ""
                    verificationError = null
//                    if (isPhoneMode) {
//                        signupViewModel.checkUserEmail(
//                            VerifyEmailRequest(
//                                mobile = phone,
//                                country_code = selectedCountryCode
//                            )
//                        )
//                    } else {
//                        signupViewModel.checkUserEmail(
//                            VerifyEmailRequest(email = email)
//                        )
//                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = context.getString(R.string.resend_code),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }


        Spacer(modifier = Modifier.height(if (isKeyBoardOpen) 45.dp else 71.dp))

        // SIGN UP BUTTON
        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color(0xFFD4A038), shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD4A038)
            )
        ) {
            Text("Sign Up", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(50.dp))

        // SIGNUP FOOTER
        Text(
            text = "Already a Joyer?",
            fontSize = 12.sp,
            color = Color(0xFF9A9A9A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Login",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFD4A038),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onLogInClick() }
                .padding(top = 4.dp, bottom = 20.dp)
        )
    }

}

@Composable
fun AppLogo(isKeyBoardOpen: Boolean, modifier: Modifier) {

    // LOGO
    val width = if (isKeyBoardOpen) 125.dp else 155.dp
    val height = if (isKeyBoardOpen) 48.dp else 59.dp
    Image(
        painter = painterResource(id = R.drawable.joyer_logo),
        contentDescription = "Joyers Logo",
        modifier = modifier.size(width, height)
    )

}

@Composable
fun rememberIsKeyboardOpen(): Boolean {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    var isKeyboardOpen by remember { mutableStateOf(imeBottom > 0) }

    LaunchedEffect(imeBottom) {
        isKeyboardOpen = imeBottom > 0
    }
    return isKeyboardOpen
}