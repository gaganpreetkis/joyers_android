package com.joyersapp.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.auth.presentation.SplashVideoViewModel
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack30
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable

@Preview
@Composable
private fun MagneticsScreenP() {
    MagneticsScreen(
        onBack = {},
    )
}

@Composable
fun MagneticsScreen(
    viewModel: UserProfileViewModel = hiltViewModel<UserProfileViewModel>(),
                    onBack: () -> Unit,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrayBG)
    ) {

        /** ─────────────── TOP BAR ─────────────── **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(63.dp)
                .background(White)
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                color = Golden60,
                lineHeight = 22.sp,
                modifier = Modifier.noRippleClickable() { onBack() }
            )

            // center username block
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // lock
                Box(contentAlignment = Alignment.TopEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lock_heart_black),
                        contentDescription = "Lock",
                        modifier = Modifier.size(13.39.dp, 20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(7.01.dp))

                // Username
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.username,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        lineHeight = 22.sp,
                        color = LightBlack,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(7.dp))
                    Image(
                        painter = painterResource(id = R.drawable.arrowdown_lite),
                        contentDescription = "Dropdown",
                        modifier = Modifier.size(14.dp, 8.dp)
                    )
                }
            }

            Text(
                text = "Save",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                color = Golden60,
                lineHeight = 22.sp,
                modifier = Modifier.noRippleClickable { onBack() }
            )
        }

        Divider(color = LightBlack30, thickness = 1.dp)

        // Scrollable column
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
            ) {
            Spacer(Modifier.height(10.dp))
            Divider(color = LightBlack30, thickness = 1.dp)

            /** ─────────────── SECTION: PROFILE HEADER ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
                    .noRippleClickable {  },
            ) {
                SectionHeader(title = "Profile Header")
                Spacer(Modifier.height(13.dp))
                ProfileEditableRow(title = "Profile Picture")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Bio")
            }

            Divider(color = LightBlack30, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            Divider(color = LightBlack30, thickness = 1.dp)

            /** ─────────────── SECTION: DESCRIPTION ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp),
            ) {
                SectionHeader(title = "Description")
                Spacer(Modifier.height(13.dp))
                ProfileEditableRow(title = "Joyer Status")
            }

            Divider(color = LightBlack30, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            Divider(color = LightBlack30, thickness = 1.dp)

            /** ─────────────── SECTION: IDENTIFICATION  ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp),
            ) {
                SectionHeader(title = "Identification")
                Spacer(Modifier.height(13.dp))
                ProfileEditableRow(title = "Name")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Birthday")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Gender")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Nationality")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Ethnicity")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Faith")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Language")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Education")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Relationship")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Children")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Political Ideology")
                Spacer(Modifier.height(11.dp))
                ProfileEditableRow(title = "Joyer Location")

                /*val staticItems = listOf(
                    "Name", "Birthday", "Gender", "Nationality", "Ethnicity", "Faith",
                    "Language", "Education", "Relationship", "Children",
                    "Political Ideology", "Joyer Location"
                )

                    staticItems.forEach { item ->
                    ProfileEditableRow(title = item)
                }*/
            }

            Divider(color = LightBlack30, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            Divider(color = LightBlack30, thickness = 1.dp)

            /** ─────────────── SECTION: INTERESTS ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp),
            ) {
                SectionHeader(title = "Interests")
                Spacer(Modifier.height(13.dp))
                ProfileEditableRow(title = "Joyer Interests")
            }

            Spacer(Modifier.height(80.dp))

        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = fontFamilyLato,
        color = LightBlack,
        lineHeight = 22.sp,
    )
}

@Composable
fun ProfileEditableRow(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(23.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_forward_black),
                contentDescription = null,
                modifier = Modifier.size(6.dp, 10.5.dp),
                colorFilter = ColorFilter.tint(Golden60)
            )
            Spacer(Modifier.width(9.9.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamilyLato,
                color = LightBlack60,
                lineHeight = 22.sp,
            )
        }
    }
}