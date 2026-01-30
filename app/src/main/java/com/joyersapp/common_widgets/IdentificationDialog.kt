package com.joyersapp.common_widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.auth.presentation.identity.IdentityEvent
import com.joyersapp.components.dialogs.BaseDialog
import com.joyersapp.feature.profile.data.remote.dto.Language
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.theme.Golden
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.SubLanguageWrapper
import com.joyersapp.theme.GrayBG5
import com.joyersapp.theme.GrayInnerBorder
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.Red
import com.joyersapp.utils.convertDate
import com.joyersapp.utils.graphemeCount

@Composable
fun IdentificationDialog(
    onDismiss: () -> Unit,
    onApply: (IdentificationData) -> Unit,
    viewModel: UserProfileViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val identificationData = state.identificationData

    BaseDialog(
        onDismiss = onDismiss,
        titles = arrayListOf("Identification")
    ) { dialogModifier, dialogFocusManager, maxHeight, listState ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 35.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Spacer(Modifier.height(5.dp))
                // Name Field
                IdentificationTextField(
                    label = "Name",
                    value = identificationData.name?:"",
                    onValueChange = { viewModel.onEvent(UserProfileEvent.OnNameChanged(value = it)) },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Name")) },
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Birthday Field
                IdentificationDropdownField(
                    label = "Birthday",
                    hintText = "Joyer Birthday",
                    value = convertDate(identificationData.birthday),
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleDatePickerDialog(
                                show = true,
                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Birthday")) },
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Gender Field
                GenderSelectionField(
                    selectedGender = identificationData.gender?:"",
                    sortedGenderList = identificationData.sortedGenderList,
                    onSelection = { viewModel.onEvent(UserProfileEvent.OnGenderSelected(it.value)) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nationality Field (with chips + See All)
                NationalityField(
                    label = "Nationality",
                    hintText = "Joyer Nationality",
                    values = identificationData.nationality?: emptyList(),
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Nationality")) },
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Nationality",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Country"),
                                titlesData = state.countryList,
                                selectedIds = identificationData.nationality?.map { it.dropdownCountries?.id?: "" }?: emptyList()
                            )
                        )
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Ethnicity",
                    hintText = "Joyer Ethnicity",
                    value = identificationData.ethnicity?.name?: "",
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Ethnicity",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Identification", "Ethnicity"),
                                titlesData = state.ethenicityList,
                                selectedIds = if (identificationData.ethnicity != null) listOf(identificationData.ethnicity?.id?: "") else emptyList()
                            )
                        )
                              },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Ethnicity")) },
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Faith",
                    hintText = "Joyer Faith / Religion",
                    value = identificationData.faith?.name?: "",
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Faith",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Identification", "Faith"),
                                titlesData = state.faithReligionList,
                                selectedIds = if (identificationData.faith != null) listOf(identificationData.faith?.id?: "") else emptyList()

                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Faith")) },
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Language Field (with chips + See All)
                LanguagesField(
                    label = "Language",
                    hintText = "Joyer Language",
                    languages = identificationData.selectedLanguages,
                    signLanguages = identificationData.selectedLanguages?.firstOrNull{ it.language?.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef")}?.sublanguages,
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Language")) },
                    onClick = {
                        viewModel.onEvent(UserProfileEvent.ToggleLanguageDialog(show = true))
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Education",
                    hintText = "Joyer Degree",
                    value = identificationData.education?.name?: "",
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Education",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Identification", "Education"),
                                titlesData = state.educationList,
                                selectedIds = if (identificationData.education != null) listOf(identificationData.education?.id?: "") else emptyList()
                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Education")) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Relationship",
                    hintText = "Relationship Status",
                    value = identificationData.relationship?.name?: "",
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Relationship",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Identification", "Relationship"),
                                titlesData = state.relationShipList,
                                selectedIds = if (identificationData.relationship != null) listOf(identificationData.relationship?.id?: "") else emptyList()
                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Relationship")) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                PoliticalIdeologyField (
                    label = "Political Ideology",
                    hintText = "Joyer Ideology",
                    values = identificationData.politicalIdeology?: arrayListOf(),
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                key = "Political Ideology",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Identification", "Political Ideology"),
                                titlesData = state.politicalIdeologyList,
                                selectedIds = identificationData.politicalIdeology?.map { it.politicalIdeology?.id?: "" }?: emptyList()
                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Political Ideology")) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Joyer Location",
                    hintText = "Joyer Location",
                    value = identificationData.location?.name?: "",
                    onClick = {
                        viewModel.onEvent(
                            UserProfileEvent.ToggleMultipleSelectionsDialog(
                                "Joyer Location",
                                isMultiSelectEnabled = false,
                                show = true,
                                headers = arrayListOf("Country"),
                                titlesData = state.countryList,
                                selectedIds = if (identificationData.location != null) listOf(identificationData.location?.id?: "") else emptyList()
                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Joyer Location")) }
                )

                Spacer(modifier = Modifier.height(30.dp))

                // ---------- APPLY BUTTON ----------
                Button (
                    onClick = {
                        onApply(identificationData)
                              },
                    modifier = Modifier
                        .width(190.dp)
                        .align(Alignment.CenterHorizontally)
                        .height(47.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Golden),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Apply",
                        fontSize = 16.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyLato,
                        color = White
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanguagesField(
    label: String,
    hintText: String,
    languages: List<Languages>?,
    signLanguages: List<SubLanguageWrapper>?,
    onClear: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val lightBlackColor = LightBlack
    var seeAll by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = lightBlackColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Outer field container (light grey rectangle)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .noRippleClickable { onClick() }
                .background(GrayBG5, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(horizontal = 15.dp)
                .padding(top = 15.dp, bottom = 13.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, GrayInnerBorder, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (!languages.isNullOrEmpty()) {
                            Row(
                                modifier = Modifier.weight(1f)
                            ) {
                                val name = languages[0].language?.name?:""
                                val level = languages[0].language?.level?:""
                                Text(
                                    text = name,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    fontFamily = fontFamilyLato,
                                    fontWeight = FontWeight.Bold,
                                    color = LightBlack,
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
                                if (languages.size > 1) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+${(languages.size - 1)}",
                                        fontSize = 16.sp,
                                        lineHeight = 22.sp,
                                        fontFamily = fontFamilyLato,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Golden,
                                    )
                                }
                            }

                            Image(
                                painter = painterResource(id = R.drawable.ic_cross_round_gray),
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .size(15.dp)
                                    .noRippleClickable { onClear() },
                            )
                        } else {
                            Text(
                                text = hintText,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Normal,
                                color = LightBlack60,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Image(
                                painter = painterResource(id = R.drawable.arrowdown_lite),
                                contentDescription = "Drop down",
                                modifier = Modifier
                                    .size(10.49.dp, 6.dp)
                            )
                        }
                    }
                }
                if ((!languages.isNullOrEmpty() && languages.size > 1 || !signLanguages.isNullOrEmpty())) {
                    Spacer(Modifier.height(15.dp))
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
                        languages?.forEachIndexed { index, item ->

                            if (item.language?.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef")) return@forEachIndexed

                            val name = item.language?.name?:""
                            val level = (item.language?.level?:"").trim()
                            val language = buildString {
                                append(name)
                                if (level.isNotEmpty()) {
                                    append(" ($level)")
                                }
                            }

//                            Row(verticalAlignment = Alignment.CenterVertically) {

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
                                    fontWeight = FontWeight.Bold,
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
//                            }
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

//                                Row(verticalAlignment = Alignment.CenterVertically) {
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

//                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NationalityField(
    label: String = "label",
    hintText: String = "hint",
    values: List<Nationality>,
    onClear: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val lightBlackColor = LightBlack
    var seeAll by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = lightBlackColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Outer field container (light grey rectangle)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .noRippleClickable { onClick() }
                .background(GrayBG5, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(horizontal = 15.dp)
                .padding(top = 15.dp, bottom = 13.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, GrayInnerBorder, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (values.isNotEmpty()) {
                            Row(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = values[0].dropdownCountries?.name?: "",
                                    fontSize = 16.sp,
                                    lineHeight = 23.sp,
                                    fontFamily = fontFamilyLato,
                                    fontWeight = FontWeight.Normal,
                                    color = LightBlack,
                                )
                                if (values.size > 1) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+${(values.size - 1)}",
                                        fontSize = 16.sp,
                                        lineHeight = 22.sp,
                                        fontFamily = fontFamilyLato,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Golden,
                                    )
                                }
                            }

                            Image(
                                painter = painterResource(id = R.drawable.ic_cross_round_gray),
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .size(15.dp)
                                    .noRippleClickable { onClear() },
                            )
                        } else {
                            Text(
                                text = hintText,
                                fontSize = 16.sp,
                                lineHeight = 23.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Normal,
                                color = LightBlack60,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Image(
                                painter = painterResource(id = R.drawable.arrowdown_lite),
                                contentDescription = "Drop down",
                                modifier = Modifier
                                    .size(10.49.dp, 6.dp)
                            )
                        }
                    }
                }
                if (values.size > 1) {
                    Spacer(Modifier.height(15.dp))
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
                        values.forEachIndexed { index, item ->
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
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PoliticalIdeologyField(
    label: String = "label",
    hintText: String = "hint",
    values: List<PoliticalIdeology>,
    onClear: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val lightBlackColor = LightBlack
    var seeAll by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = lightBlackColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Outer field container (light grey rectangle)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 60.dp)
                .noRippleClickable { onClick() }
                .background(GrayBG5, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(horizontal = 15.dp)
                .padding(top = 15.dp, bottom = 13.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, GrayInnerBorder, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (values.isNotEmpty()) {
                            Row(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = values[0].politicalIdeology?.name?: "",
                                    fontSize = 16.sp,
                                    lineHeight = 23.sp,
                                    fontFamily = fontFamilyLato,
                                    fontWeight = FontWeight.Normal,
                                    color = LightBlack,
                                )
                                if (values.size > 1) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+${(values.size - 1)}",
                                        fontSize = 16.sp,
                                        lineHeight = 22.sp,
                                        fontFamily = fontFamilyLato,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Golden,
                                    )
                                }
                            }

                            Image(
                                painter = painterResource(id = R.drawable.ic_cross_round_gray),
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .size(15.dp)
                                    .noRippleClickable { onClear() },
                            )
                        } else {
                            Text(
                                text = hintText,
                                fontSize = 16.sp,
                                lineHeight = 23.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Normal,
                                color = LightBlack60,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Image(
                                painter = painterResource(id = R.drawable.arrowdown_lite),
                                contentDescription = "Drop down",
                                modifier = Modifier
                                    .size(10.49.dp, 6.dp)
                            )
                        }
                    }
                }
                if (values.size > 1) {
                    Spacer(Modifier.height(15.dp))
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
    }
}

@Composable
fun GenderSelectionField(
    selectedGender: String,
    sortedGenderList: LinkedHashMap<String, Gender>,
    onSelection: (Gender) -> Unit = {}
) {
    Column(
    ) {
        Text(
            text = "Gender",
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = LightBlack,
        )
        Spacer(Modifier.height(10.dp))

        // Outer container (as per design)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GrayBG5, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(horizontal = 15.dp)
                .padding(top = 7.dp, bottom = 11.dp)
        ) {
            LazyColumn(
                Modifier.height(90.dp)
            ) {
                itemsIndexed(sortedGenderList.entries.toList()) { index, item ->
                    GenderOption(
                        label = item.value.value,
                        isSelected = selectedGender == item.value.value,
                        onClick = { onSelection(item.value) }
                    )
                }
//                GenderOption(
//                    label = "Male",
//                    isSelected = selectedGender == Gender.MALE.value,
//                    onClick = { onSelection(Gender.MALE) }
//                )
//
//                GenderOption(
//                    label = "Female",
//                    isSelected = selectedGender == Gender.FEMALE.value,
//                    onClick = { onSelection(Gender.FEMALE) }
//                )
//
//                GenderOption(
//                    label = "Other Gender",
//                    isSelected = selectedGender == Gender.OTHER.value,
//                    onClick = { onSelection(Gender.OTHER) }
//                )
            }
        }
    }
}

@Composable
fun IdentificationTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val lightBlackColor = LightBlack

    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = lightBlackColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Outer field container (light grey rectangle)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(GrayBG5, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            // Inner pill container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .border(1.dp, GrayInnerBorder, RoundedCornerShape(30.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var isFocused by remember { mutableStateOf(false) }
                    AppBasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .offset(y = -1.dp)
                            .focusRequester(remember { FocusRequester() })
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            },
                        placeholder = "Joyer $label",
                        containerColor = Color.Transparent,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = lightBlackColor
                        ),
                        maxLength = 45
                    )


                    if (isFocused || value.isEmpty()) {
                        Spacer(modifier = Modifier.width(10.dp))
                        val remainingChars = (45 - value.graphemeCount())
                        Text(
                            text = remainingChars.toString(),
                            fontSize = 12.sp,
                            color = if (remainingChars < 0) Red else LightBlack60,
                            modifier = Modifier.fillMaxHeight().padding(top = 4.dp, end = 14.dp),
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 24.sp,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    } else {
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(end = 15.dp)
                                .size(15.dp)
                                .clickable { onClear() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_cross_round_gray),
                                contentDescription = "Clear",
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IdentificationDropdownField(
    label: String,
    hintText: String,
    value: String,
    onClick: () -> Unit,
    onClear: () -> Unit
) {
    val lightBlackColor = LightBlack

    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Bold,
            color = lightBlackColor,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Outer field container (light grey rectangle)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(GrayBG5, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(15.dp)
                .noRippleClickable { onClick() }
        ) {
            // Inner pill container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .border(1.dp, GrayInnerBorder, RoundedCornerShape(30.dp))
                    .noRippleClickable { onClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp)
                        .noRippleClickable { onClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (value.isNotEmpty()) {
                        Text(
                            text = value,
                            fontSize = 16.sp,
                            lineHeight = 23.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = LightBlack,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_cross_round_gray),
                            contentDescription = "Clear",
                            modifier = Modifier
                                .size(15.dp)
                                .noRippleClickable { onClear() },
                        )
                    } else {
                        Text(
                            text = hintText,
                            fontSize = 16.sp,
                            lineHeight = 23.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = LightBlack60,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(
                            painter = painterResource(id = R.drawable.arrowdown_lite),
                            contentDescription = "Drop down",
                            modifier = Modifier
                                .size(10.49.dp, 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenderOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val goldenColor = Golden
    val lightBlackColor = LightBlack

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .clickable { if (!isSelected) onClick() }
//            .padding(vertical = 3.dp)
        , contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) goldenColor else lightBlackColor
        )
    }
}

data class IdentificationData(
    var name: String? = null,
    var birthday: String? = null,
    var gender: String? = null,
    var ethnicity: ProfileMeta? = null,
    var faith: ProfileMeta? = null,
    var language: List<Languages>? = null,
    var selectedLanguages: List<Languages>? = null,
//    var selectedSignLanguages: List<Languages>? = null,
    var education: ProfileMeta? = null,
    var relationship: ProfileMeta? = null,
    var nationality: List<Nationality>? = null,
    var politicalIdeology: List<PoliticalIdeology>? = null,
    var location: ProfileMeta? = null,
    var children: String? = null,

) {

    val genderList: LinkedHashMap<String, Gender>
        get() = linkedMapOf(
            Pair("Male", Gender.MALE),
            Pair("Female", Gender.FEMALE),
            Pair("Other Gender", Gender.OTHER),
        )

    val sortedGenderList: LinkedHashMap<String, Gender> =
        genderList.entries
            .partition { it.value.value.equals(gender) }
            .let { (selected, unSelected) ->
                (selected + unSelected)
                    .associate { it.key to it.value }
            }
            .toMap(LinkedHashMap())
    val dataList: LinkedHashMap<String, Any?>
        get() = linkedMapOf(
            Pair("Name", name),
            Pair("Birthday", birthday),
            Pair("Gender", gender),
            Pair("Nationality", nationality),
            Pair("Ethnicity", ethnicity?.name),
            Pair("Faith", faith?.name),
            Pair("Language", selectedLanguages),
            Pair("Education", education?.name),
            Pair("Relationship", relationship?.name),
//            Pair("Children", ""),
            Pair("Political Ideology", politicalIdeology),
            Pair("Joyer Location", location?.name),
        )

    val sortedDataList: LinkedHashMap<String, Any?> =
        dataList.entries
            .partition { it.value != null }
            .let { (nonNull, nulls) ->
                (nonNull + nulls)
                    .associate { it.key to it.value }
            }
            .toMap(LinkedHashMap())
}

enum class Gender(val value: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other Gender")
}

