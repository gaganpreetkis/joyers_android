package com.joyersapp.auth.presentation.signup

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.AppBasicTextFieldForLetterSpacing
import com.joyersapp.common_widgets.AppBasicTextFieldForPassword
import com.joyersapp.common_widgets.AppBasicTextFieldWithCursorHandling
import com.joyersapp.common_widgets.CountryCodePicker
import com.joyersapp.theme.Black
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray80
import com.joyersapp.theme.GrayBorder
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isValidPassword
import com.joyersapp.utils.isValidUsername
import com.joyersapp.utils.rememberIsKeyboardOpen
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
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showUsernameLoader by remember { mutableStateOf(false) }
    var showUsernameTick by remember { mutableStateOf(false) }
    var showUsernameError by remember { mutableStateOf(false) }
    var showUsernameSuggestions by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var usernameSuggestions by remember {
        mutableStateOf<List<String>>(
            arrayListOf(
                "test123",
                "test256",
                "test478",
            )
        )
    }

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
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var selectedCountryCode by remember { mutableStateOf("+1") }
    var signupError by remember { mutableStateOf<String?>(null) }
    var codeSentMessage by remember { mutableStateOf("") }
    var signInButtonText by remember { mutableStateOf(context.getString(R.string.sign_up)) }
    val focusRequester = remember { FocusRequester() }
    var isPasswordFocused by remember { mutableStateOf(false) }
    var isConfirmPasswordFocused by remember { mutableStateOf(false) }
    var isUsernameFocused by remember { mutableStateOf(true) }
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
    }
