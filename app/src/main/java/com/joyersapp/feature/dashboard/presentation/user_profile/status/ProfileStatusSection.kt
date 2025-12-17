package com.joyersapp.feature.dashboard.presentation.user_profile.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.theme.Golden60
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
    ProfileStatusSection()
}

@Composable
fun ProfileStatusSection(
    description: String = "Description",
    joyerStatus: String = "Classic",
    title: String = "Pet",
    subTitle: String = "Dogs",
    interests: List<String> = listOf("Affiliation", "Preschoolers", "Adults", "Education", "Training"),
    onEditDescription: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GrayBG)
    ) {
        Spacer(Modifier.height(16.dp))

        /** -------- Description Header -------- */
        Row(
            modifier = Modifier.fillMaxWidth()
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
                lineHeight = 22.sp
            )

            Box(
                modifier = Modifier.size(35.dp)
                    .clip(CircleShape)
                    .background(LightBlack5)
                    .padding(start = 10.dp, top = 10.dp, end = 9.3.dp, bottom = 9.38.dp)
                    .clickable {

                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_pencil),
                    contentDescription = "Edit",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .background(White)
                .padding(top = 20.dp, start = 15.dp, bottom = 29.dp, end = 15.dp)
        ) {
            /** -------- Key-Value Rows -------- */
            ProfileKeyValueRow(label = "Joyer Status", value = joyerStatus)
            Spacer(Modifier.height(20.dp))
            ProfileKeyValueRow(label = "Title", value = title)
            Spacer(Modifier.height(20.dp))
            ProfileKeyValueRow(label = "Sub-Title", value = subTitle)
            Spacer(Modifier.height(20.dp))
            KeyValueRowWithDotSeparators("Area of Interest",interests)
        }

        Spacer(Modifier.height(15.dp))

        /** -------- Joying Header -------- */
        Row(
            modifier = Modifier.fillMaxWidth()
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
                lineHeight = 22.sp
            )

            Box(
                modifier = Modifier.size(35.dp)
                    .clip(CircleShape)
                    .background(LightBlack5)
                    .padding(start = 10.dp, top = 10.dp, end = 9.3.dp, bottom = 9.38.dp)
                    .clickable {

                    }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_pencil),
                    contentDescription = "Edit",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(15.dp))

        JoyerCodeSection()

        Spacer(Modifier.height(80.dp))

    }
}

@Composable
fun ProfileKeyValueRow(
    label: String,
    value: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
            modifier = Modifier.offset(x = 130.dp)
        )
    }
}


@Composable
fun JoyerCodeSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {

        DateInfoRow(
            label = "Joying Since",
            date = "2 March 2018",
            duration = "3 Years, 10 Months, 2 Days"
        )

        Spacer(Modifier.height(19.dp))

        DateInfoRow(
            label = "Friends Since",
            date = "12 June 2019",
            duration = "2 Years, 6 Months, 11 Days"
        )

        Spacer(Modifier.height(19.dp))

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

            Column(Modifier.offset(x = 130.dp).wrapContentWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    painter = painterResource(R.drawable.dmy_profile_qr),
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(200.dp), contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Sara Spiegel James Spiem",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                )

                Text(
                    text = "James Spiegel Jade II",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                )

                Spacer(Modifier.height(7.dp))

                Text(
                    text = "@Sara_99",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = Golden60,
                    lineHeight = 15.sp,
                )
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

        Column(Modifier.offset(x = 130.dp)) {
            Text(
                text = date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp,
            )
            Spacer(Modifier.height(6.dp))
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
fun KeyValueRowWithDotSeparators(
    label: String,
    values: List<String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                .offset(x = 130.dp)
                .widthIn(max = 250.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            values.forEachIndexed { index, item ->
                Text(
                    text = item,
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
                        modifier = Modifier.clip(CircleShape).size(3.dp).background(LightBlack55)
                    )
                    Spacer(Modifier.width(10.dp))
                }
            }
        }

    }
}
