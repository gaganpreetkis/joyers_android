package com.joyersapp.feature.profile.presentation.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.auth.presentation.identity.IdentityEvent
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.theme.Black
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.containsEmoji
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isAllowedIdentityNameChars


@Composable
fun IdentificationDialog(
    onClose: () -> Unit,
    onUploadImage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var usernameError by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf("") }
    var isUsernamFocused by remember { mutableStateOf(false) }
    val maxLength = 45
    var remainingChars by remember { mutableStateOf(maxLength) }
    if (!isUsernamFocused && username.isNotEmpty() && remainingChars > 43) {
        usernameError = stringResource(R.string.username_error)
    }
    val redColor = Red
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.padding(vertical = 50.dp),
            shape = RoundedCornerShape(25.dp),
            color = White
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 35.dp)   // extra padding at bottom for scroll
            ) {

                /** HEADER **/
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(
                        text = "Identification",
                        fontSize = 24.sp,
                        color = LightBlack,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 29.sp,
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_cross_golden),
                        contentDescription = "Close",
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .padding(end = 23.04.dp, top = 16.5.dp)
                            .size(15.51.dp)
                            .clickable { onClose() }
                    )
                }

                Spacer(Modifier.height(20.dp))

                /** NAME SECTION **/
                SectionTitle("Name")
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .height(60.dp)
                        .background(
                            color = Gray20,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (usernameError != null) Red else GrayLightBorder,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 14.dp)
                            .background(color = White, shape = RoundedCornerShape(30.dp))
                            .border(color = LightBlack10, shape = RoundedCornerShape(30.dp), width = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        AppBasicTextField(
                            value = username,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words
                            ),
                            onValueChange = {
                                if (it.length <= maxLength) {
                                    username = it
                                    //viewModel2.onEvent(IdentityEvent.NameChanged(it))
                                    if (it.isEmpty()) {
                                        remainingChars = maxLength - it.length
                                        usernameError = null
                                        return@AppBasicTextField
                                    }
                                    if (containsEmoji(it) || !isAllowedIdentityNameChars(it)) {
                                        remainingChars = maxLength - (it.length / 2)
                                        usernameError = context.getString(R.string.username_error)
                                    } else {
                                        remainingChars = maxLength - it.length
                                        usernameError = null
                                    }
                                }
                            },
                            placeholder = stringResource(R.string.joyer_name),
                            containerColor = White,
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(bottom = 1.dp)
                                .fillMaxWidth()
                                .imePadding()
                                .focusRequester(remember { FocusRequester() })
                                .onFocusChanged { focusState ->
                                    isUsernamFocused = focusState.isFocused
                                },
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                color = lightBlackColor,
                                fontWeight = if (username.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                                textAlign = TextAlign.Start,
                                fontFamily = fontFamilyLato
                            ),
                            maxLength = 45
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = remainingChars.toString(),
                            fontSize = 12.sp,
                            color = if (remainingChars == 0) redColor else hintColor,
                            modifier = Modifier.fillMaxHeight().padding(top = 3.dp, end = 13.dp),
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 24.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                }

                if (usernameError != null) {
                    Text(
                        text = usernameError!!,
                        color = redColor,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyLato,
                        fontStyle = FontStyle.Normal,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
                Spacer(Modifier.height(20.dp))

                /** BIRTH SECTION **/
                SectionTitle("Birth")

                Spacer(Modifier.height(14.dp))
                InputLabel("Date")
                RoundedInputField("Joyer Birthday")

                /** GENDER SECTION **/
                SectionTitle("Gender")
                Spacer(Modifier.height(14.dp))
                InputLabel("Birthplace")
                RoundedInputField("Joyer Birthplace")

                /** NATIONALITY SECTION **/
                SectionTitle("Nationality")
                Spacer(Modifier.height(14.dp))
                InputLabel("Status")
                RoundedInputField("Birth Status")

                /** ETHNICITY SECTION **/
                SectionTitle("Ethnicity")
                Spacer(Modifier.height(14.dp))
                InputLabel("Brief")
                RoundedInputField("Brief about Birth")

                /** FAITH SECTION **/
                SectionTitle("Faith")
                Spacer(Modifier.height(20.dp))
                InputLabel("File")
                FileUploadInput()

                /** LANGUAGE SECTION **/
                SectionTitle("Language")
                Spacer(Modifier.height(14.dp))
                InputLabel("Brief")
                RoundedInputField("Brief about Birth")

                /** RELATIONSHIP STATUS SECTION **/
                SectionTitle("Relationship")
                Spacer(Modifier.height(14.dp))
                InputLabel("Brief")
                RoundedInputField("Brief about Birth")

                /** POLITICAL IDEOLOGY SECTION **/
                SectionTitle("Political Ideology")
                Spacer(Modifier.height(14.dp))
                InputLabel("Brief")
                RoundedInputField("Brief about Birth")

                /** JOYER LOCATION SECTION **/
                SectionTitle("Joyer Location")
                Spacer(Modifier.height(14.dp))
                InputLabel("Brief")
                RoundedInputField("Brief about Birth")
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Box(
        modifier = Modifier.height(19.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = LightBlack,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 15.dp),
        )
    }
}

@Composable
fun InputLabel(label: String) {
    Text(
        text = label,
        fontSize = 13.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 20.dp)
    )
}


@Composable
fun RoundedInputField(placeholder: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = placeholder,
            fontSize = 14.sp,
            color = Color(0xFFB7B7B7),
            modifier = Modifier.padding(start = 14.dp)
        )
    }
}

@Composable
fun InputWithPlusButton(placeholder: String) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = placeholder,
            fontSize = 14.sp,
            color = Color(0xFFB7B7B7),
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        )

        Icon(
            painter = painterResource(R.drawable.ic_nav_joyers_add),
            contentDescription = null,
            tint = Color(0xFFD8A23A),
            modifier = Modifier
                .padding(end = 14.dp)
                .size(20.dp)
        )
    }
}

@Composable
fun FileUploadInput() {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF7F7F7)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Choose File",
            fontSize = 14.sp,
            color = Color(0xFFB7B7B7),
            modifier = Modifier.padding(start = 14.dp)
        )
    }
}