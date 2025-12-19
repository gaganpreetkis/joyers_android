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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato


@Composable
fun IdentificationDialog(
    onClose: () -> Unit,
    onUploadImage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
                InputWithPlusButton(placeholder = "Joyer Name")

                Spacer(Modifier.height(20.dp))

                /** BIRTH SECTION **/
                SectionTitle("Birth")

                Spacer(Modifier.height(14.dp))
                InputLabel("Date")
                RoundedInputField("Joyer Birthday")

                Spacer(Modifier.height(14.dp))
                InputLabel("Birthplace")
                RoundedInputField("Joyer Birthplace")

                Spacer(Modifier.height(14.dp))
                InputLabel("Status")
                RoundedInputField("Birth Status")

                Spacer(Modifier.height(14.dp))
                InputLabel("Brief")
                RoundedInputField("Brief about Birth")

                Spacer(Modifier.height(20.dp))
                InputLabel("File")
                FileUploadInput()
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