//    tempp====================
    var isSuggestionSelected by remember { mutableStateOf(false) }


    // Debounced username validation
    LaunchedEffect(username) {
        val cleanUsername = username.text.replace("@", "")

        if (cleanUsername.isEmpty() || cleanUsername == "@") {
            showUsernameLoader = false
            showUsernameTick = false
            showUsernameError = false
            showUsernameSuggestions = false
//            temp=======================================
//            usernameSuggestions = emptyList()
        } else if (cleanUsername.length < 3) {

            if (isUsernameFocused) {
                showUsernameLoader = false
                showUsernameTick = false
                showUsernameError = false
                showUsernameSuggestions = false
            } else {
                showUsernameLoader = false
                showUsernameTick = false
                usernameError = context.getString(R.string.username_must_be_3_15_characters_only_letters_numbers_and_underscores)
                showUsernameError = true
                showUsernameSuggestions = false
            }

        } else {
            //        temp=====================
            if (cleanUsername.length >= 6 && !isSuggestionSelected) {
                showUsernameLoader = false
                showUsernameSuggestions = true
                showUsernameTick = false
                usernameError = context.getString(R.string.username_is_already_taken)
                showUsernameError = true
            } else {
                showUsernameLoader = true
                delay(400)

                // API call would go here - for now, simulate validation  ====temp=====
                if (isValidUsername(cleanUsername)) {
//            if (true) {
                    showUsernameLoader = false
                    showUsernameTick = true
                    showUsernameError = false
                    usernameError = null
                    showUsernameSuggestions = false
                } else {
                    showUsernameLoader = false
                    showUsernameTick = false
                    showUsernameError = true
                    usernameError = context.getString(R.string.username_must_be_3_15_characters_only_letters_numbers_and_underscores)
                    showUsernameSuggestions = false
                }
            }
        }
    }


    val cleanUsername = username.text.replace("@", "")
    if (!isUsernameFocused && username.text.replace("@", "").isNotEmpty() && !isValidUsername(cleanUsername)) {
        showUsernameLoader = false
        usernameError = context.getString(R.string.username_must_be_3_15_characters_only_letters_numbers_and_underscores)
        showUsernameError = true
        showUsernameSuggestions = false
    }

    if (!isPasswordFocused) {
        passwordError = if (password.isNotEmpty() && !isValidPassword(password)) {
            context.getString(R.string.weak_password)
        } else {
            null
        }
    }

    if (passwordError != null) {
        confirmPasswordError = null
    } else if (!isConfirmPasswordFocused) {
        confirmPasswordError = if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            context.getString(R.string.password_does_not_match)
        } else {
            null
        }
    }

    // Form validation
    val isFormValid = remember(username, email, phone, isPhoneMode) {
        val cleanUsername = username.text.replace("@", "")
        if (isPhoneMode) {
            cleanUsername.isNotEmpty() && isValidUsername(cleanUsername) && phone.isNotEmpty() &&
                    phone.all { it.isDigit() } &&
                    phone.length in 10..15
        } else {
            cleanUsername.isNotEmpty() && isValidUsername(cleanUsername) && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    val isPasswordFormValid = remember(password, confirmPassword) {
        isValidPassword(password)
                && password == confirmPassword
    }

    val isPasswordValid = remember(password) {
        password.length >= 8 &&
                password.any { it.isUpperCase() } &&
                password.any { !it.isLetterOrDigit() }
    }

    val isVerificationValid = verificationCode.length == 6

    if (isFormValid && !showPasswordFields) {
        signInButtonText = context.getString(R.string.next)
    } else {
        signInButtonText = context.getString(R.string.sign_up)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 35.dp)
            .statusBarsPadding()
            .imePadding()
    ) {

        Spacer(modifier = Modifier.height(if (isKeyBoardOpen) 20.dp else 45.dp))

        AppLogo(isKeyBoardOpen, Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(45.dp))

// SIGNUP LABEL
        Text(
            text = "Sign Up",
            fontSize = 18.sp,
            fontFamily = fontFamilyLato,
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
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (showUsernameError) Red else GrayLightBorder,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(0.dp))

            AppBasicTextFieldWithCursorHandling(
                value = username,
                onValueChange = {
                    showVerification = false
                    showPasswordFields = false
                    var updatedText = ""
                    if (username.text.length <= 15) {
                        updatedText =
                            if (it.text.isEmpty() || it.text.get(0).toString().equals("@")) {
                                it.text
                            } else {
                                "@${it.text}"
                            }

                    } else {
                        updatedText = it.text.dropLast(1)
                    }
                    username = TextFieldValue(
                        text = updatedText,
                        selection = TextRange(
                            start = updatedText.length,
                            end = updatedText.length
                        )
                    )

                    if (isSuggestionSelected) {
                        isSuggestionSelected = false
                    }
                },
                maxLength = 16,
                isEnabled = !showPasswordFields,
                placeholder = "@username",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.71f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isUsernameFocused = focusState.isFocused
                    },
                containerColor = Color.Transparent,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.weight(0.29f))

            if (showUsernameLoader) {
                // Loader GIF would go here - using placeholder for now
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.gif_loader_username)
                        .decoderFactory(GifDecoder.Factory())
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Splash GIF",
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.FillBounds,
                    onSuccess = {
                        // Mark GIF as loaded, then start the delay
                        //gifLoaded = true
                    },
                    onError = {
                        // On error, still proceed after delay
//                        gifLoaded = true
                    }
                )
            } else if (showUsernameTick && isUsernameFocused) {
                Image(
                    painter = painterResource(id = R.drawable.ic_tick_green),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            } else if (showUsernameError) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel_grey),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(start = 5.dp, end = 10.dp)
                            .clickable { username = TextFieldValue(text = "") }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_reload),
                        contentDescription = null, alignment = Alignment.Center,
                        modifier = Modifier
                            .size(15.dp)
                            .clickable {
                                val cleanUsername = username.text.replace("@", "")
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
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box() {
            Column() {
// EMAIL
                val toggleIcon = if (!isPhoneMode) R.drawable.telephone_icon_golden else R.drawable.ic_mail_golden
                val contactIcon = if (!isPhoneMode) R.drawable.ic_mail else R.drawable.ic_telephone_gray
                val contactPlaceHolder = if (!isPhoneMode) "Email" else "Phone Number"

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    // LEFT PART
                    Box(
                        modifier = Modifier
                            .weight(0.85f)
                            .fillMaxHeight()
                            .background(Gray20, RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                            .border(
                                color = if ((emailError != null && !isPhoneMode) || (phoneError != null && isPhoneMode)) Red else GrayLightBorder,
                                width = 1.dp,
                                shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isPhoneMode) {
                                Image(
                                    painter = painterResource(id = contactIcon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Gray80)
                                )

                                Spacer(modifier = Modifier.width(0.dp))

                                AppBasicTextField(
                                    value = email,
                                    onValueChange = {
                                        if (email != it) {
                                            email = it
                                            showVerification = false
                                            showPasswordFields = false
                                        }
                                    },
                                    maxLength = 100,
                                    placeholder = contactPlaceHolder,
                                    modifier = Modifier.weight(1f),
                                    containerColor = Color.Transparent,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                )

                                if (email.isNotEmpty()) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_cancel_grey),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(start = 5.dp, end = 10.dp)
                                            .clickable {
                                                email = ""
                                                showVerification = false
                                                showPasswordFields = false
                                            }
                                    )
                                }
                            } else {
                                Image(
                                    painter = painterResource(id = contactIcon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    colorFilter = ColorFilter.tint(Gray80)
                                )

                                Spacer(modifier = Modifier.width(7.dp))

                                CountryCodePicker { code ->
                                    selectedCountryCode = code
                                }

                                AppBasicTextField(
                                    value = phone,
                                    onValueChange = {
                                        if (phone != it) {
                                            phone = it
                                            showVerification = false
                                            showPasswordFields = false
                                        }
                                    },
                                    maxLength = 15,
                                    placeholder = contactPlaceHolder,
                                    modifier = Modifier.weight(1f),
                                    containerColor = Color.Transparent,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                                                showVerification = false
                                                showPasswordFields = false
                                            }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.width(5.dp))

                    // RIGHT TOGGLE ICON PART
                    Box(
                        modifier = Modifier
                            .clickable {
                                isPhoneMode = !isPhoneMode
                                email = ""
                                phone = ""
                                showVerification = false
                                showPasswordFields = false
                            }
                            .weight(0.15f)
                            .fillMaxHeight()
                            .background(Gray20, RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                            .border(
                                color = GrayLightBorder,
                                width = 1.dp,
                                shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = toggleIcon),
                            contentDescription = "Toggle",
                            modifier = Modifier.size(24.dp),
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
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
                if (phoneError != null && isPhoneMode) {
                    Text(
                        text = phoneError!!,
                        color = Red,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }

// Verification Code Input
                if (showVerification) {
                    Spacer(modifier = Modifier.height(20.dp))

                    AppBasicTextFieldForLetterSpacing(
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
                        letterSpacing = 4.sp,
                    )

                    Spacer(Modifier.height(4.dp))

                    if (codeSentMessage.isNotEmpty() && verificationError == null) {
                        Text(
                            text = codeSentMessage,
                            fontSize = 14.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = Black, textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .fillMaxWidth()
                        )
                    }

                    if (verificationError != null) {
                        Text(
                            text = verificationError!!,
                            color = Red,
                            fontSize = 14.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .padding(top = 3.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

// Verify Button
                    Button(
                        onClick = {
                            verificationError = null
                            showVerification = false
                            showPasswordFields = true
                        },
                        enabled = isVerificationValid,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Golden60,
                            disabledContainerColor = Golden60,
                            contentColor = White,
                            disabledContentColor = DisabledTextColor
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.verify),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
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
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.resend_code),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
                            color = White,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                } else {
                    verificationCode = ""
                }

// Password Fields
                if (showPasswordFields) {
                    Spacer(modifier = Modifier.height(10.dp))


                    // Password Input
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                color = if (passwordError != null) Red else GrayLightBorder,
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
                                        password = it
                                        passwordError = null
                                        if (confirmPassword.isNotEmpty()) {
                                            if (password == confirmPassword) {
                                                confirmPasswordError = null
                                            } else {
                                                confirmPasswordError = context.getString(R.string.password_does_not_match)
                                            }
                                        }
                                    },
                                    maxLength = 16,
                                    placeholder = stringResource(R.string.password),
                                    isPassword = true,
                                    passwordVisible = isPasswordVisible,
                                    onPasswordToggle = {
                                        isPasswordVisible = !isPasswordVisible
                                        //passwordError = null
                                    },
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .focusRequester(focusRequester)
                                        .onFocusChanged { focusState ->
                                            isPasswordFocused = focusState.isFocused
                                        },
                                    containerColor = Color.Transparent,
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
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }

                    // Password strength indicator
                    if (password.isNotEmpty() && isValidPassword(password) && isPasswordFocused && confirmPasswordError == null) {

                        Text(
                            text = context.getString(R.string.strong),
                            fontSize = 14.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = Color.Green,
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 5.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .background(
                                        color = Color.Green,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Confirm Password Input
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                color = if (confirmPasswordError != null) Red else GrayLightBorder,
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
                                        confirmPassword = it
                                        confirmPasswordError = null
                                        isPasswordFocused = false
                                        isConfirmPasswordFocused = true
                                    },
                                    maxLength = 16,
                                    placeholder = stringResource(R.string.confirm_password),
                                    isPassword = true,
                                    passwordVisible = isConfirmPasswordVisible,
                                    onPasswordToggle = {
                                        isConfirmPasswordVisible = !isConfirmPasswordVisible
                                        //confirmPasswordError = null
                                    },
                                    modifier = Modifier.fillMaxSize()
                                        .focusRequester(focusRequester)
                                        .onFocusChanged { focusState ->
                                            isConfirmPasswordFocused = focusState.isFocused
                                        },
                                    containerColor = Color.Transparent,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
                                )
                            }
                        }
                    }


                    if (confirmPasswordError != null) {
                        Text(
                            text = confirmPasswordError!!,
                            color = Red,
                            fontSize = 14.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                } else {
                    password = ""
                    confirmPassword = ""
                    passwordError = null
                    confirmPasswordError = null
                }

                Spacer(modifier = Modifier.height(if (isKeyBoardOpen) 45.dp else 71.dp))

// Sign Up  && Verification Button
                if (!showVerification) {
                    Button(
                        onClick = {
//                            onSignUpClick()
                            if (showPasswordFields) {

                                onSignUpClick()
//                    signupViewModel.signup(registerRequest)
//                                Toast.makeText(context, "Signed In", Toast.LENGTH_SHORT).show()
                            } else {
                                // Next step - verify email/phone
                                if (isPhoneMode) {
                                    if (phone.length >= 6) {
                                        showVerification = true
                                        codeSentMessage = if (isPhoneMode) {
                                            context.getString(R.string.code_sent_to_phone)
                                        } else {
                                            context.getString(R.string.code_sent_to_email)
                                        }
                                        emailError = null
                                        phoneError = null
//                            signupViewModel.checkUserEmail(
//                                VerifyEmailRequest(
//                                    mobile = phone,
//                                    country_code = selectedCountryCode
//                                )
//                            )
                                    } else {
                                        phoneError = context.getString(R.string.invaild_phone)
                                    }
                                } else {
                                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                        showVerification = true
                                        codeSentMessage = if (isPhoneMode) {
                                            context.getString(R.string.code_sent_to_phone)
                                        } else {
                                            context.getString(R.string.code_sent_to_email)
                                        }
                                        emailError = null
                                        phoneError = null
//                            signupViewModel.checkUserEmail(VerifyEmailRequest(email = email))
                                    } else {
                                        emailError = context.getString(R.string.invaild_email)
                                    }
                                }
                            }
                        },
                        enabled = if (showPasswordFields) isPasswordFormValid else isFormValid,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Golden60,
                            disabledContainerColor = Golden60,
                            contentColor = White,
                            disabledContentColor = DisabledTextColor
                        ),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = signInButtonText,
                            fontSize = 16.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            /*modifier = Modifier.padding(vertical = 12.dp)*/
                        )
                    }

                }

                Spacer(modifier = Modifier.height(50.dp))

// SIGNUP FOOTER
                Text(
                    text = "Already a Joyer?",
                    fontSize = 12.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9A9A9A),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fontFamilyLato,
                    color = Golden60,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onLogInClick() }
                        .padding(0.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

            }

// Username suggestions
            if (showUsernameSuggestions) {
//        if (showUsernameSuggestions && usernameSuggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GrayLightBorder, RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                        .background(GrayLightBorder,RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp)),
                    shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    LazyColumn(
                        Modifier.height(165.dp)
                    ) {
                        items(usernameSuggestions.size) { index ->
                            Row(
                                Modifier
                                    .height(54.dp)
                                    .background(
                                        color = Gray20
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "@${usernameSuggestions[index]}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clickable {
                                            username = TextFieldValue(
                                                text = "@${usernameSuggestions[index]}",
                                                selection = TextRange(
                                                    start = usernameSuggestions[index].length + 1,
                                                    end = usernameSuggestions[index].length + 1
                                                )
                                            )
                                            isSuggestionSelected = true
                                            showUsernameSuggestions = false
                                            showUsernameTick = true
                                            showUsernameError = false
                                        }
                                        .padding(horizontal = 20.dp, vertical = 12.dp),
                                    fontSize = 16.sp,
                                    fontFamily = fontFamilyLato,
                                    fontWeight = FontWeight.Normal,
                                    color = Black
                                )

                                Image(
                                    painter = painterResource(id = R.drawable.ic_tick_green),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(20.dp)
                                )

                                Spacer(Modifier.width(15.dp))
                            }
                             Box(Modifier.height(1.dp).background(GrayLightBorder))
                        }
                    }
                }
            } else {

            }

        }

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
