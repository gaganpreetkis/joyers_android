package com.joyersapp.auth.presentation.login

import android.graphics.Rect
import android.util.Patterns
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joyersapp.R
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.auth.presentation.forgotpassword.ForgotPasswordEvent
import com.joyersapp.auth.presentation.forgotpassword.ForgotPasswordViewModel
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.AppBasicTextFieldForPassword
import com.joyersapp.common_widgets.BottomSocialDialog
import com.joyersapp.common_widgets.CountryCodePicker
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isValidPassword
import com.joyersapp.utils.isValidUsername
import kotlin.text.isNotEmpty

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onJoinWithClick: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    //var username by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }
    //val passwordVisible = remember { mutableStateOf(false) }
    //var emailPhoneError by remember { mutableStateOf(false) }
    //var passwordError by remember { mutableStateOf(false) }
    //val isPhoneMode = remember { mutableStateOf(false) }
    //var showSocialDialog by remember { mutableStateOf(false) }
    //var selectedCountryCode by remember { mutableStateOf("+1") }
    //var selectedCountryNameCode by remember { mutableStateOf("US") }
    //var rememberMe by remember { mutableStateOf(false) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var logoSize by remember { mutableStateOf(155.dp to 59.dp) }
    var viewHeight by remember { mutableStateOf(45.dp) }
    var showGapView by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isFormValid = remember(state.username, state.isPhoneMode) {
        if (state.isPhoneMode) {
            // PHONE VALIDATION
            state.username.isNotEmpty() &&
                    state.username.all { it.isDigit() } &&
                    state.username.length in 10..15
        } else {
            // USERNAME or EMAIL VALIDATION
            state.username.isNotEmpty() && (isValidUsername(state.username) || Patterns.EMAIL_ADDRESS.matcher(state.username).matches())
        }
    }

    // Function to check keyboard visibility and update UI
    fun updateKeyboardState(frameLayout: View) {
        val rect = Rect()
        frameLayout.getWindowVisibleDisplayFrame(rect)
        val screenHeight = frameLayout.rootView.height
        val keypadHeight = screenHeight - rect.bottom

        if (keypadHeight > screenHeight * 0.15) {
            // Keyboard is visible
            logoSize = 125.dp to 48.dp
            viewHeight = 20.dp
            showGapView = true
            isKeyboardVisible = true
        } else {
            // Keyboard is hidden
            logoSize = 155.dp to 59.dp
            viewHeight = 45.dp
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 35.dp)
            .statusBarsPadding()
            .imePadding()
    ) {

        Spacer(modifier = Modifier.height(viewHeight))

        // LOGO
        Image(
            painter = painterResource(id = R.drawable.joyer_logo),
            contentDescription = "Joyers Logo",
            modifier = Modifier
                .size(logoSize.first, logoSize.second)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(viewHeight))

        // LOGIN LABEL
        Text(
            text = "Login",
            fontSize = 18.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.SemiBold,
            color = LightBlack
        )

        Spacer(modifier = Modifier.height(15.dp))

        // USERNAME / EMAIL BOX (LEFT BIG + RIGHT ICON)
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
                    .border(color = if (state.apiOnlyUsernameErrorMessage.isNotEmpty() || state.apiErrorMessage.isNotEmpty()) Red else colorResource(id = R.color.color_border_light), width = 1.dp, shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = if (state.isPhoneMode) R.drawable.ic_telephone_gray else R.drawable.user_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(0.dp))

                    if (state.isPhoneMode)
                        CountryCodePicker(
                            initialPadding = 8f,
                            defaultCountry = state.selectedCountryNameCode,
                            onCountrySelected = { code ->
                                viewModel.onEvent(LoginEvent.SelectedCountryCodeChanged(code))
                            },
                            onCountryNameCodeSelected = { nameCode ->
                                viewModel.onEvent(LoginEvent.SelectedCountryNameCodeChanged(nameCode))
                            }
                        )

                    AppBasicTextField(
                        value = state.username,
                        onValueChange = {
                            viewModel.onEvent(LoginEvent.UsernameChanged(it))
                            viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                            viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                            viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                            if (isFormValid && isValidPassword(state.password) && state.rememberMe) {
                                viewModel.onEvent(LoginEvent.RememberMeChanged(false))
                            }
                        },
                        maxLength = if (state.isPhoneMode) 15 else 100,
                        placeholder = if (state.isPhoneMode) "Phone Number" else "Username / Email",
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (state.isPhoneMode) {
                                KeyboardType.Phone
                            } else {
                                KeyboardType.Email
                            }
                        ),
                    )

                    if (state.username.isNotEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cancel_grey),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(start = 5.dp, end = 10.dp)
                                .clickable {
                                    viewModel.onEvent(LoginEvent.UsernameChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.RememberMeChanged(false))
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(5.dp))

            // RIGHT CALL ICON PART
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxHeight()
                    .clickable {
                        viewModel.onEvent(LoginEvent.UsernameChanged(""))
                        viewModel.onEvent(LoginEvent.RememberMeChanged(false))
                        viewModel.onEvent(LoginEvent.IsPhoneModeChanged(!state.isPhoneMode))
                        viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                        viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                        viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                    }
                    .background(Gray20, RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                    .border(color = colorResource(id = R.color.color_border_light), width = 1.dp, shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = if (state.isPhoneMode) R.drawable.user_icon_golden else R.drawable.telephone_icon_golden),
                    contentDescription = "Toggle",
                    modifier = Modifier.size(24.dp),
                    colorFilter = ColorFilter.tint(Golden60)
                )
            }
        }

        if (state.apiOnlyUsernameErrorMessage.isNotBlank()) {
            Text(
                text = state.apiOnlyUsernameErrorMessage,
                color = Red,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                modifier = Modifier.fillMaxWidth().padding(top = 3.dp),
                lineHeight = 20.sp,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // PASSWORD FIELD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(color = if (state.passwordError || state.apiErrorMessage.isNotEmpty()) Red else colorResource(id = R.color.color_border_light), width = 1.dp, shape = RoundedCornerShape(5.dp))
                .background(Gray20, RoundedCornerShape(5.dp))
        ) {
            Row(
                modifier = Modifier.padding(start = 20.dp),
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
                        value = state.password,
                        onValueChange = {
                            viewModel.onEvent(LoginEvent.PasswordChanged(it))
                            viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                            viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                            viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                            if (isFormValid && isValidPassword(state.password) && state.rememberMe) {
                                viewModel.onEvent(LoginEvent.RememberMeChanged(false))
                            }
                        },
                        maxLength = 16,
                        placeholder = "Password",
                        isPassword = true,
                        passwordVisible = state.passwordVisible,
                        onPasswordToggle = {
                            viewModel.onEvent(LoginEvent.PasswordVisibleChanged(!state.passwordVisible))
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        if (state.passwordError) {
            Text(
                text = "The password or username entered is incorrect. Please try again.",
                color = Red,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        if (state.apiErrorMessage.isNotEmpty()) {
            Text(
                text = state.apiErrorMessage,
                color = Red,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // REMEMBER ME + FORGOT PASSWORD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(
                    enabled = isFormValid && isValidPassword(state.password)
                ) {
                    if (isFormValid && isValidPassword(state.password)) {
                        viewModel.onEvent(LoginEvent.RememberMeChanged(!state.rememberMe))
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = if (state.rememberMe) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Remember Me",
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = LightBlack60
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                color = Color(0xFFD4A038),
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    viewModel.onEvent(LoginEvent.UsernameChanged(""))
                    viewModel.onEvent(LoginEvent.PasswordChanged(""))
                    viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                    viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                    viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                    onForgotPasswordClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(if (state.apiFailedErrorMessage.isNotBlank()) 11.dp else 45.dp))

        if (state.apiFailedErrorMessage.isNotBlank()) {
            Text(
                text = state.apiFailedErrorMessage,
                color = Red,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(if (state.apiFailedErrorMessage.isNotBlank()) 14.dp else 0.dp))

        // LOGIN BUTTON
        Button(
            onClick = {
                //onLoginClick()
                //passwordError = !passwordError
                //emailPhoneError = !emailPhoneError
                viewModel.onEvent(LoginEvent.OnLoginButtonClicked)
            },
            enabled = isFormValid && isValidPassword(state.password),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Golden60, shape = RoundedCornerShape(5.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Golden60,
                disabledContainerColor = Golden60,
                contentColor = White,
                disabledContentColor = DisabledTextColor
            ),
        ) {
            Text(
                "Login", fontSize = 16.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // OR
        Text(
            text = "Or",
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            color = Color(0x99333333),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // JOIN WITH BUTTON
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black, RoundedCornerShape(5.dp))
                .clickable {
                    viewModel.onEvent(LoginEvent.ShowSocialDialogChanged(true))
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Join With",
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.width(7.dp))

            Image(
                painter = painterResource(id = R.drawable.icon_awesome_caret_down),
                contentDescription = null,
                modifier = Modifier
                    .width(12.dp)
                    .padding(top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        // SIGNUP FOOTER
        Text(
            text = "A New Joyer?",
            fontSize = 12.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            color = LightBlack60,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Sign Up",
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = Golden60,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    viewModel.onEvent(LoginEvent.UsernameChanged(""))
                    viewModel.onEvent(LoginEvent.PasswordChanged(""))
                    viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                    viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                    viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                    onSignUpClick()
                }
                .padding(top = 0.dp, bottom = 0.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        BottomSocialDialog(
            showDialog = state.showSocialDialog,
            onDismiss = {
                viewModel.onEvent(LoginEvent.ShowSocialDialogChanged(false))
            },
            onFacebookClick = { },
            onGoogleClick = { }
        )
    }

    if (state.isLoading) {
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

