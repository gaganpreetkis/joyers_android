package com.joyersapp.feature.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.common_widgets.IdentificationDialog
import com.joyersapp.components.dialogs.BirthdayDatePickerDialog
import com.joyersapp.components.dialogs.BulletRow
import com.joyersapp.components.dialogs.DescriptionDialog
import com.joyersapp.components.dialogs.MultipleSelectionsDialog
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.components.dialogs.HighlightBullet
import com.joyersapp.components.dialogs.LanguageSelectionDialog
import com.joyersapp.components.dialogs.MentionJoyersDialog
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Language
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileLanguagesData
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.SubLanguageWrapper
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayInnerBorder
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.UiText
import com.joyersapp.utils.convertDate
import com.joyersapp.utils.filteredBio
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.graphemeCount
import com.joyersapp.utils.highlightWords
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.toHighlightBullets
import kotlin.collections.isNullOrEmpty

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
    navigateToDescriptionDialog: (List<ProfileTitlesData>) -> Unit,
    navigateToProfileHeaderDialog: () -> Unit,
    navigateToMentionJoyersDialog: () -> Unit,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val magneticsData = state.magneticsData

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when(event) {
                is UserProfileNavigationEvent.NavigateToUserProfile -> { onBack() }
                is UserProfileNavigationEvent.NavigateToProfileHeaderDialog -> {
                    navigateToProfileHeaderDialog()
                }
                is UserProfileNavigationEvent.NavigateToMentionJoyersDialog -> {
                    navigateToMentionJoyersDialog()
                }
                is UserProfileNavigationEvent.NavigateToDescriptionDialog -> {
                    navigateToDescriptionDialog(event.list)
                }
                is UserProfileNavigationEvent.NavigateToIdentificationDialog -> { onBack() }
            }
        }
    }

    HardBlockingLoader(state.isLoading)
//    if (state.isLoading) {
//        CustomProgressIndicator()
//    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GrayBG)
        ) {

            /** ─────────────── TOP BAR ─────────────── **/
            TopBar(
                username = magneticsData.username,
                onBack = { onBack() },
                onSave = { viewModel.onEvent(UserProfileEvent.UpdateUserData(UserProfileGraphRequestDto())) }
            )

            HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)

            // Scrollable column
            LazyColumn (
                Modifier
            ) {

                item {
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)


                    /** ─────────────── SECTION: PROFILE HEADER ─────────────── **/
                    ProfileHeaderSection( magneticsData.profileHeaderData) {
                        viewModel.onEvent(UserProfileEvent.ToggleProfileHeaderDialog(true))
                    }
                }

                item {
                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)


                    /** ─────────────── SECTION: DESCRIPTION ─────────────── **/
                    val headers = arrayListOf("Description", "Joyer Status", magneticsData.joyerStatus)
                    if (state.title != null) headers.add(state.title?.name?: "")
                    if (state.subTitle != null) headers.add(state.subTitle?.name?: "")
                    DescriptionSection( state.magneticsData) {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleDescriptionDialog(
                                show = true,
                                titlesData = state.titles,
                            )
                        )
                    }
                }

                item {

                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)


