package com.joyersapp.components.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
import com.joyersapp.theme.LightBlack13
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.LightBlack9
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable

//@Preview
@Composable
fun EditProfileHeaderDialog(
    onDismiss: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    BaseDialog (
        onDismiss = { onDismiss() },
        titles = arrayListOf("Profile Header")

    ) { dialogModifier, dialogFocusManager, maxHeight ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 35.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
        ) {
            // ---------- HEADER SECTION ----------
            EditableProfilePictureCard(
                backgroundPicturePath = "",
                profilePicturePath = "",
                onHeaderPicker = {},
                onClearHeaderIamage = {},
                onProfilePicturePicker = {},
                onClearProfilePicture = {}
            )

            // ---------- BIO SECTION ----------
            Text(
                text = "Bio",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

            BioEditor("") {}

            Spacer(modifier = Modifier.height(10.dp))

            // ---------- WEBSITE SECTION ----------
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(60.dp)) {
                Text(
                    text = "Website",
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fontFamilyLato,
                    color = LightBlack
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0xFFF1F1F1))
                        .padding(horizontal = 15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AppBasicTextField(
                        value = "",
                        onValueChange = {

                        },
                        placeholder = "Domain Link",
                        modifier = Modifier
                            .padding(bottom = 1.dp)
                            .fillMaxWidth()
                            .imePadding()
                            .focusRequester(remember { FocusRequester() })
                            .onFocusChanged { focusState ->

                            },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = LightBlack,
                            fontWeight = FontWeight.Normal,
//                            textAlign = TextAlign.Center,
                            fontFamily = fontFamilyLato
                        ),
                        maxLength = 45
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ---------- APPLY BUTTON ----------
            Button (
                onClick = onApply,
                modifier = Modifier
                    .width(190.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(47.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Golden),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = White
                )
            }
        }
    }
}

@Composable
fun EditableProfilePictureCard(
    backgroundPicturePath: String,
    profilePicturePath: String,
    onHeaderPicker: () -> Unit,
    onClearHeaderIamage: () -> Unit,
    onProfilePicturePicker: () -> Unit,
    onClearProfilePicture: () -> Unit

) {
    // Card with profile and header images
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .border(
                width = 1.dp,
                color = GrayLightBorder,
                shape = RoundedCornerShape(5.dp)
            ),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = Gray20)
    ) {

        var showHeaderPicker by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            // Header/Background Image
            if (backgroundPicturePath.isNotEmpty()) {
                AsyncImage(
                    model = backgroundPicturePath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Close button for header
                Image(
                    painter = painterResource(id = R.drawable.ic_cancel_round_golden),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(15.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            onClearHeaderIamage()
                        }
                )
            } else {
                // Header picker button
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(15.dp)
                            .noRippleClickable {
                                onHeaderPicker()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(37.dp)
                                .background(Color.White, CircleShape)
                                .border(1.dp, LightBlack13, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera_inside_color),
                                contentDescription = "Edit Background",
                                modifier = Modifier.width(20.82.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.header),
                            fontSize = 11.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = LightBlack60,
                            lineHeight = 20.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                }
            }

            Box(
                modifier = Modifier
                    .size(189.dp)
                    .align(Alignment.Center)
            ) {

                // Profile Image (centered)
                Box(
                    modifier = Modifier
                        .size(189.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            2.dp,
                            if (profilePicturePath.isNotEmpty()) Color.Transparent else LightBlack9,
                            CircleShape
                        )
                ) {
                    if (profilePicturePath.isNotEmpty()) {
                        AsyncImage(
                            model = profilePicturePath,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    onProfilePicturePicker
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera_outline_colored),
                                contentDescription = null,
                                modifier = Modifier.size(71.dp, 55.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = stringResource(R.string.upload_picture),
                                fontSize = 15.sp,
                                fontFamily = fontFamilyLato,
                                fontStyle = FontStyle.Normal,
                                color = LightBlack60,
                                lineHeight = 21.sp,
                            )
                        }
                    }
                }

                if (profilePicturePath.isNotEmpty()) {
                    // Close button for profile
                    Image(
                        painter = painterResource(id = R.drawable.ic_cross_round_border_golden),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp)
                            .align(Alignment.BottomEnd)
                            .clickable {
                                onClearProfilePicture()
                            }
                    )
                }

            }
        }
    }
}

@Composable
fun BioEditor(
    overview: String,
    onOverviewChange: (String) -> Unit,
) {
    var selectedTab by remember { mutableStateOf("overview") }

    Card(
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, LightBlack10),
        colors = CardDefaults.cardColors(containerColor = GrayBG),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            // ---------------------- TAB HEADER ----------------------
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                TabItem(
                    title = "Overview",
                    selected = selectedTab == "overview"
                ) { selectedTab = "overview" }
                VerticalDivider(color = LightBlack10)
                TabItem(
                    title = "Highlights",
                    selected = selectedTab == "highlights"
                ) { selectedTab = "highlights" }
            }

            HorizontalDivider(color = LightBlack10)

            // ---------------------- TEXT EDITOR ----------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Text(
                    text = "25",
                    color = LightBlack60,
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fontFamilyLato,
                    modifier = Modifier
                        .padding(top = 5.5.dp, bottom = 1.dp, end = 7.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.End
                )

                Box(Modifier.padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)) {
                    if (selectedTab == "overview") {
                        OverviewEditor(
                            text = overview,
                            onChange = onOverviewChange
                        )
                    } else {
                        HighlightsEditor()
                    }
                }
            }
        }
    }
}

@Composable
fun TabItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (selected) Golden else LightBlack,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = fontFamilyLato
        )
    }
}

@Composable
fun OverviewEditor(
    text: String,
    onChange: (String) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = onChange,
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = Modifier.fillMaxSize(),
        decorationBox = { inner ->
            if (text.isEmpty()) {
                Text("About Joyer", color = Color.Gray)
            }
            inner()
        }
    )
}
@Composable
fun HighlightsEditor(
) {
    var bullets by remember { mutableStateOf("• ") }

    BasicTextField(
        value = bullets,
        onValueChange = { newValue ->
            bullets = newValue

            // Auto-add bullet when pressing Enter
            if (newValue.endsWith("\n")) {
                bullets += "• "
            }
        },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        decorationBox = { inner ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                inner()
            }
        }
    )
}