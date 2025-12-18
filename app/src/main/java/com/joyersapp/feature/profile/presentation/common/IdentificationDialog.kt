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
import com.joyersapp.R


@Composable
fun IdentificationDialog(
    onClose: () -> Unit,
    onUploadImage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
            color = Color.White
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)   // extra padding at bottom for scroll
            ) {

                /** HEADER **/
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Identification",
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_cross_golden),
                        contentDescription = "Close",
                        tint = Color(0xFFD8A23A),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                            .size(22.dp)
                            .clickable { onClose() }
                    )
                }

                Spacer(Modifier.height(20.dp))

                /** PROFILE PICTURE SECTION **/
                SectionTitle("Profile Picture")

                Spacer(Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF7F7F7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.camera_outline_colored),
                                contentDescription = null,
                                modifier = Modifier.size(42.dp)
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Upload Picture",
                            fontSize = 14.sp,
                            color = Color(0xFFD8A23A),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        painter = painterResource(R.drawable.camera_inside_color),
                        contentDescription = null,
                        tint = Color(0xFFD8A23A),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE5E5E5), CircleShape)
                            .padding(4.dp)
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
    Text(
        text = text,
        fontSize = 14.sp,
        color = Color.Black,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 20.dp)
    )
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