//                    /** ─────────────── SECTION: IDENTIFICATION  ─────────────── **/
//                    IdentificationSection( magneticsData.identificationData) {
//                        viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(true))
//                    }


                }

                item {
                    Column (
                        Modifier
                            .background(White)
                            .fillMaxWidth()
                            .padding(top = 17.dp, bottom = 2.dp, start = 15.dp, end = 15.dp)
                            .noRippleClickable{ viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(true)) },
                    ) {
                        SectionHeader(title = "Identification")
                    }
                }

                val identificationEntries =
                    magneticsData.identificationData
                        ?.sortedDataList
                        ?.entries
                        ?.toList()
                        ?: emptyList()

                itemsIndexed(identificationEntries) { key, value ->

                    Column (
                        Modifier
                            .background(White)
                            .padding(top = 11.dp, bottom = 0.dp, start = 15.dp, end = 15.dp)
                            .noRippleClickable{ viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(true)) },
                    ) {
                        when(value.key) {
                            "Name","Gender","Ethnicity","Faith","Education", "Relationship", "Joyer Location" -> {

                                val first = value.key
                                val second = value.value as String?

                                if (!second.isNullOrEmpty()) {
                                    KeyValueText(
                                        first,
                                        second
                                    )
                                } else { ProfileEditableRow(title = first) }

                            }
                            "Birthday" -> {

                                val first = value.key
                                val second = convertDate(value.value as String?)

                                if (second.isNotEmpty()) {
                                    KeyValueText(
                                        first,
                                        second
                                    )
                                } else { ProfileEditableRow(title = first) }

                            }
                            "Nationality" -> {

                                if (!magneticsData.identificationData?.nationality.isNullOrEmpty()) {
                                    NationalitySection(magneticsData.identificationData.nationality!!)
                                } else {
                                    ProfileEditableRow(title = "Nationality") }
                            }
                            "Language" -> {

                                if (!magneticsData.identificationData?.selectedLanguages.isNullOrEmpty()) {
                                    LanguageSection(
                                        languages = magneticsData.identificationData.selectedLanguages!!,
                                        signLanguages = magneticsData.identificationData.selectedLanguages?.firstOrNull{ it.language?.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef")}?.sublanguages,
                                    )
                                } else {
                                    ProfileEditableRow(title = "Language") }
                            }
                            "Political Ideology" -> {

                                if (!magneticsData.identificationData?.politicalIdeology.isNullOrEmpty()) {
                                    PoliticalIdeoLogySection(
                                        magneticsData.identificationData.politicalIdeology!!
                                    )
                                } else {
                                    ProfileEditableRow(title = "Political Ideology") }
                            }
                        }
                    }
                }
                item {
                    Column (
                        Modifier
                            .background(White)
                            .fillMaxWidth()
                            .noRippleClickable{ viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(true)) },
                    ) {
                        Spacer(Modifier.height(20.dp))
                    }
                }


                item {

                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = GrayOuterBorder, thickness = 1.dp)


                    /** ─────────────── SECTION: INTERESTS ─────────────── **/
                    InterestsSection( magneticsData) {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Interests",
                                isMultiSelectEnabled = true,
                                show = true,
                                headers = arrayListOf("Interests"),
                                titlesData = state.interestList,
                                selectedIds = state.magneticsData.interests?.map { it.dropdownInterests?.id?: "" }?: emptyList()
                            )
                        )
                    }

                    Spacer(Modifier.height(45.dp))
                }



            }
        }

        if (state.showEditProfileHeaderDialog) {
            EditProfileHeaderDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleProfileHeaderDialog(false)) },
                onApply = { data ->
                    viewModel.onEvent(UserProfileEvent.OnApplyProfileHeader(data))
                    viewModel.onEvent(UserProfileEvent.ToggleProfileHeaderDialog(false))
                },
            )
        }
        if (state.showDescriptionDialog) {
            DescriptionDialog (
                initList = state.titles,
                selectedTitle = ProfileTitlesData(
                    id = state.magneticsData.title?.id?:"",
                    name = state.magneticsData.title?.name?:"",
                ),
                selectedSubTitle = ProfileTitlesData(
                    id = state.magneticsData.subTitle?.id,
                    name = state.magneticsData.subTitle?.name,
                ),
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleDescriptionDialog(false)) },
                onApply = { title, subTitle ->
                    viewModel.onEvent(UserProfileEvent.OnApplyDescription(title, subTitle))
                }
            )
        }
        if (state.showMentionJoyersDialog) {
            MentionJoyersDialog (
                initList = state.editMagneticsUserList,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleMentionJoyersDialog(false)) },
                onApply = { viewModel.onEvent(UserProfileEvent.OnApplyMentionedJoyers(it)) },
            )
        }
        if (state.showIdentificationDialog) {
            IdentificationDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false)) },
                onApply = {
                    viewModel.onEvent(UserProfileEvent.OnApplyIdentification(it))
                    viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false))
                },
            )
        }
        if (state.showMultipleSelectionsDialog) {
            MultipleSelectionsDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleMultipleSelectionsDialog(show = false)) },
                onApply = { key, value ->
                    viewModel.onEvent(UserProfileEvent.OnApplyMultipleSelections(key, value))
                    viewModel.onEvent(UserProfileEvent.ToggleMultipleSelectionsDialog(show = false))
                },
            )
        }
        if (state.showDatePickerDialog) {
            BirthdayDatePickerDialog(
                onDismiss = {  viewModel.onEvent(UserProfileEvent.ToggleDatePickerDialog(show = false)) },
                onDateSelected = {  viewModel.onEvent(UserProfileEvent.OnApplyBirthday(value = it)) }
            )
        }
        if (state.showLanguagesDialog) {
            LanguageSelectionDialog(
                initList = state.languageList,
                selectedLanguages = state.identificationData.selectedLanguages,
//                viewModel = viewModel,
                onDismiss = {  viewModel.onEvent(UserProfileEvent.ToggleLanguageDialog(show = false)) },
                onApply = { l ->   viewModel.onEvent(UserProfileEvent.OnApplyLanguage(l)) }
            )
        }
