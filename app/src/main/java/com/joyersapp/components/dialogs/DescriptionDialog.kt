package com.joyersapp.components.dialogs

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.AppBasicTextField2
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.feature.dashboard.Routes
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.UserProfileNavigationEvent
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.feature.profile.presentation.dialogs.DescriptionEvent
import com.joyersapp.feature.profile.presentation.dialogs.DescriptionNavEvent
import com.joyersapp.feature.profile.presentation.dialogs.DescriptionViewModel
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack80
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isScrollingUp
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.tapToDismissKeyboard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

//@Preview
@Composable
fun DescriptionDialog(
    initList: List<ProfileTitlesData>,
    selectedTitle: ProfileTitlesData?,
    selectedSubTitle: ProfileTitlesData?,
    viewModel: DescriptionViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onApply: (ProfileMeta?, ProfileMeta?) -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DescriptionEvent.InitData(initList, selectedTitle, selectedSubTitle))
        viewModel.navigationEvents.collect { event ->
            when(event) {
                is DescriptionNavEvent.OnApply -> {
                    onApply(
                        ProfileMeta(
                            id = event.Title?.id,
                            name = event.Title?.name,
                        ),
                        ProfileMeta(
                            id = event.SubTitle?.id,
                            name = event.SubTitle?.name,
                        )) }
            }
        }
    }


    if (!state.rootItems.isEmpty()) {
        EditDescriptionDialog(
            onDismiss = {
                viewModel.onEvent(DescriptionEvent.OnClearData)
                onDismiss()
            },
            onApply = { viewModel.onEvent(DescriptionEvent.OnApply) },
            showApplyButton = state.isApplyEnabled,
            showBackButton = state.showBackButton,
            headers = state.headers,
            searchQuery = state.searchQuery,
            selectedId = state.selectedId,
            onSearchQueryChanged = { query ->
                viewModel.onEvent(DescriptionEvent.OnSearchQueryChanged(query))
//            searchQuery = query
//            CoroutineScope(Dispatchers.Default).launch {
//                itemsList =
//                    itemsList2.filter { it.name?.contains(query, ignoreCase = true) ?: false }
//            }

            },
            titlesData = state.reorderedItems,
            clarificationData = state.clarificationItems,
//        onShowSubTitles = { list ->
////            currentList = list
////            isSubTitleMode = true
//        },
            onItemClicked = { title ->

                viewModel.onEvent(DescriptionEvent.OnItemClicked(title))

//            if (isSubTitleMode) {
//                selectedSubTitle = title
//                selectedId = title?.id?: ""
//            } else {
//                selectedTitle = title
//                selectedId = title?.id?: ""
//            }

            },
            onBack = {
                viewModel.onEvent(DescriptionEvent.OnBackButton)
//            currentList = titlesData
//            isSubTitleMode = false
//            selectedSubTitle = null
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditDescriptionDialog(
    onDismiss: () -> Unit,
    headers: List<String>,
    searchQuery: String,
    selectedId: String,
    onSearchQueryChanged: (query: String) -> Unit,
    titlesData: List<ProfileTitlesData>,
    clarificationData: List<ProfileTitlesData> = emptyList(),
    showApplyButton: Boolean = false,
    showBackButton: Boolean = false,
//    onShowSubTitles: (List<ProfileTitlesData>) -> Unit,
    onItemClicked: (ProfileTitlesData) -> Unit,
    onBack: () -> Unit,
    onApply: () -> Unit
) {

    val context = LocalContext.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val goldenColor = Golden
    val lightBlackColor = LightBlack
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    BaseCard(
        onDismiss = onDismiss,
        titles = headers,
        onBack = {
            onBack()
        },
        showBackButton = showBackButton
    ) { dialogModifier, dialogFocusManager, maxHeight ->

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
                            .fillMaxWidth()
                    ) {

                        val scrollBehavior = if (headers.size == 3) TopAppBarDefaults.pinnedScrollBehavior()
                        else TopAppBarDefaults.enterAlwaysScrollBehavior()
                        val isScrollable by remember {
                            derivedStateOf {
                                listState.canScrollForward || listState.canScrollBackward
                            }
                        }

                        Column (
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f, fill = false)
                                .nestedScroll(scrollBehavior.nestedScrollConnection),
                            content = {

                                Spacer(Modifier.height(15.dp))

                                TopAppBar(
                                    expandedHeight = 55.dp,
                                    modifier = Modifier.heightIn(min = 0.dp),
                                    colors = TopAppBarColors(
                                        containerColor = Color.Transparent,
                                        scrolledContainerColor = Color.Transparent,
                                        navigationIconContentColor = Color.Transparent,
                                        titleContentColor = Color.Transparent,
                                        actionIconContentColor = Color.Transparent
                                    ),
                                    title = {
                                        SearchBarRow(
                                            placeholder = "Search ${headers.lastOrNull()?:""}",
                                            searchQuery = searchQuery,
                                            showApplyButton = showApplyButton,
                                            onApply = { onApply() },
                                            onSearchQueryChanged = { onSearchQueryChanged(it) }
                                        )
                                    },
                                    windowInsets = WindowInsets(0, 0, 0, 0),
                                    scrollBehavior = if (isScrollable) scrollBehavior else null
                                )
                                // First Scrollable
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .weight(1f, fill = false)
                                        .fillMaxWidth()
                                ) {
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
                                                            top = if (isKeyBoardOpen) 90.dp else 35.dp,
                                                            bottom = if (isKeyBoardOpen) 0.dp else 69.dp
                                                        )
                                                )
                                            }
                                        }
                                    } else {
                                        itemsIndexed(
                                            titlesData,
                                            key = { _, item -> item.id ?: "" }) { index, item ->
                                            val isFirst = index == 0
                                            val isLast = index == titlesData.lastIndex
//                        AnimatedContent(title.isSelected) {
                                            DescriptionItem(
                                                isFirstItem = isFirst,
                                                isLastItem = isLast,
                                                title = item,
                                                isSelected = item.isSelected,
                                                onClick = {
                                                    coroutineScope.launch {
                                                        listState.animateScrollToItem(0)
                                                        if (isScrollable) {
                                                            scrollBehavior.state.heightOffset = 0f
                                                        }
                                                    }
                                                    onItemClicked(item)
                                                },
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            }
                        )

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
                                    modifier = dialogModifier.noRippleClickable {
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
                                            title = title.name ?: "",
                                            description = title.description ?: "",
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
    placeholder: String,
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
            .padding(top = 0.dp, bottom = 20.dp)
            .height(35.dp)
            .padding(end = 15.dp),
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
                AppBasicTextField2(
                    value = searchQuery,
                    onValueChange = { query ->
                        onSearchQueryChanged(query)
                    },
                    placeholder = placeholder,
                    modifier = dialogModifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 10.dp, bottom = 1.dp),
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
                            .noRippleClickable {
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
        if (showApplyButton && searchQuery.isEmpty()) {
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
            .noRippleClickable { onClick() }
            .padding(bottom = if (isLastItem) 0.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = title.name ?: "",
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden else LightBlack,
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
//        Text(text = "•",
//            modifier = modifier.padding(end = 4.dp),
//            color = LightBlack,
//            fontSize = 18.sp,
//            fontFamily = fontFamilyLato,
//        )
        Box(
            modifier = Modifier
                .padding(top = 8.dp, end = 10.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(LightBlack)
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


@Composable
private fun BaseCard(
    onDismiss: () -> Unit = {},
    titles: List<String> = arrayListOf("",),
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    dialogContent: @Composable (dialogModifier: Modifier, dialogFocusManager: FocusManager, maxHeight: Dp) -> Unit = { dialogModifier, dialogFocusManager, maxHeight -> }
) {

    val context = LocalContext.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val goldenColor = Golden
    val lightBlackColor = LightBlack

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(White)
//    ) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val dialogFocusManager = LocalFocusManager.current
        val dialogModifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    dialogFocusManager.clearFocus()
                }
            }

        val configuration = LocalWindowInfo.current.containerSize
        // Calculate maximum height: screen height - 100.dp (50.dp top + 50.dp bottom)
        val minHeight = 275.dp
        val maxHeight = remember(configuration) {
            configuration.height.dp - 100.dp
        }

        // Determine the height modifier dynamically
        val dialogHeightModifier = if (isKeyBoardOpen) {
            // When keyboard is visible, the parent Column will resize to full height
            Modifier
                .height(maxHeight)
                .padding(top = 50.dp)
        } else {
            // When keyboard is hidden, use a standard dialog height constraint
            Modifier
                .wrapContentHeight()
                .heightIn(min = minHeight, max = maxHeight)
                .padding(top = 50.dp, bottom = 50.dp)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .background(LightBlack80),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = dialogModifier

                    .windowInsetsPadding(WindowInsets.systemBars)
                    .then(dialogHeightModifier) // Apply dynamic height
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White) // Ensure background captures taps
                    .imePadding()
//                .dismissKeyboardOnScroll()
                    .tapToDismissKeyboard(), shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                // Header
                Row(
                    modifier = dialogModifier
                        .fillMaxWidth()
                        .padding(top = 18.dp, start = 18.dp, end = 23.04.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Back button (only visible in subtitle mode)
                    if (showBackButton) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                            contentDescription = null,
                            modifier = dialogModifier
                                .size(20.dp, 15.dp)
                                .noRippleClickable { onBack() }
                        )
                    } else {
                        Spacer(modifier = dialogModifier.size(20.dp, 15.dp))
                    }

                    // Title or Second Title
                    if (titles.size == 1) {
                        Text(
                            text = titles[0],
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
                            color = lightBlackColor,
                            lineHeight = 29.sp,
                            modifier = dialogModifier.padding(top = 0.dp)
                        )
                    } else {
                        FlowRow(
                            modifier = dialogModifier.padding(
                                top = 2.dp,
                                bottom = 2.dp,
                                start = 10.dp,
                                end = 10.dp
                            ),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            titles.forEachIndexed { index, item ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item,
                                        fontSize = 16.sp,
                                        lineHeight = if (index == 0) 19.sp else 22.sp,
                                        fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                                        fontFamily = fontFamilyLato,
                                        color = lightBlackColor,
                                        modifier = dialogModifier
                                    )
                                    if (index < titles.size - 1) {
                                        Spacer(modifier = dialogModifier.width(11.dp))
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_forward_black),
                                            contentDescription = null,
                                            modifier = dialogModifier.size(6.dp, 10.dp)
                                        )
                                        Spacer(modifier = dialogModifier.width(10.dp))
                                    }
                                }
                            }

                        }
                    }

                    // Close button
                    Image(
                        painter = painterResource(id = R.drawable.ic_cross_golden),
                        contentDescription = null,
                        modifier = dialogModifier
                            .width(15.51.dp)
                            .noRippleClickable { onDismiss() }
                    )
                }
                dialogContent(dialogModifier, dialogFocusManager, maxHeight)
            }

        }
//        }
    }
}