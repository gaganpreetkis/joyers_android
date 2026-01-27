package com.joyersapp.components.dialogs

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isScrollingUp
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import kotlinx.coroutines.launch

//@Preview
@Composable
fun LanguageSelectionDialog(
    viewModel: UserProfileViewModel,
    onDismiss: () -> Unit,
    onApply: (List<ProfileTitlesData>) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val key = state.key
    val isMultiselectEnabled = true
    val languageList = state.titlesData
    val headers = arrayListOf("Identification", "Language")

    var searchQuery by remember { mutableStateOf("") }
    var currentList by remember { mutableStateOf(languageList) }

// Derived states (calculated efficiently)
    val filteredTitles by remember(searchQuery, currentList) {
        derivedStateOf {
            currentList.filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    val reorderedTitles by remember(filteredTitles) {
        derivedStateOf {
            val selected = filteredTitles.filter { it.isSelected }
            val unselected = filteredTitles.filter { !it.isSelected }

            // selected first, then normal
            selected + unselected
        }
    }

    LanguagesDialog(
        onDismiss = onDismiss,
        onApply = { onApply( currentList.filter { it.isSelected }) },
        showApplyButton = currentList.any { it.isSelected },
        headers = headers,
        searchQuery = searchQuery,
        onSearchQueryChanged = { query ->
            searchQuery = query
//            CoroutineScope(Dispatchers.Default).launch {
//                itemsList =
//                    itemsList2.filter { it.name?.contains(query, ignoreCase = true) ?: false }
//            }
        },
        titlesData = reorderedTitles,
        onShowSubTitles = { list ->
            currentList = list
        },
        onTitleSelected = { titleId ->
            if (isMultiselectEnabled) {
                currentList = currentList.map { item ->
                    if (item.id == titleId) item.copy(
                        isSelected = !item.isSelected,
                    )
                    else item
                }
            } else {
                currentList = currentList.map { item ->
                    if (item.id == titleId) item.copy(isSelected = !item.isSelected)
                    else item.copy(isSelected = false)
                }
            }
        },
        onLanguageLevelSelected = { id, level ->
            currentList = currentList.map { item ->
                if (item.id?.contains(id) == true) {
                    if (level.isEmpty()) {
                        item.copy(
                            isSelectionMode = true,
                        )
                    } else {
                        item.copy(
                            level = level,
                            isSelectionMode = false,
                        )
                    }
                }
                else item
            }
        },
        onBack = {
            currentList = languageList
        }
    )
}

@Composable
fun LanguagesDialog(
    onDismiss: () -> Unit,
    headers: List<String>,
    searchQuery: String,
    onSearchQueryChanged: (query: String) -> Unit,
    titlesData: List<ProfileTitlesData>,
    showApplyButton: Boolean = false,
    onShowSubTitles: (List<ProfileTitlesData>) -> Unit,
    onTitleSelected: (String) -> Unit,
    onLanguageLevelSelected: (String, String) -> Unit,
    onBack: () -> Unit,
    onApply: () -> Unit
) {

    val context = LocalContext.current
    var showBackButton by remember { mutableStateOf(false) }
    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val goldenColor = Golden
    val lightBlackColor = LightBlack
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isScrollingUp = listState.isScrollingUp()
    var recentSelectedItemId by remember {
        mutableStateOf("")
    }


    BaseDialog(
        onDismiss = onDismiss,
        titles = headers,
        onBack = {
            showBackButton = false
            onBack()
        },
        showBackButton = showBackButton
    ) { dialogModifier, dialogFocusManager, maxHeight ->

        Spacer(modifier = dialogModifier.height(15.dp))

        // Use BoxWithConstraints to get the maximum height available within the Card/Dialog

            Column(
                modifier = Modifier
                    .animateContentSize(animationSpec = tween(durationMillis = 3, delayMillis = 30))
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 25.dp)
                    .heightIn(max = maxHeight)
            ) {

                // First Scrollable
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 3,
                                delayMillis = 30
                            )
                        )
                        .weight(1f, fill = false)
                        .fillMaxWidth()
                ) {
                    item {
                        SearchBarRow(
                            searchQuery = searchQuery,
                            showApplyButton = showApplyButton,
                            onApply = { onApply() },
                            onSearchQueryChanged = { onSearchQueryChanged(it) }
                        )
                        Spacer(modifier = dialogModifier.height(20.dp))
                    }

                    if (titlesData.isEmpty()) {
                        item {
                            Box(
                                modifier = dialogModifier
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = context.getString(R.string.no_results_found),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = fontFamilyLato,
                                    textAlign = TextAlign.Center,
                                    color = lightBlackColor,
                                    lineHeight = 22.sp,
                                    modifier = dialogModifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = if (isKeyBoardOpen) 95.dp else 55.dp,
                                            bottom = if (isKeyBoardOpen) 0.dp else 69.dp
                                        )
                                )
                            }
                        }
                    } else {
                        itemsIndexed(titlesData,  key = { _, item -> item.id?:"" }) { index, title ->
                            val isFirst = index == 0
                            val isLast = index == titlesData.lastIndex
//                        AnimatedContent(title.isSelected) {
                            LanguageItem(
                                isRecentSelectedItem = recentSelectedItemId == title.id,
                                isFirstItem = isFirst,
                                isLastItem = isLast,
                                language = title,
                                isSelected = title.isSelected,
                                onClick = {
//                                title.isSelected = !title.isSelected
                                    if (title.selections.isNullOrEmpty()) {

                                        recentSelectedItemId = title.id?: ""
                                        if (!title.isSelectionMode) {
                                            onLanguageLevelSelected(title.id?: "", "")
                                        } else {
                                            onTitleSelected(title.id ?: "")
                                            coroutineScope.launch {
                                                listState.animateScrollToItem(0)
                                            }
                                        }
                                    } else {
                                        showBackButton = true
                                        onShowSubTitles(title.selections ?: emptyList())
                                    }
//                                     keyboardController?.hide()
                                },
                                modifier = Modifier,
                                onLanguageLevelSelected = { onLanguageLevelSelected(title.id?: "", it) }
                            )
//                        }
                        }
                    }

                }

            }
        }

}

