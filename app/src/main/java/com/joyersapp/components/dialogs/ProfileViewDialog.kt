package com.joyersapp.components.dialogs

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isScrollingUp
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileViewDialog(
    onDismiss: () -> Unit,
    headers: List<String>,
    searchQuery: String,
    onSearchQueryChanged: (query: String) -> Unit,
    titlesData: List<ProfileTitlesData>,
    clarificationData: List<ProfileTitlesData> = emptyList(),
    showApplyButton: Boolean = false,
    onShowSubTitles: (List<ProfileTitlesData>) -> Unit,
    onTitleSelected: (String) -> Unit,
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


    BaseDialog(
        onDismiss = onDismiss,
        titles = headers,
        onBack = {
            showBackButton = false
            onBack()
                 },
        showBackButton = showBackButton
        ) { dialogModifier, dialogFocusManager, maxHeight, listState ->

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

//                        val density = LocalDensity.current
//// Convert 70.dp to pixels once
//                        val thresholdPx = with(density) { 70.dp.toPx() }
//
//                        val isScrollableEnough by remember(thresholdPx) {
//                            derivedStateOf {
//                                val layoutInfo = listState.layoutInfo
//                                val visibleItems = layoutInfo.visibleItemsInfo
//
//                                if (visibleItems.isEmpty()) return@derivedStateOf false
//
//                                // Calculate if total content height is at least 70dp larger than the viewport
//                                val totalChildrenHeight = visibleItems.sumOf { it.size }
//                                val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
//
//                                // If we have more items than visible, or visible items exceed viewport by threshold
//                                val isContentLongEnough = layoutInfo.totalItemsCount > visibleItems.size ||
//                                        (totalChildrenHeight - viewportHeight) > thresholdPx
//
//                                isContentLongEnough
//                            }
//                        }

                        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                        val isScrollable by remember {
                            derivedStateOf {
                                listState.canScrollForward || listState.canScrollBackward
                            }
                        }

                        Column (
                            modifier = Modifier
//                                .wrapContentHeight()
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
                                                    text = stringResource(R.string.no_results_found),
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
                                            key = { _, item -> item.id ?: "" }) { index, title ->
                                            val isFirst = index == 0
                                            val isLast = index == titlesData.lastIndex
                                            TitleItem(
                                                isFirstItem = isFirst,
                                                isLastItem = isLast,
                                                title = title,
                                                isSelected = title.isSelected,
                                                onClick = {
                                                    if (title.selections.isNullOrEmpty()) {
                                                        coroutineScope.launch {
                                                            listState.animateScrollToItem(0)
                                                            if (isScrollable) {
                                                                scrollBehavior.state.heightOffset = 0f
                                                            }
                                                        }
                                                        onTitleSelected(title.id ?: "")
                                                    } else {
                                                        showBackButton = true
                                                        onShowSubTitles(
                                                            title.selections ?: emptyList()
                                                        )
                                                    }
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
                                            text = stringResource(R.string.strik_right_space),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = fontFamilyLato,
                                            color = goldenColor
                                        )
                                        Spacer(modifier = Modifier.width(0.dp))
                                    }
                                    Text(
                                        text = stringResource(R.string.clarifications),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = fontFamilyLato,
                                        color = lightBlackColor,
                                        modifier = dialogModifier
                                    )
                                }
                                Text(
                                    text = if (isExpanded) stringResource(R.string.hide) else stringResource(
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
                AppBasicTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        onSearchQueryChanged(query)
                    },
                    placeholder = stringResource(R.string.search_speciality),
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
                    text = stringResource(R.string.apply),
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
                    text = stringResource(R.string.search),
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
fun TitleItem(
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
                text = stringResource(R.string.strik_right_space),
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