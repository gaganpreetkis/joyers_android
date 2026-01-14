package com.joyersapp.common_widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.components.dialogs.BaseDialog
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

@Composable
fun IdentificationDialog(
    onDismiss: () -> Unit,
    viewModel: UserProfileViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val identificationData = state.identificationData

    BaseDialog(
        onDismiss = onDismiss,
        titles = arrayListOf("Identification")
    ) { dialogModifier, dialogFocusManager, maxHeight ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 35.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Name Field
                IdentificationTextField(
                    label = "Name",
                    value = identificationData.name,
                    onValueChange = { viewModel.onEvent(UserProfileEvent.OnNameChanged(value = it)) },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Name")) },
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Birthday Field
                IdentificationDropdownField(
                    label = "Birthday",
                    hintText = "Joyer Birthday",
                    value = identificationData.birthday,
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
                    selectedGender = identificationData.gender,
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
                                isMultiSelectEnabled = true,
                                show = true,
                                headers = arrayListOf("Countries List"),
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
                    values = identificationData.language,
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
                                isMultiSelectEnabled = true,
                                show = true,
                                headers = arrayListOf("Identification", "Political Ideology"),
                                titlesData = state.politicalIdeologyList,
                                selectedIds = identificationData.politicalIdeology?.map { it.dropdownPoliticalIdeology?.id?: "" }?: emptyList()
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
                                headers = arrayListOf("Countries List"),
                                titlesData = state.countryList,
                                selectedIds = if (identificationData.location != null) listOf(identificationData.location?.id?: "") else emptyList()
                            )
                        )
                    },
                    onClear = { viewModel.onEvent(UserProfileEvent.OnClearMultipleSelections(key = "Joyer Location")) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- APPLY BUTTON ----------
                Button (
                    onClick = {
                        viewModel.onEvent(UserProfileEvent.OnApplyIdentification(identificationData))
                        viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false))
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
fun IdentificationMultiselectField(
    label: String = "label",
    hintText: String = "hint",
    values: MutableList<String> = arrayListOf("ghhj", "bjbnmn", "iuhjk dfsd","ghhj"),
    onClear: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val lightBlackColor = LightBlack
    val fieldOuterBg = GrayBG
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
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, LightBlack10, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (values.isNotEmpty()) {
                            Text(
                                text = values[0],
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
                        values.forEachIndexed { index, item ->
                            val name = item

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
fun LanguagesField(
    label: String,
    hintText: String,
    values: List<Languages>?,
    onClear: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val lightBlackColor = LightBlack
    val fieldOuterBg = GrayBG
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
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, LightBlack10, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (!values.isNullOrEmpty()) {

                            val name = values[0].language?.name?:""
                            val level = values[0].language?.level?:""
                            val language = buildString {
                                append(name)
                                if (level.isNotEmpty()) {
                                    append(" ($level)")
                                }
                            }
                            Text(
                                text = language,
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
                if (!values.isNullOrEmpty() && values.size > 1) {
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
                        values.forEachIndexed { index, item ->
                            val name = item.language?.name?:""
                            val level = item.language?.level?:""
                            val language = buildString {
                                append(name)
                                if (level.isNotEmpty()) {
                                    append(" ($level)")
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = language,
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
fun NationalityField(
    label: String = "label",
    hintText: String = "hint",
    values: List<Nationality>,
    onClear: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val lightBlackColor = LightBlack
    val fieldOuterBg = GrayBG
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
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, LightBlack10, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (values.isNotEmpty()) {
                            Text(
                                text = values[0].dropdownCountries?.name?: "",
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
    val fieldOuterBg = GrayBG
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
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            Column() {
                // Inner pill container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 30.dp)
                        .background(Color.White, RoundedCornerShape(30.dp))
                        .border(1.dp, LightBlack10, RoundedCornerShape(30.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .padding(horizontal = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        if (values.isNotEmpty()) {
                            Text(
                                text = values[0].dropdownPoliticalIdeology?.name?: "",
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
                        values.forEachIndexed { index, item ->
                            val name = item.dropdownPoliticalIdeology?.name?: ""

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
    onSelection: (Gender) -> Unit = {}
) {
    Column() {
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
                .background(GrayBG, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(horizontal = 15.dp)
                .padding(top = 9.dp, bottom = 11.dp)
        ) {
            Column {
                GenderOption(
                    label = "Male",
                    isSelected = selectedGender == Gender.MALE.value,
                    onClick = { onSelection(Gender.MALE) }
                )

                GenderOption(
                    label = "Female",
                    isSelected = selectedGender == Gender.FEMALE.value,
                    onClick = { onSelection(Gender.FEMALE) }
                )

                GenderOption(
                    label = "Other Gender",
                    isSelected = selectedGender == Gender.OTHER.value,
                    onClick = { onSelection(Gender.OTHER) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectionWithChipsSection(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    chips: List<String>,
    onRemoveChip: (String) -> Unit,
    showSeeAll: Boolean,
    onSeeAllClick: () -> Unit,
) {
    val goldenColor = Golden
    val outerBg = GrayBG

    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.SemiBold,
            color = LightBlack,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(outerBg, RoundedCornerShape(6.dp))
                .border(1.dp, GrayLightBorder, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column {
                // inner pill selection input (same as other text fields)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White, RoundedCornerShape(25.dp))
                        .border(1.dp, GrayLightBorder, RoundedCornerShape(25.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppBasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            placeholder = "",
                            containerColor = Color.Transparent,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Normal,
                                color = LightBlack
                            )
                        )
                        if (value.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0E0E0))
                                    .clickable { onClear() },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cross_round_border_grey),
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                if (chips.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chips.forEach { chip ->
                            ChipWithRemove(
                                text = chip,
                                onRemove = { onRemoveChip(chip) }
                            )
                        }
                    }

                    if (showSeeAll) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "See All",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.SemiBold,
                                color = goldenColor,
                                modifier = Modifier.clickable { onSeeAllClick() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipWithRemove(
    text: String,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, GrayLightBorder, RoundedCornerShape(16.dp))
            .padding(start = 10.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            color = LightBlack
        )
        Spacer(modifier = Modifier.width(6.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_cross_round_border_grey),
            contentDescription = "Remove",
            modifier = Modifier
                .size(14.dp)
                .clickable { onRemove() }
        )
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
    val fieldOuterBg = GrayBG

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
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(15.dp)
        ) {
            // Inner pill container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .border(1.dp, LightBlack10, RoundedCornerShape(30.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppBasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        placeholder = "Joyer $label",
                        containerColor = Color.Transparent,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            color = lightBlackColor
                        )
                    )

                    if (value.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
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
    val fieldOuterBg = GrayBG

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
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, LightBlack10, RoundedCornerShape(5.dp))
                .padding(15.dp)
                .noRippleClickable { onClick() }
        ) {
            // Inner pill container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .border(1.dp, LightBlack10, RoundedCornerShape(30.dp))
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
            .clickable { if (!isSelected) onClick() }
            .padding(vertical = 3.dp)
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
    var name: String = "",
    var birthday: String = "",
    var gender: String = "",
    var ethnicity: ProfileMeta? = null,
    var faith: ProfileMeta? = null,
    var language: List<Languages>? = null,
    var education: ProfileMeta? = null,
    var relationship: ProfileMeta? = null,
    var nationality: List<Nationality>? = null,
    var politicalIdeology: List<PoliticalIdeology>? = null,
    var location: ProfileMeta? = null,
    var children: String? = null,

) {
    val dataList: HashMap<String, Any?>
        get() = hashMapOf(
            Pair("Name", name),
            Pair("Birthday", birthday),
            Pair("Gender", gender),
            Pair("Nationality", nationality),
            Pair("Ethnicity", ethnicity),
            Pair("Faith", faith),
            Pair("Language", language),
            Pair("Education", education),
            Pair("Relationship", relationship),
//            Pair("Children", ""),
            Pair("Political Ideology", politicalIdeology),
            Pair("Joyer Location", location),
        )
}

enum class Gender(val value: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}

