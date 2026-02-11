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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.feature.profile.data.remote.dto.Language
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.SubLanguageWrapper
import com.joyersapp.feature.profile.presentation.UserProfileUiState
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack5
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

@Preview
@Composable
fun preProfile() {

//Column(
//    Modifier.fillMaxSize().background(Red)
//) {
//    Text( text = "JKHDjkh", Modifier.fillMaxWidth().background(White))
//    Spacer(modifier = Modifier.height(20.dp))
//    Text( text = "JKHDjkh", Modifier.fillMaxWidth().background(White))
//    Spacer(modifier = Modifier.height(20.dp))
//    Text( text = "JKHDjkh", Modifier.fillMaxWidth().background(White))
//    Spacer(modifier = Modifier.height(20.dp))
//
//    when(1) {
//
//        1 -> {
//            Column(Modifier.fillMaxSize().background(AvatarBorder)) {
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Red.copy(alpha = 0.5f))
//                        .weight(0.5f, fill = true),
////                    .weight(1f),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "No Identity yet!",
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = fontFamilyLato,
//                        color = LightBlack,
//                        lineHeight = 34.sp,
//                        modifier = Modifier
//                            .offset(y = -39.dp)
//                    )
//                }
//            }
//        }
//    }
//}


    LazyColumn(
        Modifier.background(Red).fillMaxSize()
    ) {
        item {
            Spacer(Modifier.height(8.dp))

            ProfileIdentitySection(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White),
//                .weight(1f, false),
                {},
                UserProfileUiState()
            )
        }
    }


}

@Composable
fun ProfileIdentitySection(
    modifier: Modifier = Modifier,
    onEditIdentity: () -> Unit = {},
    state: UserProfileUiState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
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
                lineHeight = 22.sp,
                modifier = Modifier
                    .offset(-1.dp)
            )

            Box(
                modifier = Modifier.size(35.dp)
                    .clip(CircleShape)
                    .clickable {
                        onEditIdentity()
                    }
                    .background(LightBlack5)
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

        if (
            !state.gender.isNullOrEmpty() || !state.nationality.isNullOrEmpty() || !state.ethnicity?.name.isNullOrEmpty() || !state.faith?.name.isNullOrEmpty() || !state.languages.isNullOrEmpty() || !state.education?.name.isNullOrEmpty() || !state.location?.name.isNullOrEmpty()
            ) {

            Column(
                modifier = Modifier
                    .background(White)
                    .padding(top = 10.dp, start = 15.dp, bottom = 12.dp, end = 15.dp)
            ) {
                /** -------- Key-Value Rows -------- */
                ProfileKeyValueRow(label = "Gender", value = state.gender?:"")
                NationalityField(label = "Nationality", values = state.nationality)
                ProfileKeyValueRow(label = "Ethnicity", value = state.ethnicity?.name ?: "")
                ProfileKeyValueRow(label = "Faith / Religion", value = state.faith?.name ?: "")
                LanguageField(
                    label = "Language",
                    state.selectedLanguages,
                    state.selectedLanguages?.firstOrNull{ it.language?.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef")}?.sublanguages,
                )
                ProfileKeyValueRow(label = "Education", value = state.education?.name ?: "")
                ProfileKeyValueRow(label = "Joyer Location", value = state.location?.name ?: "")
            }

            Spacer(Modifier.height(10.dp))
        }
        else {
            Column(
                Modifier
                    .background(GrayBG)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(364.dp)
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .offset(y = -39.dp)
                        , horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Identity yet!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 34.sp,
                            modifier = Modifier
                                .padding(bottom = 15.dp)
                        )
                        Text(
                            text = "Fill in your identity information to display it here.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack60,
                            lineHeight = 18.sp,
                            modifier = Modifier
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))

            }
        }


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
                modifier = Modifier.padding(start = 130.dp)
                    .align(Alignment.CenterStart)
            )
        }
    }
}
@Composable
private fun NationalityField(
    label: String,
    values: List<Nationality>?
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
                    .padding(start = 130.dp)
                    .align(Alignment.CenterStart),
//                    .widthIn(max = 230.dp),
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
    values: List<Languages>?,
    signLanguages: List<SubLanguageWrapper>?
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
                    .padding(start = 130.dp)
                    .align(Alignment.CenterStart),
                itemVerticalAlignment = Alignment.CenterVertically,
            ) {
                values.forEachIndexed { index, item ->

                    if (item.language?.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef")) return@forEachIndexed

                    if (index != 0) {
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.clip(CircleShape).size(3.dp)
                                .background(LightBlack55)
                        )
                        Spacer(Modifier.width(10.dp))
                    }

                    val name = item.language?.name ?: ""
                    val level = item.language?.level ?: ""
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
                            text = "(${level.trim()})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                            modifier = Modifier
                        )
                    }
                }

                if (!signLanguages.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.fillMaxWidth())
                    Text(
                        text = "Sign Language :",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyLato,
                        color = LightBlack,
                        lineHeight = 22.sp,
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    signLanguages.forEachIndexed { index, item ->
                        val name = item.sublanguage?.name?:""
                        val level = (item.sublanguage?.level?:"").trim()

//                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (index != 0 ) {
                                Spacer(Modifier.width(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(LightBlack55)
                                        .size(3.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                            }
                            Text(
                                text = name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = fontFamilyLato,
                                color = LightBlack,
                                lineHeight = 22.sp,
                            )
                            if (level.isNotEmpty()) {
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = "($level)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = fontFamilyLato,
                                    color = LightBlack,
                                    lineHeight = 22.sp,
                                )
                            }

//                        }
                    }
                }
            }

        }
    }
}