//    }
}

@Composable
fun TopBar(
    username: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(63.dp)
            .background(White)
            .padding(horizontal = 15.dp),
    ) {
        Text(
            text = "Cancel",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamilyLato,
            color = Golden,
            lineHeight = 22.sp,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .noRippleClickable() { onBack() }
        )

        // center username block
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 17.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // lock
//            Box(contentAlignment = Alignment.TopEnd) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_lock_heart_black),
//                    contentDescription = "Lock",
//                    modifier = Modifier.size(13.39.dp, 20.dp)
//                )
//            }

//            Spacer(modifier = Modifier.width(7.01.dp))

            // Username
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "@$username",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fontFamilyLato,
                    lineHeight = 22.sp,
                    color = LightBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                Spacer(Modifier.width(7.dp))
//                Image(
//                    painter = painterResource(id = R.drawable.arrowdown_lite),
//                    contentDescription = "Dropdown",
//                    modifier = Modifier.size(14.dp, 8.dp)
//                )
            }
        }

        Text(
            text = "Save",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = fontFamilyLato,
            color = Golden,
            lineHeight = 22.sp,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .noRippleClickable { onSave() }
        )
    }
}

@Composable
fun InterestsSection(state: MagneticsData, onClick: () -> Unit) {
    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 13.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onClick() },
    ) {
        SectionHeader(title = "Interests")

        Spacer(Modifier.height(13.dp))

        if (!state.interests.isNullOrEmpty()) {
            InterestsSection(state.interests?: emptyList())
        } else {
            ProfileEditableRow(title = "Joyer Interests") }
    }
}

@Composable
fun IdentificationSection(state: IdentificationData?, onClick: () -> Unit) {

    LazyColumn(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onClick() },
    ) {
        itemsIndexed(state?.dataList?.entries?.toList()!!) { key, value ->

            when(value.key) {
                "Name","Birthday","Gender","Ethnicity","Faith","Education", "Relationship" -> {

                    val first = value.key
                    val second = value.value as String?
                    Spacer(Modifier.height(11.dp))

                    if (!second.isNullOrEmpty()) {
                        KeyValueText(
                            first,
                            second
                        )
                    } else { ProfileEditableRow(title = first) }

                }
                "Nationality" -> {

                    Spacer(Modifier.height(11.dp))

                    if (!state?.nationality.isNullOrEmpty()) {
                        NationalitySection(state.nationality!!)
                    } else {
                        ProfileEditableRow(title = "Nationality") }
                }
                "Language" -> {

                    Spacer(Modifier.height(11.dp))
                    if (!state?.language.isNullOrEmpty()) {
//                        LanguageSection(languages = state.language!!, state.selectedSignLanguages)
                    } else {
                        ProfileEditableRow(title = "Language") }
                }
                "Political Ideology" -> {

                    Spacer(Modifier.height(11.dp))
                    if (!state?.politicalIdeology.isNullOrEmpty()) {
                        PoliticalIdeoLogySection(
                            state.politicalIdeology!!
                        )
                    } else {
                        ProfileEditableRow(title = "Political Ideology") }
                }

            }
        }
    }
    Spacer(Modifier.height(13.dp))




