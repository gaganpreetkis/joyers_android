package com.joyersapp.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.common_widgets.IdentificationDialog
import com.joyersapp.components.dialogs.EditDescriptionDialog
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.components.dialogs.MentionJoyersDialog
import com.joyersapp.components.layouts.CustomProgressIndicator
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.EditProfileHeaderDialogDto
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
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

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when(event) {
                is UserProfileNavigationEvent.NavigateToUserProfile -> { onBack() }
            }
        }
    }

    if (state.isLoading) {
        CustomProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GrayBG)
        ) {

            /** ─────────────── TOP BAR ─────────────── **/
            TopBar(
                username = state.username,
                onBack = { onBack() },
                onSave = { viewModel.onEvent(UserProfileEvent.UpdateUserData(UserProfileGraphRequestDto())) }
            )

            HorizontalDivider(color = LightBlack10, thickness = 1.dp)

            // Scrollable column
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = LightBlack10, thickness = 1.dp)


                /** ─────────────── SECTION: PROFILE HEADER ─────────────── **/
                ProfileHeaderSection( state) {
                    viewModel.onEvent(UserProfileEvent.ToggleProfileHeaderDialog(true))
                }

                HorizontalDivider(color = LightBlack10, thickness = 1.dp)
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = LightBlack10, thickness = 1.dp)


                /** ─────────────── SECTION: DESCRIPTION ─────────────── **/
                val headers = arrayListOf("Description", "Joyer Status", state.joyerStatus)
                if (state.title != null) headers.add(state.title?.name?: "")
                if (state.subTitle != null) headers.add(state.subTitle?.name?: "")
                DescriptionSection( state) {
                    viewModel.onEvent(
                        UserProfileEvent.ToggleDescriptionDialog(
                            true,
                            headers = headers,
                            titlesData = state.titles
                        )
                    )
                }

                HorizontalDivider(color = LightBlack10, thickness = 1.dp)
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = LightBlack10, thickness = 1.dp)


                /** ─────────────── SECTION: IDENTIFICATION  ─────────────── **/
                IdentificationSection( state) {
                    viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(true))
                }

                HorizontalDivider(color = LightBlack10, thickness = 1.dp)
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = LightBlack10, thickness = 1.dp)


                /** ─────────────── SECTION: INTERESTS ─────────────── **/
                InterestsSection( state) {
                    viewModel.onEvent(
                        UserProfileEvent.ToggleDescriptionDialog(
                            true,
                            arrayListOf("Interests"),
                            state.interestList
                        )
                    )
                }

                Spacer(Modifier.height(80.dp))

            }
        }

        if (state.showEditProfileHeaderDialog) {
            EditProfileHeaderDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleProfileHeaderDialog(false)) },
                onApply = { data->

                },
                data = EditProfileHeaderDialogDto(profilePicturePath = state.profilePicture, backgroundPicturePath = state.backgroundPicture, bio = "", websiteUrl = "")
            )
        }

        if (state.showMentionJoyersDialog) {
            MentionJoyersDialog (
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleMentionJoyersDialog(false)) }
            ){  }
        }
        if (state.showIdentificationDialog) {
            IdentificationDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false)) },
                onApply = { viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false)) },
                initialData = IdentificationData(
                    name = state.fullname,
                    birthday = state.birthday,
//                gender = null,
                    nationality = state.nationality,
                    ethnicity = state.ethnicity,
                    faith = state.faith,
                    language = state.language,
                    education = state.educationName,
                    relationship = state.relationship,
                    politicalIdeology = state.politicalIdeology,
                    joyerLocation = state.location
                )
            )
        }
        if (state.showEditDescriptionDialog) {
            EditDescriptionDialog(
                titlesData = state.titlesData,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleDescriptionDialog(false, emptyList(), emptyList()))},
                onApply = { viewModel.onEvent(UserProfileEvent.ToggleDescriptionDialog(false, emptyList(), emptyList()))},
                headers = state.dialogHeader
            )
        }
    }
}

@Composable
fun TopBar(
    username: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
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
            color = Golden,
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
                    text = username,
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
            color = Golden,
            lineHeight = 22.sp,
            modifier = Modifier.noRippleClickable { onSave() }
        )
    }
}

