package com.joyersapp.feature.profile.presentation

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
import com.joyersapp.theme.LightBlack30
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable

//@Preview
//@Composable
//private fun MagneticsScreenP() {
//    MagneticsScreen(
//        onBack = {},
//    )
//}

@Composable
fun MagneticsScreen(
    viewModel: UserProfileViewModel,
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

        HorizontalDivider(color = LightBlack10, thickness = 1.dp)

        // Scrollable column
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = LightBlack10, thickness = 1.dp)

            /** ─────────────── SECTION: PROFILE HEADER ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
                    .noRippleClickable { viewModel.onEvent(UserProfileEvent.OnEditProfileHeader(0)) },
            ) {
                SectionHeader(title = "Profile Header")
                Spacer(Modifier.height(13.dp))
                if (state.profilePicture.isNotEmpty()) {
                    ProfilePicture(
                        NetworkConfig.IMAGE_BASE_URL + state.profilePicture,
                        NetworkConfig.IMAGE_BASE_URL + state.backgroundPicture
                    )
                } else {
                    ProfileEditableRow(title = "Profile Picture")
                }
                Spacer(Modifier.height(11.dp))
                if (state.profilePicture.isNotEmpty()) {
                    BioSection()
                } else { ProfileEditableRow(title = "Bio") }
            }

            HorizontalDivider(color = LightBlack10, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = LightBlack10, thickness = 1.dp)

            /** ─────────────── SECTION: DESCRIPTION ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp),
            ) {
                SectionHeader(title = "Description")
                Spacer(Modifier.height(13.dp))
                if (state.joyerStatus.isNotEmpty()) {
                    KeyValueText(
                        "Joyer Status",
                        state.joyerStatus
                    )
                } else { ProfileEditableRow(title = "Joyer Status") }
            }

            HorizontalDivider(color = LightBlack10, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = LightBlack10, thickness = 1.dp)

            /** ─────────────── SECTION: IDENTIFICATION  ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp),
            ) {
                SectionHeader(title = "Identification")

                Spacer(Modifier.height(13.dp))

                if (state.fullname.isNotEmpty()) {
                    KeyValueText(
                        "Name",
                        state.joyerStatus
                    )
                } else { ProfileEditableRow(title = "Name") }

                Spacer(Modifier.height(11.dp))

                if (state.birthday.isNotEmpty()) {
                    KeyValueText(
                        "Birthday",
                        state.birthday
                    )
                } else { ProfileEditableRow(title = "Birthday") }

                Spacer(Modifier.height(11.dp))

                if (state.gender.isNotEmpty()) {
                    KeyValueText(
                        "Gender",
                        state.gender
                    )
                } else { ProfileEditableRow(title = "Gender") }

                Spacer(Modifier.height(11.dp))

                if (state.nationality.isNotEmpty()) {
                    KeyValueText(
                        "Nationality",
                        state.nationality
                    )
                } else {
                    ProfileEditableRow(title = "Nationality") }

                Spacer(Modifier.height(11.dp))

                if (state.ethnicity.isNotEmpty()) {
                    KeyValueText(
                        "Ethnicity",
                        state.ethnicity
                    )
                } else {
                    ProfileEditableRow(title = "Ethnicity") }

                Spacer(Modifier.height(11.dp))

                if (state.faith.isNotEmpty()) {
                    KeyValueText(
                        "Faith",
                        state.faith
                    )
                } else { ProfileEditableRow(title = "Faith") }

                Spacer(Modifier.height(11.dp))

                if (state.language.isNotEmpty()) {
                    LanguageSection(languages = state.languages)
                } else {
                    ProfileEditableRow(title = "Language") }

                Spacer(Modifier.height(11.dp))

                if (state.educationName.isNotEmpty()) {
                    KeyValueText(
                        "Education",
                        state.educationName
                    )
                } else {
                    ProfileEditableRow(title = "Education") }

                Spacer(Modifier.height(11.dp))

                if (state.relationship.isNotEmpty()) {
                    KeyValueText(
                        "Relationship",
                        state.relationship
                    )
                } else {
                    ProfileEditableRow(title = "Relationship") }

                Spacer(Modifier.height(11.dp))

                if (state.children.isNotEmpty()) {
                    KeyValueText(
                        "Children",
                        state.children
                    )
                } else {
                    ProfileEditableRow(title = "Children") }

                Spacer(Modifier.height(11.dp))

                if (state.politicalIdeology.isNotEmpty()) {
                    KeyValueText(
                        "Political Ideology",
                        state.politicalIdeology
                    )
                } else {
                    ProfileEditableRow(title = "Political Ideology") }

                Spacer(Modifier.height(11.dp))

                if (state.location.isNotEmpty()) {
                    KeyValueText(
                        "Joyer Location",
                        state.location
                    )
                } else {
                    ProfileEditableRow(title = "Joyer Location") }

                /*val staticItems = listOf(
                    "Name", "Birthday", "Gender", "Nationality", "Ethnicity", "Faith",
                    "Language", "Education", "Relationship", "Children",
                    "Political Ideology", "Joyer Location"
                )

                    staticItems.forEach { item ->
                    ProfileEditableRow(title = item)
                }*/
            }

            HorizontalDivider(color = LightBlack10, thickness = 1.dp)
            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = LightBlack10, thickness = 1.dp)

            /** ─────────────── SECTION: INTERESTS ─────────────── **/
            Column(
                Modifier
                    .background(White)
                    .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp),
            ) {
                SectionHeader(title = "Interests")

                Spacer(Modifier.height(13.dp))

                if (state.areaOfInterest.isNotEmpty()) {
                    InterestsSection(state.areaOfInterest)
                } else {
                    ProfileEditableRow(title = "Joyer Interests") }
            }

            Spacer(Modifier.height(80.dp))

        }
    }

    if (state.showEditProfileHeaderDialog) {
        EditProfileHeaderDialog(
            onDismiss = {viewModel.onEvent(UserProfileEvent.OnDialogClosed(0))}
        ){  }
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


@Composable
fun ProfilePicture(
    profileImage: String?,
    backgroundImage: String?,
) {
    Column() {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LightBlack)
                    .size(6.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Profile Picture",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 19.sp,

                )
        }
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(240.dp)
                .border(1.dp, if (backgroundImage.isNullOrEmpty()) GrayLightBorder else Color.Transparent, RoundedCornerShape(5.dp)),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Gray20)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                /** ─────────── BACKGROUND IMAGE / HEADER ─────────── **/
                if (!backgroundImage.isNullOrEmpty()) {
                    AsyncImage(
                        model = backgroundImage,
                        contentDescription = "Header",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                /** ─────────── PROFILE IMAGE CIRCLE ─────────── **/
                Box(
                    modifier = Modifier
                        .size(189.dp)
                        .align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .size(189.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(
                                width = 2.dp,
                                color = Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        AsyncImage(
                            model = profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BioSection(
    bioText: String = "KMLMKLmklfmv f fd ",
    linkText: String = "www.joyers.com/USA/laid...",
    onLinkClick: () -> Unit =  {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // • Bio
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(LightBlack)
                    .size(6.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Bio",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                lineHeight = 22.sp,
            )
        }
        Spacer(Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = LightBlack10,
                    shape = RoundedCornerShape(5.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = GrayBG)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 11.dp,
                    bottom = 15.dp
                )
            ) {

                // ----- BIO RICH TEXT -----
                Text(
                    buildAnnotatedString {
                        append("Hi there! I’m using the ")

                        withStyle(
                            SpanStyle(
                                color = Color(0xFFCC8A00),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("@best ")
                        }

                        append("platform in this Globe it’s ")

                        withStyle(SpanStyle(color = Color(0xFF2680EB), fontWeight = FontWeight.Bold)) {
                            append("#Joyers ")
                        }

                        append("Network! It’s ")

                        // clickable URL www.hi.com
                        pushStringAnnotation(tag = "url", annotation = "https://www.hi.com/")
                        withStyle(SpanStyle(color = Color(0xFF2680EB))) {
                            append("www.hi.com/")
                        }
                        pop()

                        append(", an Amazing Social App.")
                    },
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = Color.Black
                )

                Spacer(Modifier.height(10.dp))

                // ----- LINK ROW -----
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onLinkClick() }
                ) {
                  /*  Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(17.dp)
                    )*/
                    Spacer(Modifier.width(6.dp))

                    Text(
                        text = linkText,
                        fontSize = 14.sp,
                        color = Color(0xFF2680EB),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Composable
private fun KeyValueText(
    key: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(LightBlack)
                .size(6.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "$key :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamilyLato,
            color = LightBlack,
            lineHeight = 22.sp,
            )
        Spacer(Modifier.width(10.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamilyLato,
            color = LightBlack,
            lineHeight = 23.sp,
            )
    }
}

@Composable
fun LanguageSection(
    languages: List<Languages>
) {
    var expanded by remember { mutableStateOf(true) }

    // How many items to show before pressing See All
    val visibleLanguages = if (expanded) languages else languages.take(6)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .clip(CircleShape)
                    .background(LightBlack)
                    .size(6.dp)
            )
            Spacer(Modifier.width(10.dp))

            // ---- FLOW ROW WITH WRAPPED LANGUAGES ----
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {

                Text(
                    text = "Language :",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.width(10.dp))

                visibleLanguages.forEachIndexed { index, item ->
                    val name = item.language?.name
                    val level = item.language?.description

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$name ($level)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                        )

                        if (index != visibleLanguages.lastIndex) {
                            Spacer(Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(LightBlack55)
                                    .size(3.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                }
            }

        }

        // ---- HEADER WITH SEE ALL / SHOW LESS ----
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (expanded) "Show Less" else "See All",
                fontSize = 15.sp,
                color = Color(0xFFCC8A00),
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(end = 3.dp)
                    .clickable { expanded = !expanded }
            )
        }
    }
}

@Composable
fun InterestsSection(
    interests: List<Interests>
) {
    var expanded by remember { mutableStateOf(true) }

    // How many items to show before pressing See All
    val visibleLanguages = if (expanded) interests else interests.take(12)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 7.dp)
                    .clip(CircleShape)
                    .background(LightBlack)
                    .size(6.dp)
            )
            Spacer(Modifier.width(10.dp))

            // ---- FLOW ROW WITH WRAPPED LANGUAGES ----
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {

                Text(
                    text = "Language :",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.width(10.dp))

                visibleLanguages.forEachIndexed { index, item ->
                    val name = item.dropdownInterests?.name
                    val level = item.dropdownInterests?.description

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$name ($level)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                        )

                        if (index != visibleLanguages.lastIndex) {
                            Spacer(Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(LightBlack55)
                                    .size(3.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                        }
                    }
                }
            }

        }

        // ---- HEADER WITH SEE ALL / SHOW LESS ----
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if (expanded) "Show Less" else "See All",
                fontSize = 15.sp,
                color = Color(0xFFCC8A00),
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(end = 3.dp)
                    .clickable { expanded = !expanded }
            )
        }
    }
}