/*    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onClick() },
    ) {
        SectionHeader(title = "Identification")

        Spacer(Modifier.height(13.dp))

        if (!state?.name.isNullOrEmpty()) {
            KeyValueText(
                "Name",
                state.name?:""
            )
        } else { ProfileEditableRow(title = "Name") }

        Spacer(Modifier.height(11.dp))

        if (!state?.birthday.isNullOrEmpty()) {
            KeyValueText(
                "Birthday",
                convertDate(state.birthday)
            )
        } else { ProfileEditableRow(title = "Birthday") }

        Spacer(Modifier.height(11.dp))

        if (!state?.gender.isNullOrEmpty()) {
            KeyValueText(
                "Gender",
                state.gender?:""
            )
        } else { ProfileEditableRow(title = "Gender") }

        Spacer(Modifier.height(11.dp))

        if (!state?.nationality.isNullOrEmpty()) {
            NationalitySection(state.nationality!!)
        } else {
            ProfileEditableRow(title = "Nationality") }

        Spacer(Modifier.height(11.dp))

        if (state?.ethnicity != null) {
            KeyValueText(
                "Ethnicity",
                state.ethnicity?.name?: ""
            )
        } else {
            ProfileEditableRow(title = "Ethnicity") }

        Spacer(Modifier.height(11.dp))

        if (state?.faith != null) {
            KeyValueText(
                "Faith",
                state.faith?.name?: ""
            )
        } else { ProfileEditableRow(title = "Faith") }

        Spacer(Modifier.height(11.dp))

        if (!state?.language.isNullOrEmpty()) {
            LanguageSection(languages = state.language!!)
        } else {
            ProfileEditableRow(title = "Language") }

        Spacer(Modifier.height(11.dp))

        if (state?.education != null) {
            KeyValueText(
                "Education",
                state.education?.name?: ""
            )
        } else {
            ProfileEditableRow(title = "Education") }

        Spacer(Modifier.height(11.dp))

        if (state?.relationship != null) {
            KeyValueText(
                "Relationship",
                state.relationship?.name?: ""
            )
        } else {
            ProfileEditableRow(title = "Relationship") }

        *//*    Spacer(Modifier.height(11.dp))

          if (!state?.children.isNullOrEmpty()) {
               KeyValueText(
                   "Children",
                   state.children?: ""
               )
           } else {
               ProfileEditableRow(title = "Children") }*//*

        Spacer(Modifier.height(11.dp))

        if (!state?.politicalIdeology.isNullOrEmpty()) {
            PoliticalIdeoLogySection(
                state.politicalIdeology!!
            )
        } else {
            ProfileEditableRow(title = "Political Ideology") }

        Spacer(Modifier.height(11.dp))

        if (state?.location != null) {
            KeyValueText(
                "Joyer Location",
                state.location?.name?: ""
            )
        } else {
            ProfileEditableRow(title = "Joyer Location") }

    }*/
}

@Composable
fun DescriptionSection(state: MagneticsData, onclick: () -> Unit) {
    Column(
        Modifier
            .background(White)
            .padding(top = 17.dp, bottom = 20.dp, start = 15.dp, end = 15.dp)
            .noRippleClickable{ onclick() },
    ) {
        SectionHeader(title = "Description")
        Spacer(Modifier.height(13.dp))
        if (!(state.subTitle?.name?: state.title?.name?: state.joyerStatus).isNullOrEmpty()) {
            KeyValueText(
                "Joyer Status",
                state.subTitle?.name?: state.title?.name?: state.joyerStatus?: ""
            )
        } else { ProfileEditableRow(title = "Joyer Status") }
    }
}