@Composable
fun InterestsSection(state: UserProfileUiState, onClick: () -> Unit) {
    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onClick() },
    ) {
        SectionHeader(title = "Interests")

        Spacer(Modifier.height(13.dp))

        if (state.areaOfInterest.isNotEmpty()) {
            InterestsSection(state.areaOfInterest)
        } else {
            ProfileEditableRow(title = "Joyer Interests") }
    }
}

@Composable
fun IdentificationSection(state: UserProfileUiState, onClick: () -> Unit) {
    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onClick() },
    ) {
        SectionHeader(title = "Identification")

        Spacer(Modifier.height(13.dp))

        if (state.fullname.isNotEmpty()) {
            KeyValueText(
                "Name",
                state.fullname
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

        if (state.languages.isNotEmpty()) {
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
}

@Composable
fun DescriptionSection(state: UserProfileUiState, onclick: () -> Unit) {
    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onclick() },
    ) {
        SectionHeader(title = "Description")
        Spacer(Modifier.height(13.dp))
        if (!(state.subTitle?.name?: state.title?.name).isNullOrEmpty()) {
            KeyValueText(
                "Joyer Status",
                state.subTitle?.name?: state.title?.name?: ""
            )
        } else { ProfileEditableRow(title = "Joyer Status") }
    }
}

@Composable
fun ProfileHeaderSection(
    state: UserProfileUiState,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable { onClick() },
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
        Spacer(Modifier.height(13.dp))
        if (state.profilePicture.isNotEmpty()) {
            BioSection()
        } else { ProfileEditableRow(title = "Bio") }
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
                colorFilter = ColorFilter.tint(Golden)
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
                                color = Golden,
                                fontWeight = FontWeight.SemiBold,
                            )
                        ) {
                            append("@best ")
                        }

                        append("platform in this Globe it’s ")

                        withStyle(SpanStyle(
                            color = Golden,
                            fontWeight = FontWeight.SemiBold,
                        )
                        ) {
                            append("#Joyers ")
                        }

                        append("Network! It’s ")

                        // clickable URL www.hi.com
                        pushStringAnnotation(tag = "url", annotation = "https://www.hi.com/")
                        withStyle(SpanStyle(
                            color = Golden,
                            fontWeight = FontWeight.SemiBold,
                            )
                        ) {
                            append("www.hi.com/")
                        }
                        pop()

                        append(", an Amazing Social App.")
                    },
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = LightBlack,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fontFamilyLato
                )

                Spacer(Modifier.height(10.dp))

                // ----- LINK ROW -----
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onLinkClick() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = null,
                        modifier = Modifier.height(14.5.dp).width(14.5.dp)
                    )
                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = linkText,
                        fontSize = 14.sp,
                        color = Golden,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanguageSection(
    languages: List<Languages>
) {
    var seeAll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                maxLines = if (seeAll) 100 else 4,
                overflow = FlowRowOverflow.expandOrCollapseIndicator(
                    minRowsToShowCollapse = 4,
                    expandIndicator = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "See All",
                                fontSize = 12.sp,
                                lineHeight = 22.sp,
                                color = Golden,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .noRippleClickable() { seeAll = true }
                            )
                        }
                    },
                    collapseIndicator = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "Show Less",
                                fontSize = 12.sp,
                                lineHeight = 22.sp,
                                color = Golden,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .noRippleClickable() { seeAll = false }
                            )
                        }
                    }
                )
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

                languages.forEachIndexed { index, item ->
                    val name = item.language?.name
                    val level = item.language?.description

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                        )

                        if (index != languages.lastIndex) {
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
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsSection(
    interests: List<Interests>
) {
    var seeAll by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                modifier = Modifier
                    .weight(1f),
                maxLines = if (seeAll) 100 else 4,
                overflow = FlowRowOverflow.expandOrCollapseIndicator(
                    minRowsToShowCollapse = 4,
                    expandIndicator = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "See All",
                                fontSize = 12.sp,
                                lineHeight = 22.sp,
                                color = Golden,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .noRippleClickable() { seeAll = true }
                            )
                        }
                    },
                    collapseIndicator = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "Show Less",
                                fontSize = 12.sp,
                                lineHeight = 22.sp,
                                color = Golden,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 9.dp)
                                    .noRippleClickable() { seeAll = false }
                            )
                        }
                    }
                )
            ) {

                interests.forEachIndexed { index, item ->
                    val name = item.dropdownInterests?.name
                    val level = item.dropdownInterests?.description

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                        )

                        if (index != interests.lastIndex) {
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
    }
}