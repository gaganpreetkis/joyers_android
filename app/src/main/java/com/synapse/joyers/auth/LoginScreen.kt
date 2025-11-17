package com.synapse.joyers.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.synapse.joyers.R
import androidx.compose.ui.unit.sp

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 35.dp)
    ) {

        Spacer(modifier = Modifier.height(45.dp))

        // LOGO
        Image(
            painter = painterResource(id = R.drawable.joyer_logo),
            contentDescription = "Joyers Logo",
            modifier = Modifier
                .width(155.dp)
                .height(59.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(45.dp))

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

                Spacer(modifier = Modifier.width(12.dp))

                TextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    placeholder = { Text("Username / Email", color = Color(0xFF9A9A9A)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        disabledIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            // RIGHT CALL ICON PART
            Box(
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxHeight()
                    .background(Color(0xFFF1F1F1), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.telephone_icon_golden),
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
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.password_icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text("Password", color = Color(0xFF9A9A9A)) },
                visualTransformation =
                    if (passwordVisible.value) VisualTransformation.None
                    else PasswordVisualTransformation(),
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    disabledIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Image(
                    painter = painterResource(id = R.drawable.password_hide),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // REMEMBER ME + FORGOT PASSWORD
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                    modifier = Modifier.size(16.dp)
                )
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
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD4A038)
            )
        ) {
            Text("Login", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
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
                .clickable { onJoinWithClick() },
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
    }
}
