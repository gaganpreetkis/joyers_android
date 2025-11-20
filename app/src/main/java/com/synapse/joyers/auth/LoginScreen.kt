package com.synapse.joyers.auth

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.synapse.joyers.R
import androidx.compose.ui.unit.sp
import com.synapse.joyers.common_widgets.BottomSocialDialog
import com.synapse.joyers.common_widgets.CountryCodePicker

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onJoinWithClick: () -> Unit = {}
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val isPhoneMode = remember { mutableStateOf(false) }
    var showSocialDialog by remember { mutableStateOf(false) }
    var selectedCountryCode by remember { mutableStateOf("+1") }
    var rememberMe by remember { mutableStateOf(false) }
    var isKeyboardVisible by remember { mutableStateOf(false) }
    var logoSize by remember { mutableStateOf(155.dp to 59.dp) }
    var viewHeight by remember { mutableStateOf(45.dp) }
    var showGapView by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222)
        )

        Spacer(modifier = Modifier.height(15.dp))

        // USERNAME / EMAIL BOX (LEFT BIG + RIGHT ICON)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            // LEFT PART
            Row(
                modifier = Modifier
                    .weight(0.85f)
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .padding(start = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(0.dp))

                if (isPhoneMode.value)
                    CountryCodePicker { code ->
                        selectedCountryCode = code
                    }

                TextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    placeholder = { Text(if (isPhoneMode.value) "Phone Number" else "Username / Email", color = Color(0xFF9A9A9A)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF1F1F1),
                        focusedContainerColor = Color(0xFFF1F1F1),
                        disabledIndicatorColor = Color(0xFFF1F1F1),
                        focusedIndicatorColor = Color(0xFFF1F1F1),
                        unfocusedIndicatorColor = Color(0xFFF1F1F1),
                    ),
                    singleLine = true,
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            // RIGHT CALL ICON PART
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxHeight()
                    .clickable {
                        isPhoneMode.value = !isPhoneMode.value
                    }
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = if (isPhoneMode.value) R.drawable.ic_mail_golden else R.drawable.telephone_icon_golden),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // PASSWORD FIELD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFFF1F1F1), RoundedCornerShape(8.dp))
                .padding(start = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.password_icon),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(0.dp))

                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    placeholder = { Text("Password", color = Color(0xFF9A9A9A)) },
                    visualTransformation =
                        if (passwordVisible.value) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF1F1F1),
                        focusedContainerColor = Color(0xFFF1F1F1),
                        disabledIndicatorColor = Color(0xFFF1F1F1),
                        focusedIndicatorColor = Color(0xFFF1F1F1),
                        unfocusedIndicatorColor = Color(0xFFF1F1F1),
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (password.value.trim().isNotEmpty()) {
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Image(
                        painter = painterResource(id = if (passwordVisible.value) R.drawable.show_password else R.drawable.password_hide),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // REMEMBER ME + FORGOT PASSWORD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 19.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    rememberMe = !rememberMe
                }
            ) {
                Image(
                    painter = painterResource(id = if (rememberMe) R.drawable.checkbox_checked else R.drawable.checkbox_unchecked),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Remember Me",
                    fontSize = 12.sp,
                    color = Color(0xFF9A9A9A)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Forgot Password?",
                fontSize = 12.sp,
                color = Color(0xFFD4A038),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onForgotPasswordClick() }
            )
        }

        Spacer(modifier = Modifier.height(45.dp))

        // LOGIN BUTTON
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = Color(0xFFD4A038), shape = RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD4A038)
            )
        ) {
            Text("Login", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(11.dp))

        // OR
        Text(
            text = "Or",
            fontSize = 16.sp,
            color = Color(0xFF9A9A9A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // JOIN WITH BUTTON
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black, RoundedCornerShape(8.dp))
                .clickable { showSocialDialog = true },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Join With",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(6.dp))

            Image(
                painter = painterResource(id = R.drawable.icon_awesome_caret_down),
                contentDescription = null,
                modifier = Modifier.width(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        // SIGNUP FOOTER
        Text(
            text = "A New Joyer?",
            fontSize = 12.sp,
            color = Color(0xFF9A9A9A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Sign Up",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD4A038),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { onSignUpClick() }
                .padding(top = 4.dp, bottom = 20.dp)
        )

        BottomSocialDialog(
            showDialog = showSocialDialog,
            onDismiss = { showSocialDialog = false },
            onFacebookClick = { },
            onGoogleClick = { }
        )
    }
}