@Composable
private fun SearchBarRow(
    dialogModifier: Modifier = Modifier,
    searchQuery: String,
    showApplyButton: Boolean,
    onApply: () -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val goldenColor = Golden
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val whiteColor = Color.White
    // Search bar and buttons
    Row(
        modifier = dialogModifier
            .fillMaxWidth()
            .height(35.dp)
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search field with icons
        Box(
            modifier = dialogModifier
                .weight(1f)
                .height(35.dp)
                .clip(shape = RoundedCornerShape(35.dp))
                .background(
                    color = Gray20,
                    shape = RoundedCornerShape(35.dp)
                )
                .border(
                    1.dp,
                    color = GrayLightBorder,
                    shape = RoundedCornerShape(35.dp)
                )
        ) {
            Row(
                modifier = dialogModifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading search icon - positioned to match Material3 TextField icon spacing
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = dialogModifier
                        .padding(start = 16.dp, end = 0.dp)
                        .size(17.dp),
                )

                // AppBasicTextField - it has internal padding (15.dp start, 2.dp end)
                // We account for this in our layout
                AppBasicTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        onSearchQueryChanged(query)
                    },
                    placeholder = context.getString(R.string.search_speciality),
                    modifier = dialogModifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(bottom = 1.dp),
                    textStyle = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    containerColor = Color.Transparent,
                    contentColor = lightBlackColor,
                    placeholderColor = hintColor,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    maxLength = 100
                )

                // Trailing cancel icon (conditional) - account for AppBasicTextField's 2.dp end padding
                if (searchQuery.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel_grey),
                        contentDescription = null,
                        modifier = dialogModifier
                            .padding(
                                start = 10.dp,
                                end = 16.dp
                            ) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                            .size(15.dp)
                            .noRippleClickable() {
                                onSearchQueryChanged("")
                            }
                    )
                } else {
                    // Spacer to maintain consistent padding when icon is not visible
                    Spacer(modifier = dialogModifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
                }
            }
        }

        // Search/Apply button
        if (showApplyButton) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(35.dp)
                    .clip(RoundedCornerShape(35.dp))
                    .background(
                        color = goldenColor,
                        shape = RoundedCornerShape(35.dp)
                    )
                    .noRippleClickable {
                        onApply()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = context.getString(R.string.apply),
                    fontSize = 12.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(35.dp)
                    .padding(0.dp)
                    .clip(RoundedCornerShape(35.dp))
                    .background(
                        color = if (searchQuery.isEmpty()) Gray20 else whiteColor,
                        shape = RoundedCornerShape(35.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (searchQuery.isEmpty()) GrayLightBorder else goldenColor,
                        shape = RoundedCornerShape(35.dp)
                    )
                    .noRippleClickable {
                        keyboardController?.hide()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = context.getString(R.string.search),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fontFamilyLato,
                    color = if (searchQuery.isEmpty()) lightBlackColor else goldenColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }
    }
}


