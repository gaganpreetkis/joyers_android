package com.joyersapp.auth.presentation.login

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.auth.data.remote.dto.User
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.AppBasicTextFieldForPassword
import com.joyersapp.common_widgets.BottomSocialDialog
import com.joyersapp.common_widgets.CountryCodePicker
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Black
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.iosBounceScroll

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
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
    var isUsernameFieldFocused by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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

    LaunchedEffect(state.isLoginApiSuccess) {
        if (state.isLoginApiSuccess) {
            onLoginSuccess()
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

    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .iosBounceScroll(scrollState)
            .verticalScroll(scrollState)
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
                    .border(color = if (state.password.isNotEmpty() && state.username.isNotEmpty() && (state.apiOnlyUsernameErrorMessage.isNotEmpty() || state.apiErrorMessage.isNotEmpty())) Red else colorResource(id = R.color.color_border_light), width = 1.dp, shape = RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = if (state.isPhoneMode) R.drawable.ic_telephone_gray else R.drawable.user_icon),
                        contentDescription = null,
                        modifier = Modifier.height(if (state.isPhoneMode) 24.17.dp else 24.dp).width(if (state.isPhoneMode) 24.dp else 22.34.dp)
                    )

                    Spacer(modifier = Modifier.width(if (state.isPhoneMode) 0.dp else 0.5.dp))

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
                        },
                        maxLength = if (state.isPhoneMode) 15 else 100,
                        placeholder = if (state.isPhoneMode) "Phone Number" else "Username / Email",
                        modifier = Modifier.weight(1f).padding(bottom = 1.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (state.isPhoneMode) {
                                KeyboardType.Phone
                            } else {
                                KeyboardType.Email
                            }
                        ),
                        onFocusChanged = { focused ->
                            isUsernameFieldFocused = focused
                        }
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
                                    /*viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.RememberMeChanged(false))*/
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
                        viewModel.onEvent(LoginEvent.IsPhoneModeChanged(!state.isPhoneMode))
                        /*viewModel.onEvent(LoginEvent.UsernameChanged(""))
                        viewModel.onEvent(LoginEvent.RememberMeChanged(false))
                        viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                        viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                        viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))*/
                    }
                    .background(Gray20, RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                    .border(color = colorResource(id = R.color.color_border_light), width = 1.dp, shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = if (state.isPhoneMode) R.drawable.user_icon_golden else R.drawable.ic_telephone_golden),
                    contentDescription = "Toggle",
                    modifier = Modifier.height(if (state.isPhoneMode) 24.dp else 24.17.dp).width(if (state.isPhoneMode) 22.34.dp else 24.dp)
                )
            }
        }

        if (state.apiOnlyUsernameErrorMessage.isNotBlank() && state.username.isNotEmpty()) {
            Text(
                text = state.apiOnlyUsernameErrorMessage,
                color = Red,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 3.dp),
                lineHeight = 20.sp,
            )
        }

        // BOX FOR RECENT USER LIST AND OTHER VIEWS
        Box() {

            Column {

                Spacer(modifier = Modifier.height(10.dp))

                // PASSWORD FIELD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(color = if (state.password.isNotEmpty() && (state.passwordError || state.apiErrorMessage.isNotEmpty())) Red else colorResource(id = R.color.color_border_light), width = 1.dp, shape = RoundedCornerShape(5.dp))
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
                                modifier = Modifier.height(26.dp).width(22.62.dp)
                            )

                            Spacer(modifier = Modifier.width(0.dp))

                            AppBasicTextFieldForPassword(
                                value = state.password,
                                onValueChange = {
                                    viewModel.onEvent(LoginEvent.PasswordChanged(it))
                                    /*viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                                    viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                                    if (state.isFormValid && isValidPassword(state.password) && state.rememberMe) {
                                        viewModel.onEvent(LoginEvent.RememberMeChanged(false))
                                    }*/
                                },
                                maxLength = 16,
                                placeholder = "Password",
                                isPassword = true,
                                passwordVisible = state.passwordVisible,
                                onPasswordToggle = {
                                    viewModel.onEvent(LoginEvent.PasswordVisibleChanged(!state.passwordVisible))
                                },
                                modifier = Modifier.fillMaxSize().padding(bottom = 1.dp),
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

                if (state.apiErrorMessage.isNotEmpty() && state.password.isNotEmpty()) {
                    Text(
                        text = state.apiErrorMessage,
                        color = Red,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyLato,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // REMEMBER ME + FORGOT PASSWORD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            enabled = state.username.isNotEmpty() && state.password.isNotEmpty()/*state.isFormValid && isValidPassword(state.password)*/
                        ) {
                            if (state.username.isNotEmpty() && state.password.isNotEmpty()/*state.isFormValid && isValidPassword(state.password)*/) {
                                viewModel.onEvent(LoginEvent.RememberMeChanged(!state.rememberMe))
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = if (state.rememberMe) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Remember Me",
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = LightBlack60,
                            lineHeight = 12.sp,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Forgot Password?",
                        fontSize = 12.sp,
                        color = Golden60,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp,
                        modifier = Modifier.clickable {
                            viewModel.onEvent(LoginEvent.UsernameChanged(""))
                            viewModel.onEvent(LoginEvent.PasswordChanged(""))
                            viewModel.onEvent(LoginEvent.ApiErrorMessageChanged(""))
                            viewModel.onEvent(LoginEvent.ApiFailedErrorMessageChanged(""))
                            viewModel.onEvent(LoginEvent.ApiOnlyUsernameErrorMessageChanged(""))
                            onForgotPasswordClick()
                        },
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
                    enabled = state.username.isNotEmpty() && state.password.isNotEmpty()/*state.isFormValid && isValidPassword(state.password)*/,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(color = Golden60, shape = RoundedCornerShape(4.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Golden60,
                        disabledContainerColor = Golden60,
                        contentColor = White,
                        disabledContentColor = DisabledTextColor
                    ),
                ) {
                    Text(
                        "Login",
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 19.sp,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // OR
                Text(
                    text = "Or",
                    fontSize = 16.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    color = LightBlack60,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // JOIN WITH BUTTON
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(LightBlack, RoundedCornerShape(4.dp))
                        .clickable {
                            viewModel.onEvent(LoginEvent.ShowSocialDialogChanged(true))
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Join With",
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Image(
                        painter = painterResource(id = R.drawable.icon_awesome_caret_down),
                        contentDescription = null,
                        modifier = Modifier
                            .width(12.dp)
                            .padding(top = 1.dp)
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

            }

            // RECENT USERS DROPDOWN LIST
            if (isUsernameFieldFocused && !state.isPhoneMode && state.recentUsersList.isNotEmpty()) {
                if (state.filteredList.isNotEmpty()) {
                    Column() {

                        Spacer(modifier = Modifier.height(3.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(White, RoundedCornerShape(5.dp))
                                .border(
                                    color = colorResource(id = R.color.color_border_light),
                                    width = 1.dp,
                                    shape = RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp)
                                )
                        ) {
                            state.filteredList.forEachIndexed { index, user ->
                                val isFirst = index == 0
                                val isLast = index == state.filteredList.size - 1
                                val displayValue = if (user.recentType.equals("email", true)) {
                                    user.email.orEmpty()
                                } else {
                                    "@${user.username.orEmpty()}"
                                }
                                RecentUserItem(
                                    isSingleItem = state.filteredList.size == 1,
                                    isFirst = isFirst,
                                    isLast = isLast,
                                    user = user,
                                    onItemClick = {
                                        viewModel.onEvent(LoginEvent.UsernameChanged(displayValue))
                                        focusManager.clearFocus()
                                    },
                                    onRemoveClick = {
                                        val updatedList = state.recentUsersList.toMutableList()
                                        updatedList.remove(user)
                                        viewModel.saveUserNames(updatedList)
                                    }
                                )
                                /*if (index < filteredList.size - 1) {
                                    Spacer(modifier = Modifier.height(0.dp))
                                }*/
                            }
                        }

                    }
                }
            }

        }

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
                .background(Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Golden60,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun RecentUserItem(
    isSingleItem: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    user: User,
    onItemClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val isEmail = user.recentType.equals("email", true)
    val displayValue = if (isEmail) {
        user.email.orEmpty()
    } else {
        "@${user.username.orEmpty()}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (isFirst) 6.5.dp else 0.dp, bottom = if (isSingleItem) 7.5.dp else if (isLast) 8.5.dp else 0.dp)
            .clickable { onItemClick() }
            .padding(start = 14.5.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon on the left
        if (!isEmail) {
            // Profile picture icon for username - circular with colored background
            if (user.profile_picture != null && user.profile_picture?.isNotEmpty() == true) {
                AsyncImage(
                    model = "https://joyers-api-dev.krishnais.com/uploads/${user.profile_picture}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .border(shape = CircleShape, width = 1.5.dp, color = AvatarBorder)
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    fallback = painterResource(id = R.drawable.avatar),
                    error = painterResource(id = R.drawable.avatar),
                    placeholder = painterResource(id = R.drawable.avatar)
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(28.dp)
                        .border(shape = CircleShape, width = 1.5.dp, color = Golden60)
                        .padding(4.dp)
                        .background(shape = CircleShape, color = Gray20)
                        .padding(2.3.dp),
                    painter = painterResource(id = R.drawable.suggestion_joy),
                    contentDescription = null,
                )
            }
        } else {
            // Envelope icon for email
            Image(
                painter = painterResource(id = R.drawable.ic_mail),
                contentDescription = null,
                modifier = Modifier.size(width = 28.dp, height = 17.12.dp),
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Text in the middle
        Text(
            text = displayValue,
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            color = LightBlack,
            modifier = Modifier.weight(1f),
            lineHeight = 24.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(15.dp))

        // X button on the right
        Image(
            painter = painterResource(id = R.drawable.close_1),
            contentDescription = "Remove",
            modifier = Modifier
                .alpha(0.55f)
                .clickable { onRemoveClick() }
        )
    }
}

