package com.joyersapp.feature.profile.presentation.identity

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.presentation.UserProfileUiState
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack5
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

//@Preview
//@Composable
//fun preProfile() {
//    ProfileIdentitySection()
//}

@Composable
fun ProfileIdentitySection(
    onEditIdentity: () -> Unit = {},
    modifier: Modifier = Modifier,
    state: UserProfileUiState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(GrayBG)
    ) {
        Spacer(Modifier.height(8.dp))

        /** -------- Identification Header -------- */
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 15.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Identification",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp
            )

            Box(
                modifier = Modifier.size(35.dp)
                    .clip(CircleShape)
                    .clickable {
                        onEditIdentity()
                    }
                    .background(LightBlack5)
                    .padding(start = 10.dp, top = 10.dp, end = 9.3.dp, bottom = 9.38.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_edit_pencil),
                    contentDescription = "Edit",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .background(White)
                .padding(top = 0.dp, start = 15.dp, bottom = 20.dp, end = 15.dp)
        ) {
            /** -------- Key-Value Rows -------- */
            ProfileKeyValueRow(label = "Gender", value = state.gender)
            NationalityField(label = "Nationality", values = state.nationality)
            ProfileKeyValueRow(label = "Ethnicity", value = state.ethnicity?.name?:"")
            ProfileKeyValueRow(label = "Faith / Religion", value = state.faith?.name?:"")
            LanguageField(label = "Language", state.languages)
            ProfileKeyValueRow(label = "Education", value = state.education?.name?:"")
            ProfileKeyValueRow(label = "Joyer Location", value = state.location?.name?:"")
        }

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
                .padding(top = 20.dp)
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
                    .widthIn(max = 250.dp),
            )
        }
    }
}
@Composable
private fun NationalityField(
    label: String,
    values: List<Nationality>
) {
    if (values.isNotEmpty()) {
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
                    val name = item.dropdownCountries?.name ?: ""
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyLato,
                        color = LightBlack,
                        lineHeight = 22.sp,
                        modifier = Modifier
                    )
                    if (index != values.size - 1) {
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.clip(CircleShape).size(3.dp)
                                .background(LightBlack55)
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                }
            }
        }
    }
}
@Composable
private fun LanguageField(
    label: String,
    values: List<Languages>
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
                val name = item.language?.name?: ""
                val level = item.language?.level?:""
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                    modifier = Modifier
                )
                if (level.trim().isNotEmpty()) {
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = level.trim(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fontFamilyLato,
                        color = LightBlack,
                        lineHeight = 22.sp,
                        modifier = Modifier
                    )
                }
                if (index != values.size - 1) {
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