@Composable
fun LanguageItem(
    isFirstItem: Boolean,
    isLastItem: Boolean,
    language: ProfileTitlesData,
    isSelected: Boolean,
    isRecentSelectedItem: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLanguageLevelSelected: (String) -> Unit
) {

    val name = language.name
    val level = language.level
    val languageName = buildString {
        append(name)
        if (!level.isNullOrEmpty() && !language.isSelectionMode) {
            append(" ($level)")
        }
    }

    val context = LocalContext.current
    Log.e("is last item", "$language, islastitem: $isLastItem")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable() { onClick() }
            .padding(bottom = if (isLastItem) 0.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = languageName,
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected && language.selections.isNullOrEmpty()) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected && language.selections.isNullOrEmpty()) Golden else LightBlack,
            //modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
            //modifier = Modifier.weight(1f)
        )
        if (!language.selections.isNullOrEmpty()) {
            Spacer(modifier = modifier.width(3.dp))
            Image(
                painter = painterResource(id = R.drawable.arrowdown_lite),
                contentDescription = null,
                modifier = modifier.size(11.dp)
            )
        }
        if (!language.description.isNullOrEmpty()) {
            Spacer(modifier = modifier.width(3.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = fontFamilyLato,
                color = Golden,
                modifier = modifier
            )
        }

        if (isSelected && isRecentSelectedItem && language.isSelectionMode) {
            LanguageLevel(
                selectedLevel = language.level?: "",
                onTabClick = { onLanguageLevelSelected(it) }
            )
        }

    }
}

@Composable
private fun LanguageLevel(
    modifier: Modifier = Modifier,
    selectedLevel: String,
    onTabClick: (String) -> Unit,
) {

    // Custom LazyRow for tabs (replaces ScrollableTabRow)
    LazyRow(
//        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 15.dp),  // No edge padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tabs = listOf("Basic", "Good", "Very Good", "Excellent")
        itemsIndexed(tabs) { idx, level ->
            val isTabSelected = selectedLevel == level
            var textWidth by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current

            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(28.dp)
                    .noRippleClickable() {
                        onTabClick(level)
                    },
//                horizontalAlignment = Alignment.Center
            ) {
                Text(
                    text = level,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = fontFamilyLato,
                    color = if (isTabSelected) LightBlack else LightBlack60,
                    lineHeight = 17.sp,
                    modifier = Modifier
                        .padding(
                            start = 0.dp,
                            end = 0.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        )  // No horizontal padding
                        .height(19.dp)
                        .onGloballyPositioned { layoutCoordinates ->
                            textWidth = with(localDensity) { layoutCoordinates.size.width.toDp() }

                        }
                )
                Spacer(Modifier.height(6.dp))
                if (isTabSelected) {
                    Box(
                        modifier = Modifier
                            .width(textWidth)
                            .height(3.dp)
                            .background(Golden)
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun preview() {
//    LanguageLevel(
//        selectedLevel = "Basic",
//        onTabClick = {}
//    )
    LanguageItem (
        isFirstItem = false,
        isLastItem = false,
        language = ProfileTitlesData(
            name = "English",
            level = "Basic"
        ),
        isSelected = true,
        onClick = {  },
        onLanguageLevelSelected = {},
        isRecentSelectedItem = true
    )
}