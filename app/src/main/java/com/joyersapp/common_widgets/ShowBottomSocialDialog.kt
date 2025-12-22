package com.joyersapp.common_widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack15
import com.joyersapp.utils.fontFamilyLato

@Composable
fun BottomSocialDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onFacebookClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val overlayColor = colorResource(id = R.color.color_overlay_dark)

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(overlayColor.copy(alpha = 0.8f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() },
                contentAlignment = Alignment.BottomCenter
            ) {

                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .padding(horizontal = 35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Drag Handle
                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .width(58.dp)
                            .height(3.dp)
                            .background(
                                color = LightBlack15,
                                shape = RoundedCornerShape(2.dp)
                            )
                    )

                    // Title
                    Text(
                        text = context.getString(R.string.join_with),
                        fontSize = 24.sp,
                        color = LightBlack,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 11.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // GOOGLE BUTTON
                    SocialButton(
                        icon = R.drawable.google,
                        text = context.getString(R.string.google),
                        onClick = {
                            onGoogleClick()
                            onDismiss()
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    // FACEBOOK BUTTON
                    SocialButton(
                        icon = R.drawable.facebook,
                        text = context.getString(R.string.facebook),
                        onClick = {
                            onFacebookClick()
                            onDismiss()
                        }
                    )

                    Spacer(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .height(40.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun SocialButton(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(
                color = Gray20,
                shape = RoundedCornerShape(5.dp)
            )
            .border(
                width = 1.dp,
                color = GrayLightBorder,
                shape = RoundedCornerShape(5.dp)
            )
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(31.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = text,
            fontSize = 18.sp,
            color = LightBlack,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 1.dp)
        )
    }
}
