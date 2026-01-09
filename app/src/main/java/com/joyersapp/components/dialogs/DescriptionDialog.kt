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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.feature.dashboard.Routes
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isScrollingUp
import kotlinx.coroutines.launch

//@Preview
@Composable
fun DescriptionDialog(
    viewModel: UserProfileViewModel,
    onDismiss: () -> Unit,
    onApply: (ProfileMeta?, ProfileMeta?) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiStateMagnetics by viewModel.uiStateMagnetics.collectAsStateWithLifecycle()

    val isMultiselectEnabled = false
    val titlesData = state.titlesData
    var headers by remember { mutableStateOf(arrayListOf<String>()) }

    var isSubTitleMode by remember { mutableStateOf(false) }
    var selectedId by remember { mutableStateOf("") }
    var selectedTitle by remember { mutableStateOf<ProfileMeta?>(state.magneticsData.title) }
    var selectedSubTitle by remember { mutableStateOf<ProfileMeta?>(state.magneticsData.subTitle) }
    var searchQuery by remember { mutableStateOf("") }
    var currentList by remember { mutableStateOf(titlesData) }

    LaunchedEffect(key1 = isSubTitleMode) {
        if (isSubTitleMode && selectedTitle != null) {
            headers = arrayListOf("Description", "Joyer Status", "Classic", selectedTitle?.name?: "")
            selectedId = selectedSubTitle?.id?: ""
        } else {
            headers = arrayListOf("Description", "Joyer Status", "Classic")
            selectedId = selectedTitle?.id?: ""
        }
    }

    LaunchedEffect(state.magneticsData.title) {
        if (!state.magneticsData.title?.id.isNullOrEmpty()) {
            selectedId = state.magneticsData.title?.id?: ""
        }
    }
    LaunchedEffect(state.magneticsData.subTitle) {
        if (!state.magneticsData.subTitle?.id.isNullOrEmpty()) {
            selectedId = state.magneticsData.subTitle?.id?: ""
            currentList = state.titles.find() { it.id?.equals(state.magneticsData.title?.id?: "") == true }?.selections?: emptyList()
            isSubTitleMode = true
        }
    }

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
            val selected = filteredTitles.filter { it.id?.equals(selectedId) == true }
            val unselected = filteredTitles.filter { it.id?.equals(selectedId) == false }

            // selected first, then normal
            selected + unselected
        }
    }

    val clarificationTitles by remember(searchQuery) {
        derivedStateOf {
            titlesData.filter {
                !it.description.isNullOrEmpty() &&
                        it.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }
    EditDescriptionDialog(
        onDismiss = onDismiss,
        onApply = { onApply(selectedTitle, selectedSubTitle) },
        showApplyButton = true,
        showBackButton = isSubTitleMode,
        headers = headers,
        searchQuery = searchQuery,
        selectedId = selectedId,
        onSearchQueryChanged = { query ->
            searchQuery = query
//            CoroutineScope(Dispatchers.Default).launch {
//                itemsList =
//                    itemsList2.filter { it.name?.contains(query, ignoreCase = true) ?: false }
//            }

        },
        titlesData = reorderedTitles,
        clarificationData = clarificationTitles,
        onShowSubTitles = { list ->
            currentList = list
            isSubTitleMode = true
        },
        onTitleSelected = { title ->

            if (isSubTitleMode) {
                selectedSubTitle = title
                selectedId = title?.id?: ""
            } else {
                selectedTitle = title
                selectedId = title?.id?: ""
            }

        },
        onBack = {
            currentList = titlesData
            isSubTitleMode = false
            selectedSubTitle = null
        }
    )
}



@Composable
fun EditDescriptionDialog(
    onDismiss: () -> Unit,
    headers: List<String>,
    searchQuery: String,
    selectedId: String,
    onSearchQueryChanged: (query: String) -> Unit,
    titlesData: List<ProfileTitlesData>,
    clarificationData: List<ProfileTitlesData> = emptyList(),
    showApplyButton: Boolean = false,
    showBackButton: Boolean = false,
    onShowSubTitles: (List<ProfileTitlesData>) -> Unit,
    onTitleSelected: (ProfileMeta?) -> Unit,
    onBack: () -> Unit,
    onApply: () -> Unit
) {

    val context = LocalContext.current

    val goldenColor = Golden
    val lightBlackColor = LightBlack
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val isScrollingUp = listState.isScrollingUp()
    val showSearchBar by remember {
        derivedStateOf { isScrollingUp }
    }


    BaseDialog(
        onDismiss = onDismiss,
        titles = headers,
        onBack = {
            onBack()
        },
        showBackButton = showBackButton
    ) { dialogModifier, dialogFocusManager, maxHeight ->

        Spacer(modifier = dialogModifier.height(15.dp))

        // Use BoxWithConstraints to get the maximum height available within the Card/Dialog
        BoxWithConstraints(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 25.dp)
                .heightIn(max = maxHeight)
        ) {
            // Determine the maximum height each view can take (50dp margin)

            val maxHeightForViews = this.maxHeight
            val maxHeightForSubTitles = maxHeightForViews - 35.dp - 179.dp - 70.dp


            Column(
                modifier = Modifier
                    .animateContentSize(animationSpec = tween(durationMillis = 3, delayMillis = 30))
                    .fillMaxWidth()
            ) {

                // First Scrollable
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .animateContentSize(animationSpec = tween(durationMillis = 3, delayMillis = 30))
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

                    itemsIndexed(titlesData,  key = { _, item -> item.id?:"" }) { index, title ->
                        val isFirst = index == 0
                        val isLast = index == titlesData.lastIndex
//                        AnimatedContent(title.isSelected) {
                        DescriptionItem(
                            isFirstItem = isFirst,
                            isLastItem = isLast,
                            title = title,
                            isSelected = title.id?.equals(selectedId) == true,
                            onClick = {
//                                title.isSelected = !title.isSelected
                                onTitleSelected(ProfileMeta(
                                    id = title.id,
                                    name = title.name,
                                    description = title.description,
                                ))
                                if (title.selections.isNullOrEmpty()) {
                                    coroutineScope.launch {
                                        listState.animateScrollToItem(0)
                                    }
                                } else {
                                    onShowSubTitles(title.selections ?: emptyList())
                                }

//                                     keyboardController?.hide()
                            },
                            modifier = Modifier
                        )
//                        }
                    }
                }

                if (clarificationData.isNotEmpty()) {
                    var isExpanded by remember { mutableStateOf(false) }
                    Spacer(modifier = dialogModifier.height(20.dp))
                    DashedLine(
                        modifier = dialogModifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp),
                    )

                    Spacer(modifier = dialogModifier.height(15.dp))

                    Row(
                        modifier = dialogModifier
                            .fillMaxWidth()
                            .padding(horizontal = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = dialogModifier
                        ) {
                            if (!isExpanded) {
                                Text(
                                    text = context.getString(R.string.strik_right_space),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = fontFamilyLato,
                                    color = goldenColor
                                )
                                Spacer(modifier = Modifier.width(0.dp))
                            }
                            Text(
                                text = context.getString(R.string.clarifications),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = fontFamilyLato,
                                color = lightBlackColor,
                                modifier = dialogModifier
                            )
                        }
                        Text(
                            text = if (isExpanded) context.getString(R.string.hide) else context.getString(
                                R.string.show
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = goldenColor,
                            modifier = dialogModifier.clickable {
                                dialogFocusManager.clearFocus()
                                isExpanded = !isExpanded
                            }
                        )
                    }

                    // Second scrollable
                    if (clarificationData.isNotEmpty() && isExpanded) {
                        Spacer(Modifier.height(15.dp))
                        LazyColumn(
                            modifier = Modifier
                                .animateContentSize(animationSpec = tween(durationMillis = 3, delayMillis = 30))
                                .heightIn(
                                    min = 0.dp,
                                    max = maxHeightForSubTitles
                                )// Distributes remaining space equally with View 1
                        ) {
                            itemsIndexed(clarificationData) { index, title ->
                                // Scrollable only, no onClick
                                val isLast = index == clarificationData.lastIndex
                                ClassificationItem(
                                    isLastItem = isLast,
                                    title = title.name?: "",
                                    description = title.description?: "",
                                    modifier = Modifier
                                )
                            }
                        }
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
                            .clickable {
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
                    .clickable {
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
                    .clickable {
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
fun DescriptionItem(
    isFirstItem: Boolean,
    isLastItem: Boolean,
    title: ProfileTitlesData,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Log.e("is last item", "$title, islastitem: $isLastItem")
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(bottom = if (isLastItem) 0.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = title.name ?: "",
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected && title.selections.isNullOrEmpty()) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected && title.selections.isNullOrEmpty()) Golden else LightBlack,
            //modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
            //modifier = Modifier.weight(1f)
        )
        if (!title.selections.isNullOrEmpty()) {
            Spacer(modifier = modifier.width(3.dp))
            Image(
                painter = painterResource(id = R.drawable.arrowdown_lite),
                contentDescription = null,
                modifier = modifier.size(11.dp)
            )
        }
        if (!title.description.isNullOrEmpty()) {
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
    }
}

@Composable
private fun ClassificationItem(
    isLastItem: Boolean,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row( modifier = modifier

    ) {
        Text(text = "•",
            modifier = modifier.padding(end = 4.dp),
            color = LightBlack,
            fontSize = 18.sp,
            fontFamily = fontFamilyLato,
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Golden, fontWeight = FontWeight.SemiBold)) {
                    append(title)
                }
                append(" :  ")
                withStyle(style = SpanStyle(color = LightBlack)) {
                    append(description)
                }
            },
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            modifier = modifier.padding(bottom = if (isLastItem) 0.dp else 6.dp),
            lineHeight = 22.sp,
        )
    }
}