@Composable
fun ProfileHeaderSection(
    state: ProfileHeaderData?,
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
        if (!state?.profilePicture.isNullOrEmpty() || !state?.backgroundPicture.isNullOrEmpty()) {
            ProfilePicture(
                state.profilePicture,
                state.backgroundPicture
            )
        } else {
            ProfileEditableRow(title = "Profile Picture")
        }
        Spacer(Modifier.height(11.dp))
        if (!state?.bio?.filteredBio().isNullOrEmpty() || !state?.websiteUrl.isNullOrEmpty()) {
            BioSection(
                selectedBioTab = state.selectedTab,
                bioText = state.bio?.filteredBio()?:"",
                linkText = state.websiteUrl?:"",
                bullets = state.bullets,
                onLinkClick = {}
            )
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
                        model = NetworkConfig.IMAGE_BASE_URL + backgroundImage,
                        contentDescription = "Header",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                /** ─────────── PROFILE IMAGE CIRCLE ─────────── **/
                if (profileImage.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(189.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(
                                width = 2.dp,
                                color = Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
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
                } else {
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
                                model = NetworkConfig.IMAGE_BASE_URL + profileImage,
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
}

@Composable
private fun BioSection(
    selectedBioTab: String,
    bioText: String,
    linkText: String,
    bullets: List<HighlightBullet>,
    onLinkClick: () -> Unit
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
                    color = GrayInnerBorder,
                    shape = RoundedCornerShape(5.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Gray20)
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 14.dp,
                    top = 10.dp,
                    bottom = 14.dp
                )
            ) {

                if (bioText.isNotEmpty()) {
                    // ----- BIO RICH TEXT -----

                    if (selectedBioTab.equals("highlights")) {
                        bullets.forEachIndexed { index, bullet ->
                            if (bullet.textValue.text.isEmpty()) return@forEachIndexed
                            BulletRowText(
                                modifier = Modifier
                                    .padding(top = if (index != 0) 5.dp else 0.dp),
                                bullet = bullet,
                            )
                        }
                    } else {
                        HighlightedText(bioText.filteredBio())
                    }
                }

                if (linkText.isNotEmpty()) {

                    Spacer(Modifier.height(7.dp))

                    // ----- LINK ROW -----
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
//                            .clickable { onLinkClick() }
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
                            fontFamily = fontFamilyLato,
                            lineHeight = 22.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BulletRowText(
    modifier: Modifier,
    bullet: HighlightBullet,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {

        // Bullet Dot
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(LightBlack)
        )

        Spacer(Modifier.width(10.dp))

        Text(
            highlightWords(bullet.textValue.text),
            modifier = Modifier,
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            color = LightBlack // we paint using AnnotatedString
        )

    }
}
@Composable
fun HighlightedText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        highlightWords(text),
        modifier = modifier,
        fontSize = 16.sp,
        fontFamily = fontFamilyLato,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Normal,
        color = LightBlack // we paint using AnnotatedString
    )
}


@Composable
private fun KeyValueText(
    key: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
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
    languages: List<Languages>,
    signLanguages: List<SubLanguageWrapper>?,
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
                    .padding(top = 8.dp)
                    .clip(CircleShape)
                    .background(LightBlack)
                    .size(6.dp)
            )
            Spacer(Modifier.width(10.dp))

            // ---- FLOW ROW WITH WRAPPED LANGUAGES ----
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxLines = if (seeAll) 100 else 4,
                itemVerticalAlignment = Alignment.CenterVertically,
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
                                    .padding(top = 9.dp, bottom = 2.dp)
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
                                text = "See Less",
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

                    if (item.language?.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef")) return@forEachIndexed

                    val name = item.language?.name?:""
                    val level = (item.language?.level?: "").trim()
                    val language = buildString {
                        append(name)
                        if (level.isNotEmpty()) append(" ($level)")
                    }

//                    Row(verticalAlignment = Alignment.CenterVertically) {

                    if (index != 0) {
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
//                    }
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NationalitySection(
    nationalties: List<Nationality>
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
                    .padding(top = 8.dp)
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
                                    .padding(top = 9.dp, bottom = 2.dp)
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
                                text = "See Less",
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
                    text = "Nationality :",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.width(10.dp))

                nationalties.forEachIndexed { index, item ->
                    val name = item.dropdownCountries?.name?: ""

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                        )

                        if (index != nationalties.lastIndex) {
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
fun PoliticalIdeoLogySection(
    values: List<PoliticalIdeology>
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
                    .padding(top = 8.dp)
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
                                    .padding(top = 9.dp, bottom = 2.dp)
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
                                text = "See Less",
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
                    text = "Political Ideology :",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = LightBlack,
                    lineHeight = 22.sp,
                )
                Spacer(Modifier.width(10.dp))

                values.forEachIndexed { index, item ->
                    val name = item.politicalIdeology?.name?: ""

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                            lineHeight = 22.sp,
                        )

                        if (index != values.lastIndex) {
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
                    .padding(top = 8.dp)
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
                                text = "See Less",
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


data class MagneticsData(
    val username: String = "",
    val profileHeaderData: ProfileHeaderData = ProfileHeaderData(),
    val joyerStatus: String? = "Classic",
    val title: ProfileMeta? = null,
    val subTitle: ProfileMeta? = null,
    val identificationData: IdentificationData? = IdentificationData(),
    var interests: List<Interests>? = null,
)

data class ProfileHeaderData(
    val profilePicture: String? = null,
    val backgroundPicture: String? = null,
    val bio: String? = null,
    val overviewFieldValue: TextFieldValue = TextFieldValue(""),
    val highlightFieldValue: TextFieldValue = TextFieldValue(text = "• ", selection = TextRange(2)),
    val websiteUrl: String? = null,
    val bioValidationError: UiText? = null,
    var selectedTab: String = "overview",
    val bullets: List<HighlightBullet> = listOf(HighlightBullet()),
    val highlightsRemainingChars: Int =  25 - (bullets.firstOrNull{it.requestFocus}?.textValue?.text?.graphemeCount()?:0),

) {
    val overviewRemainingChars: Int
        get() = 150 - overviewFieldValue.text.graphemeCount()
//    val highlightsRemainingChars: Int
//        get() =  25 - (bullets.firstOrNull{it.requestFocus}?.textValue?.text?.graphemeCount()?:0)

    val isApplyEnabled: Boolean
        get() = if (bioValidationError == null && (!profilePicture.isNullOrEmpty() || !backgroundPicture.isNullOrEmpty() || !websiteUrl.isNullOrEmpty() || overviewFieldValue.text.graphemeCount() != 0 || bullets.firstOrNull()?.textValue?.text.toString().graphemeCount() != 0)) true else false

    val maxBullets: Int
        get() = if (websiteUrl.isNullOrEmpty()) 5 else 4
}