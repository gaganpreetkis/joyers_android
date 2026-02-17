package com.joyersapp.feature.profile.presentation.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.presentation.UserProfileUiState
import com.joyersapp.theme.Golden
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack5
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

@Preview
@Composable
fun preProfile() {
    ProfileStatusSection(state = UserProfileUiState())
}

@Composable
fun ProfileStatusSection(
    modifier: Modifier = Modifier,
    state: UserProfileUiState,
    onEditDescription: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GrayBG)
    ) {
        Spacer(Modifier.height(8.dp))

        /** -------- Description Header -------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Description",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp,
                modifier = Modifier
                    .offset(-1.dp)
            )

            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(LightBlack5)
                    .clickable {
                        onEditDescription()
                    }
                    .padding(start = 10.dp, top = 9.5.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_pencil),
                    contentDescription = "Edit",
                    modifier = Modifier.size(15.7.dp)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (!state.joyerStatus.isNullOrEmpty() || !state.titleName.isNullOrEmpty() || !state.subTitleName.isNullOrEmpty() || !state.areaOfInterest.isNullOrEmpty())
            Column(
                modifier = Modifier
                    .background(White)
                    .padding(top = 10.dp, start = 15.dp, bottom = 12.dp, end = 15.dp)
            ) {
                /** -------- Key-Value Rows -------- */
                ProfileKeyValueRow(label = "Joyer Status", value = state.joyerStatus ?: "")
                ProfileKeyValueRow(label = "Title", value = state.titleName ?: "")
                ProfileKeyValueRow(label = "Sub-Title", value = state.subTitleName ?: "")
                InterestsRowWithDotSeparators("Area of Interest", state.areaOfInterest)
            }

        Spacer(Modifier.height(7.dp))

        /** -------- Joying Header -------- */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Joying",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp,
                modifier = Modifier
                    .offset(-1.dp)
            )

        }

        Spacer(Modifier.height(7.dp))

        JoyerCodeSection(state = state)

        Spacer(Modifier.height(10.dp))

    }
}

@Composable
fun ProfileKeyValueRow(
    label: String,
    value: String
) {
    if (value.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamilyLato,
                color = LightBlack60,
                lineHeight = 24.sp,
            )

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp,
                modifier = Modifier
                    .padding(start = 130.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}


@Composable
fun JoyerCodeSection(
    modifier: Modifier = Modifier,
    state: UserProfileUiState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {

        DateInfoRow(
            label = "Joying Since",
            date = state.joySince ?: "",
            duration = state.joySinceDuration ?: ""
        )

        Spacer(Modifier.height(19.dp))

        /*  DateInfoRow(
              label = "Friends Since",
              date = "12 June 2019",
              duration = "2 Years, 6 Months, 11 Days"
          )

          Spacer(Modifier.height(19.dp))*/

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Joyer Code",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamilyLato,
                color = LightBlack60,
                lineHeight = 24.sp,
            )

            Column(
                Modifier
                    .padding(start = 130.dp)
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(5.5.dp))

                if (!state.qrCode.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .border(width = 3.dp, Golden, shape = RoundedCornerShape(25.dp))
                            .size(200.dp)
                    ) {
                        AsyncImage(
                            model = "${NetworkConfig.IMAGE_BASE_URL}${state.qrCode}",
                            contentDescription = "QR Code",
                            modifier = Modifier
                                .size(200.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                }

                // Removed in sprint 3
                /*if (!state.fullname?.trim().isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.width(200.dp),
                        text = state.fullname.trim(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyLato,
                        color = LightBlack,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )

                    Spacer(Modifier.height(7.dp))
                }*/

                if (state.username.trim().isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .widthIn(max = 200.dp),
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text(
                            text = "@",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.width(3.5.dp))
                        Text(
                            text = state.username,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DateInfoRow(
    label: String,
    date: String,
    duration: String
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamilyLato,
            color = LightBlack60,
            lineHeight = 24.sp,
        )

        Column(Modifier.padding(start = 130.dp)) {
            Text(
                text = date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp,
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = duration,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamilyLato,
                color = LightBlack60,
                lineHeight = 22.sp,
            )
        }
    }
}

@Composable
fun InterestsRowWithDotSeparators(
    label: String,
    values: List<Interests>?
) {
    if (!values.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamilyLato,
                color = LightBlack60,
                lineHeight = 24.sp,
            )

            FlowRow(
                Modifier
                    .padding(start = 130.dp),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                values.forEachIndexed { index, item ->
                    Text(
                        text = item.dropdownInterests?.name ?: "",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyLato,
                        color = LightBlack,
                        lineHeight = 22.sp,
                        modifier = Modifier
                    )
                    if (index != values.lastIndex) {
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(3.dp)
                                .background(LightBlack55)
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                }
            }
        }
    }
}
