package com.joyersapp.common_widgets

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.auth.data.remote.dto.identity.SubTitle
import com.joyersapp.auth.data.remote.dto.identity.Title
import com.joyersapp.auth.presentation.identity.IdentityViewModel
import com.joyersapp.auth.presentation.identity.TitlesDialogEvent
import com.joyersapp.auth.presentation.identity.TitleEvent
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.Gray80
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen


sealed class DialogState {
    data class Titles(val items: List<Title>) : DialogState()
    data class Subtitles(val parentTitle: String?, val items: List<SubTitle>) : DialogState()
}
//@Preview
@Composable
fun DualViewDialog(
    selectedTitle: Title? = null,
    viewmodel: IdentityViewModel,
    dialogState: DialogState,
    onDismiss: () -> Unit = {},
       onItemSelected: (
           titleId: String?,
           titleName: String?,
           subTitleId: String?,
           subTitleName: String?
               ) -> Unit = { a, b, c, d -> },
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val state by viewmodel.uiState.collectAsState()
    viewmodel.onEvent(TitleEvent.SearchQueryChanged(state.searchQuery))

    LaunchedEffect(Unit) {
        viewmodel.onEvent(TitleEvent.InitTitleSelection(selectedTitle))

        when(dialogState) {
            is DialogState.Subtitles -> viewmodel.onEvent(TitleEvent.ShowSubtitles(dialogState.items))
            is DialogState.Titles -> viewmodel.onEvent(TitleEvent.ShowTitles(dialogState.items))
        }

        viewmodel.events.collect { event ->
            when (event) {
                is TitlesDialogEvent.SelectionConfirmed -> {
                    onItemSelected(
                        event.titleId,
                        event.titleName,
                        event.subTitleId,
                        event.subTitleName
                    )
                    onDismiss()
                }
            }
        }
    }


    val goldenColor = Golden60
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val whiteColor = Color.White

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )) {
        val dialogFocusManager = LocalFocusManager.current
        val dialogModifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    dialogFocusManager.clearFocus()
                }
            }

        val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
        // Detect keyboard visibility using WindowInsets
        val isKeyboardVisible by remember {
            derivedStateOf { imeHeight > 0 }
        }
        val configuration = LocalWindowInfo.current.containerSize
        // Calculate maximum height: screen height - 100.dp (50.dp top + 50.dp bottom)
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
                .heightIn(max = maxHeight)
                .wrapContentHeight()
                .padding(top = 50.dp, bottom = 50.dp)
        }

        Card(
            modifier = dialogModifier

                .windowInsetsPadding(WindowInsets.systemBars)
                .then(dialogHeightModifier) // Apply dynamic height
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White) // Ensure background captures taps
                .dismissKeyboardOnScroll()
                .tapToDismissKeyboard()
            ,shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            // Header
            Row(
                modifier = dialogModifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, start = 18.dp, end = 23.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Back button (only visible in subtitle mode)
                if (state.dialogState is DialogState.Subtitles) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                        contentDescription = null,
                        modifier = dialogModifier
                            .size(20.dp, 15.dp)
                            .noRippleClickable {
                                viewmodel.onEvent(TitleEvent.NavigateToAllTitles)
                            }
                    )
                } else {
                    Spacer(modifier = dialogModifier.size(20.dp, 15.dp))
                }

                // Title or Second Title
                if (state.dialogState is DialogState.Titles) {
                    Text(
                        text = context.getString(R.string.title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = lightBlackColor,
                        modifier = dialogModifier.padding(top = 0.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = dialogModifier.padding(top = 2.dp, bottom = 2.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.title),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = lightBlackColor,
                            modifier = dialogModifier
                        )
                        Spacer(modifier = dialogModifier.width(11.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_forward_black),
                            contentDescription = null,
                            modifier = dialogModifier.size(6.dp, 10.dp)
                        )
                        Spacer(modifier = dialogModifier.width(10.dp))
                        Text(
                            text = (state.dialogState as DialogState.Subtitles).parentTitle ?: "",
                            fontSize = 16.sp,
                            fontFamily = fontFamilyLato,
                            color = lightBlackColor,
                            modifier = dialogModifier
                        )
                    }
                }

                // Close button
                Image(
                    painter = painterResource(id = R.drawable.ic_cross_golden),
                    contentDescription = null,
                    modifier = dialogModifier
                        .size(15.51.dp)
                        .noRippleClickable { onDismiss() }
                )
            }

            Spacer(modifier = dialogModifier.height(15.dp))

            // Use BoxWithConstraints to get the maximum height available within the Card/Dialog
            BoxWithConstraints(modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 25.dp)
                .heightIn(max = maxHeight)) {
                // Determine the maximum height each view can take (e.g., half of available height)

                var maxHeightForViews = this.maxHeight

//                val titleModifer = if (state.isExpanded) {
//                    Modifier.weight(1f, true)
//                }
//                if (state.isExpanded) {z
//                    maxHeightForViews = (this.maxHeight / 2)
//                }
                val maxHeightForSubTitles = maxHeightForViews - 35.dp - 179.dp - 70.dp
                Column(modifier = Modifier
                    .animateContentSize( animationSpec = tween(durationMillis = 3, delayMillis = 10))
                    .fillMaxWidth()) {

                    // First Scrollable
                     LazyColumn(
                            modifier = Modifier.animateContentSize( animationSpec = tween(durationMillis = 3, delayMillis = 10))
                                .weight(1f, fill = false)
                                .fillMaxWidth()
                        ) {
                            item {
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
                                                value = state.searchQuery,
                                                onValueChange = { query ->
                                                    viewmodel.onEvent(TitleEvent.SearchQueryChanged(query))
                                                },
                                                placeholder = context.getString(R.string.search_speciality),
                                                modifier = dialogModifier
                                                    .weight(1f)
                                                    .fillMaxHeight(),
                                                textStyle = TextStyle(
                                                    platformStyle = PlatformTextStyle(includeFontPadding = false),
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
                                            if (state.searchQuery.isNotEmpty()) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_cancel_grey),
                                                    contentDescription = null,
                                                    modifier = dialogModifier
                                                        .padding(start = 10.dp, end = 16.dp) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                                                        .size(15.dp)
                                                        .clickable {
                                                            viewmodel.onEvent(
                                                                TitleEvent.SearchQueryChanged(
                                                                    ""
                                                                )
                                                            )
                                                        }
                                                )
                                            } else {
                                                // Spacer to maintain consistent padding when icon is not visible
                                                Spacer(modifier = dialogModifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
                                            }
                                        }
                                    }

                                    // Search/Apply button
                                    if (state.showApply) {
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
                                                    viewmodel.onEvent(TitleEvent.ConfirmSelection)
////                                                keyboardController?.hide()
//                                                    onItemSelected(selectedTitleId, selectedTitleName)
//                                                    onDismiss()
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
                                                modifier = Modifier
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
                                                    color = if (state.searchQuery.isEmpty()) Gray20 else whiteColor,
                                                    shape = RoundedCornerShape(35.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = if (state.searchQuery.isEmpty()) GrayLightBorder else goldenColor,
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
                                                color = if (state.searchQuery.isEmpty()) lightBlackColor else goldenColor,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = dialogModifier.height(20.dp))
                            }
                            val items =
                            when (state.dialogState) {
                                is DialogState.Subtitles -> state.reorderedSubtitles
                                is DialogState.Titles -> state.reorderedTitles
                            }
                            if (items.isEmpty()) {
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
                                            modifier = dialogModifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = if (isKeyBoardOpen) 90.dp else 30.dp,
                                                    bottom = if (isKeyBoardOpen) 0.dp else 74.dp
                                                )
                                        )
                                    }
                                }
                            } else {
                                when(state.dialogState) {
                                    is DialogState.Subtitles -> {
                                        itemsIndexed(state.reorderedSubtitles) { index, subtitle ->
                                            val isFirst = index == 0
                                            val isLast = index == state.reorderedSubtitles.lastIndex
                                            SubtitleItem(
                                                isFirstItem = isFirst,
                                                isLastItem = isLast,
                                                modifier = Modifier,
                                                subtitle = subtitle,
                                                isSelected = state.selectedSubTitleId == subtitle.id,
                                                onClick = {
                                                    viewmodel.onEvent(TitleEvent.SubtitleClicked(subtitle))
                                                }
                                            )
                                        }
                                    }
                                    is DialogState.Titles -> {
                                        itemsIndexed(state.reorderedTitles) { index, title ->
                                            val isFirst = index == 0
                                            val isLast = index == items.lastIndex
                                            TitleItem(
                                                isFirstItem = isFirst,
                                                isLastItem = isLast,
                                                title = title,
                                                isSelected = state.selectedTitleId == title.id,
                                                onClick = {
                                                    viewmodel.onEvent(TitleEvent.TitleClicked(title))
                                                    keyboardController?.hide()
                                                },
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        val clarificationItems =
                            when (state.dialogState) {
                                is DialogState.Subtitles -> state.classificationSubtitles
                                is DialogState.Titles -> state.classificationTitles
                            }
                        if (clarificationItems.isNotEmpty()) {
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
                                    if (!state.isExpanded) {
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
                                    text = if (state.isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamilyLato,
                                    color = goldenColor,
                                    modifier = dialogModifier.clickable {
                                        dialogFocusManager.clearFocus()
                                        viewmodel.onEvent(TitleEvent.ExpandClassifications(state.isExpanded))
                                    }
                                )
                            }

                            // Second scrollable
                            if (clarificationItems.isNotEmpty() && state.isExpanded) {
                                Spacer(Modifier.height(15.dp))
                                LazyColumn(
                                    modifier = Modifier
                                        .heightIn(
                                            min = 0.dp,
                                            max = maxHeightForSubTitles
                                        )// Distributes remaining space equally with View 1
                                ) {
                                    when (state.dialogState) {
                                        is DialogState.Subtitles -> {
                                            itemsIndexed(state.classificationSubtitles) { index, title ->
                                                // Scrollable only, no onClick
                                                val isLast = index == state.classificationSubtitles.lastIndex
                                                ClassificationItem(
                                                    isLastItem = isLast,
                                                    title = title.name ?: "",
                                                    description = title.description
                                                        ?: "",
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                        is DialogState.Titles -> {
                                            itemsIndexed(state.classificationTitles) { index, title ->
                                                val isLast = index == state.classificationTitles.lastIndex
                                                ClassificationItem(
                                                    isLastItem = isLast,
                                                    title = title.name ?: "",
                                                    description = title.description
                                                        ?: "",
                                                    modifier = Modifier
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                  /*  } else {

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = maxHeightForViews)
                        ) {
                            item{
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
                                                colorFilter = ColorFilter.tint(Gray80)
                                            )

                                            // AppBasicTextField - it has internal padding (15.dp start, 2.dp end)
                                            // We account for this in our layout
                                            AppBasicTextField(
                                                value = state.searchQuery,
                                                onValueChange = { query ->
                                                    viewmodel.onEvent(TitleEvent.SearchQueryChanged(query))
                                                },
                                                placeholder = context.getString(R.string.search_speciality),
                                                modifier = dialogModifier
                                                    .weight(1f)
                                                    .fillMaxHeight(),
                                                textStyle = TextStyle(
                                                    platformStyle = PlatformTextStyle(includeFontPadding = false),
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
                                            if (state.searchQuery.isNotEmpty()) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_cancel_grey),
                                                    contentDescription = null,
                                                    modifier = dialogModifier
                                                        .padding(start = 10.dp, end = 16.dp) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                                                        .size(15.dp)
                                                        .clickable {
                                                            viewmodel.onEvent(
                                                                TitleEvent.SearchQueryChanged(
                                                                    ""
                                                                )
                                                            )
                                                        }
                                                )
                                            } else {
                                                // Spacer to maintain consistent padding when icon is not visible
                                                Spacer(modifier = dialogModifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
                                            }
                                        }
                                    }

                                    // Search/Apply button
                                    if (state.showApply) {
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
                                                    viewmodel.onEvent(TitleEvent.ConfirmSelection)
////                                                keyboardController?.hide()
//                                                    onItemSelected(selectedTitleId, selectedTitleName)
//                                                    onDismiss()
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
                                                modifier = Modifier
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
                                                    color = if (state.searchQuery.isEmpty()) Gray20 else whiteColor,
                                                    shape = RoundedCornerShape(35.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = if (state.searchQuery.isEmpty()) GrayLightBorder else goldenColor,
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
                                                color = if (state.searchQuery.isEmpty()) lightBlackColor else goldenColor,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = dialogModifier.height(20.dp))
                            }
                            if (state.reorderedTitles.isEmpty() && state.searchQuery.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = dialogModifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            text = context.getString(R.string.no_results_found),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = fontFamilyLato,
                                            textAlign = TextAlign.Center,
                                            color = lightBlackColor,
                                            modifier = dialogModifier
                                                .fillMaxWidth()
                                                .padding(
                                                    top = if (isKeyBoardOpen) 90.dp else 30.dp,
                                                    bottom = if (isKeyBoardOpen) 0.dp else 74.dp
                                                )
                                        )
                                    }
                                }
                            } else {
                                itemsIndexed(state.reorderedTitles) { index, title ->
                                    val isFirst = index == 0
                                    val isLast = index == state.reorderedTitles.lastIndex
                                    TitleItem(
                                        isFirstItem = isFirst,
                                        isLastItem = isLast,
                                        title = title,
                                        isSelected = state.selectedTitleId == title.id,
                                        onClick = {
                                            viewmodel.onEvent(TitleEvent.TitleClicked(title))
                                            keyboardController?.hide()
                                        },
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                        if (state.classificationTitles.isNotEmpty()) {
                            Spacer(modifier = dialogModifier.height(20.dp))
                            DashedLine(
                                modifier = dialogModifier
                                    .fillMaxWidth()
                                    //.height(3.dp)
                                    .padding(horizontal = 0.dp),
                                //strokeWidth = 3f
                            )

                            Spacer(dialogModifier.height(15.dp))

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
                                    if (!state.isExpanded) {

                                        Text(
                                            text = context.getString(R.string.strik_right_space),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = fontFamilyLato,
                                            color = goldenColor
                                        )
                                        Spacer(modifier = Modifier.width(1.dp))
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
                                    text = if (state.isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamilyLato,
                                    color = goldenColor,
                                    modifier = dialogModifier.clickable {
                                        dialogFocusManager.clearFocus()
                                        viewmodel.onEvent(TitleEvent.ExpandClassifications(state.isExpanded))
                                    }
                                )
                            }

                            // View 2: Scrollable Only
                            // This view dynamically matches the height logic of View 1

                            if (state.classificationTitles.isNotEmpty() && state.isExpanded) {
                                Spacer(dialogModifier.height(15.dp))
                                LazyColumn(
                                    modifier = Modifier
                                        .heightIn(
                                            min = 0.dp,
                                            max = maxHeightForViews
                                        ) // Distributes remaining space equally with View 1
                                ) {
                                    itemsIndexed(state.classificationTitles) { index, title ->
                                        val isLast = index == state.classificationTitles.lastIndex
                                        ClassificationItem(
                                            isLastItem = isLast,
                                            title = title.name ?: "",
                                            description = title.description
                                                ?: "",
                                            modifier = Modifier
                                        )
                                        //Spacer(modifier = Modifier.height(2.dp))
                                    }
                                }
                            }
                        }
//                        } ********
                    }*/
                }
            }
        }
    }
}

fun Modifier.dismissKeyboardOnScroll(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Dismiss the keyboard on any scroll action
                focusManager.clearFocus()
                return Offset.Zero
            }
        }
    }

    this.nestedScroll(nestedScrollConnection)
}

// 2. Modifier to dismiss keyboard on tap (from previous answer)
fun Modifier.tapToDismissKeyboard(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    pointerInput(Unit) {
        detectTapGestures(onTap = {
            focusManager.clearFocus()
        })
    }
}


@Composable
fun TitleItem(
    isFirstItem: Boolean,
    isLastItem: Boolean,
    title: Title,
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
            fontWeight = if (isSelected && title.subTitles.isNullOrEmpty()) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected && title.subTitles.isNullOrEmpty()) Golden60 else LightBlack,
            //modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
            //modifier = Modifier.weight(1f)
        )
        if (!title.subTitles.isNullOrEmpty()) {
            Spacer( modifier = modifier.width(3.dp))
            Image(
                painter = painterResource(id = R.drawable.arrowdown_lite),
                contentDescription = null,
                modifier = modifier.size(11.dp)
            )
        }
        if (!title.description.isNullOrEmpty()) {
            Spacer( modifier = modifier.width(3.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = fontFamilyLato,
                color = Golden60,
                modifier = modifier
            )
        }
    }
}

@Composable
fun SubtitleItem(
    isFirstItem: Boolean,
    isLastItem: Boolean,
    subtitle: SubTitle,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(bottom = if (isLastItem) 0.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = subtitle.name ?: "",
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden60 else LightBlack,
            //modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
        )
        if (!subtitle.description.isNullOrEmpty()) {
            Spacer( modifier = modifier.width(3.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 20.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Black,
                color = Golden60,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ClassificationItem(
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
                withStyle(style = SpanStyle(color = Golden60, fontWeight = FontWeight.SemiBold)) {
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


//@Composable
/*
fun getTitles(): MutableList<Title> {

    var titles by remember { mutableStateOf<MutableList<Title>>(arrayListOf()) }

    val student = arrayListOf<Subtitle>(
        SubTitle(id = "12", name = "Associate's Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "13", name = "Bachelor's Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "14", name = "Diploma Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "15", name = "Doctoral Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "16", name = "Elementary School Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "17", name = "High School Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "18", name = "Kindergartener", description = "Refers to children (5 years).", subtitles = arrayListOf()),
        SubTitle(id = "19", name = "Master's Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "20", name = "Middle School Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "21", name = "Postgraduate Diploma Student", description = "", subtitles = arrayListOf()),
        SubTitle(id = "22", name = "Preschooler", description = "Refers to children (3-4 years).", subtitles = arrayListOf()),
        SubTitle(id = "23", name = "Other Student", description = "", subtitles = arrayListOf())
    )
    titles = arrayListOf(
        Title(_id = "1", title = "Baby Joyers", decriptionTitle = "Refers to infants and toddlers (0-2 years).", subtitles = arrayListOf()),
        Title(_id = "2", title = "Couple", decriptionTitle = "", subtitles = arrayListOf()),
        Title(_id = "3", title = "Family", decriptionTitle = "", subtitles = arrayListOf()),
        Title(_id = "4", title = "Friends", decriptionTitle = "Two or more Joyers who share their activities with other Joyers.", subtitles = arrayListOf()),
        Title(_id = "5", title = "Ghost", decriptionTitle = "Only the account owner can see the followers and following of a Ghost Joyer. This information is completely hidden from everyone else.", subtitles = arrayListOf()),
        Title(_id = "6", title = "Nickname", decriptionTitle = "", subtitles = arrayListOf()),
        Title(_id = "7", title = "Pet", decriptionTitle = "", subtitles = arrayListOf()),
        Title(_id = "8", title = "Royalty & Nobility", decriptionTitle = "", subtitles = arrayListOf()),
        Title(_id = "9", title = "Special Needs Joyer", decriptionTitle = "", subtitles = arrayListOf()),
        Title(_id = "10", title = "Student", decriptionTitle = "", subtitles = student),
        Title(_id = "11", title = "Typical Joyer", decriptionTitle = "Represents the regular Joyers.", subtitles = arrayListOf()),
    )
    return titles
}*/
