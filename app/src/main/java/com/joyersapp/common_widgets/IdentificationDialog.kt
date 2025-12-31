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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joyersapp.R
import com.joyersapp.components.dialogs.BaseDialog
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

@Composable
fun IdentificationDialog(
    onDismiss: () -> Unit,
    onApply: (IdentificationData) -> Unit,
    onNavigateToDescription: () -> Unit,
    initialData: IdentificationData
) {

    var name by remember { mutableStateOf(initialData?.name ?: "") }
    var birthday by remember { mutableStateOf(initialData?.birthday ?: "") }
    var selectedGender by remember { mutableStateOf(initialData?.gender ?: Gender.MALE) }
    var nationality by remember { mutableStateOf(initialData?.nationality ?: "") }
    var ethnicity by remember { mutableStateOf(initialData?.ethnicity ?: "") }
    var faith by remember { mutableStateOf(initialData?.faith ?: "") }
    var language by remember { mutableStateOf(initialData?.language ?: "") }
    var education by remember { mutableStateOf(initialData?.education ?: "") }
    var relationship by remember { mutableStateOf(initialData?.relationship ?: "") }
    var politicalIdeology by remember { mutableStateOf(initialData?.politicalIdeology ?: "") }
    var joyerLocation by remember { mutableStateOf(initialData?.joyerLocation ?: "") }

    // “chips” sections (screenshot-style)
    val nationalityChips = remember {
        mutableStateListOf(
            "United States", "Kuwait", "India", "England",
            "Kuwait", "India", "England", "United States",
            "United States", "Kuwait", "India", "United States"
        )
    }
    val languageChips = remember {
        mutableStateListOf(
            "Hindi (Excellent)",
            "Japanese (Good)",
            "English (Very Good)",
            "Arabic (Basic)",
            "Spanish (Excellent)",
            "Italian (Good)"
        )
    }

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
                    value = name,
                    onValueChange = { name = it },
                    onClear = { name = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Birthday Field
                IdentificationDropdownField(
                    label = "Birthday",
                    hintText = "Joyer Birthday",
                    value = birthday,
                    onClick = { onNavigateToDescription() },
                    onClear = { birthday = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Gender Field
                GenderSelectionField(
                    selectedGender = selectedGender,
                    onSelection = { selectedGender = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nationality Field (with chips + See All)
                IdentificationMultiselectField(
                    label = "Nationality",
                    hintText = "Joyer Nationality",
                    values = nationalityChips,
                    onClear = { nationalityChips.clear() },
                    onClick = { nationality = "" },
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Ethnicity",
                    hintText = "Joyer Ethnicity",
                    value = ethnicity,
                    onClick = { onNavigateToDescription() },
                    onClear = { ethnicity = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Faith",
                    hintText = "Joyer Faith / Religion",
                    value = faith,
                    onClick = { onNavigateToDescription() },
                    onClear = { faith = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Language Field (with chips + See All)
                IdentificationMultiselectField(
                    label = "Language",
                    hintText = "Joyer Language",
                    values = languageChips,
                    onClear = { languageChips.clear() },
                    onClick = { nationality = "" },
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Education",
                    hintText = "Joyer Degree",
                    value = education,
                    onClick = { onNavigateToDescription() },
                    onClear = { education = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Relationship",
                    hintText = "Relationship Status",
                    value = relationship,
                    onClick = { onNavigateToDescription() },
                    onClear = { relationship = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Political Ideology",
                    hintText = "Joyer Ideology",
                    value = politicalIdeology,
                    onClick = { onNavigateToDescription() },
                    onClear = { politicalIdeology = "" }
                )

                Spacer(modifier = Modifier.height(20.dp))

                IdentificationDropdownField(
                    label = "Joyer Location",
                    hintText = "Joyer Location",
                    value = joyerLocation,
                    onClick = { onNavigateToDescription() },
                    onClear = { joyerLocation = "" }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ---------- APPLY BUTTON ----------
                Button (
                    onClick = { onApply(initialData) },
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

@Preview
@Composable
fun IdentificationMultiselectField(
    label: String = "label",
    hintText: String = "hint",
    values: List<String> = arrayListOf("ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd","ghhj", "bjbnmn", "iuhjk dfsd",),
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
                        maxLines = if (seeAll) 100 else 3
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
                    // ---- SEE ALL / SHOW LESS ----
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = if (seeAll) "Show Less" else "See All",
                            fontSize = 12.sp,
                            lineHeight = 22.sp,
                            color = Golden,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 9.dp)
                                .noRippleClickable() { seeAll = !seeAll }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenderSelectionField(
    selectedGender: Gender,
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
                    isSelected = selectedGender == Gender.MALE,
                    onClick = { onSelection(Gender.MALE) }
                )

                GenderOption(
                    label = "Female",
                    isSelected = selectedGender == Gender.FEMALE,
                    onClick = { onSelection(Gender.FEMALE) }
                )

                GenderOption(
                    label = "Other Gender",
                    isSelected = selectedGender == Gender.OTHER,
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
                .noRippleClickable { onClick() }
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
            .clickable { onClick() }
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
    val name: String = "",
    val birthday: String = "",
    val gender: Gender = Gender.MALE,
    val nationality: String = "",
    val ethnicity: String = "",
    val faith: String = "",
    val language: String = "",
    val education: String = "",
    val relationship: String = "",
    val politicalIdeology: String = "",
    val joyerLocation: String = "",
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

