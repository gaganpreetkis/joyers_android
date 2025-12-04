package com.joyersapp.common_widgets

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.response.Subtitle
import com.joyersapp.response.Title
import com.joyersapp.theme.Black
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.Gray80
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.rememberKeyboardHider


sealed class DialogState {
    data class Titles(val items: MutableList<Title>) : DialogState()
    data class Subtitles(val parentTitle: Title, val items: MutableList<Subtitle>) : DialogState()
}
@Preview
@Composable
fun DualViewDialog(/*onDismissRequest: () -> Unit,*/
                   titles: MutableList<Title> =  getTitles(),
                   onDismiss: () -> Unit = {},
                   onItemSelected: (titleId: String, titleName: String) -> Unit = { a,b -> }
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()
    var searchQuery by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var currentState by remember { mutableStateOf<DialogState>(DialogState.Titles(titles)) }
    var selectedTitleId by remember { mutableStateOf("") }
    var selectedTitleName by remember { mutableStateOf("") }
    var showApply by remember { mutableStateOf(false) }
    var showNoResults by remember { mutableStateOf(false) }



    val goldenColor = Golden60
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val whiteColor = Color.White


    val hideKeyboard = rememberKeyboardHider()

    var reorderedTitles: List<Title> = arrayListOf()
    var filteredClassificationTitles : List<Title> = arrayListOf()
    var reorderedSubtitles: List<Subtitle> = arrayListOf()
    var filteredClassificationSubtitles : List<Subtitle> = arrayListOf()


    when (val state = currentState) {
        is DialogState.Titles -> {
            var filteredTitles: List<Title> = arrayListOf()

            if (searchQuery.isEmpty()) {
                filteredTitles = state.items
                filteredClassificationTitles = state.items.filter {
                    !it.decriptionTitle.isNullOrEmpty()
                }
            } else {
                filteredTitles = state.items.filter {
                    it.title?.contains(searchQuery, ignoreCase = true) == true
                }
                filteredClassificationTitles = state.items.filter {
                    !it.decriptionTitle.isNullOrEmpty() && it.title?.contains(
                        searchQuery,
                        ignoreCase = true
                    ) == true
                }
            }

            // Reorder list: move selected item (without subtitles) to top
            reorderedTitles = if (selectedTitleId.isNotEmpty()) {
                val selectedTitle = filteredTitles.find { it._id == selectedTitleId }
                if (selectedTitle != null && selectedTitle.subtitles.isNullOrEmpty()) {
                    // Move selected item to top, keep others in original order
                    listOf(selectedTitle) + filteredTitles.filter { it._id != selectedTitleId }
                } else {
                    filteredTitles
                }
            } else {
                filteredTitles
            }
        }

        is DialogState.Subtitles -> {
            var filteredSubtitles: List<Subtitle> = arrayListOf()

            if (searchQuery.isEmpty()) {
                filteredSubtitles = state.items
                filteredClassificationSubtitles = state.items.filter {
                    !it.description.isNullOrEmpty() }
            } else {
                filteredSubtitles = state.items.filter {
                    it.name?.contains(searchQuery, ignoreCase = true) == true
                }
                filteredClassificationSubtitles = state.items.filter {
                    !it.description.isNullOrEmpty() && it.name?.contains(searchQuery, ignoreCase = true) == true }
            }

            // Reorder list: move selected item (without subtitles) to top
            reorderedSubtitles = if (selectedTitleId.isNotEmpty()) {
                val selectedTitle = filteredSubtitles.find { it.uuid == selectedTitleId }
                if (selectedTitle != null && selectedTitle.subtitles.isNullOrEmpty()) {
                    // Move selected item to top, keep others in original order
                    listOf(selectedTitle) + filteredSubtitles.filter { it.uuid != selectedTitleId }
                } else {
                    filteredSubtitles
                }
            } else {
                filteredSubtitles
            }


        }
    }

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
            Modifier.height(maxHeight)
                .padding(top = 50.dp)
        } else {
            // When keyboard is hidden, use a standard dialog height constraint
            Modifier.heightIn(max = maxHeight)
                .wrapContentHeight()
                .padding(top = 50.dp, bottom = 50.dp)
        }

        Card(
            modifier = dialogModifier
//                .imePadding()
//                .windowInsetsPadding(WindowInsets.safeDrawing)
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
            // Use BoxWithConstraints to get the maximum height available within the Card/Dialog
            BoxWithConstraints(modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 25.dp)
                .heightIn(max = maxHeight)) {
                // Determine the maximum height each view can take (e.g., half of available height)

                var maxHeightForViews = this.maxHeight
                if (isExpanded) {
                    maxHeightForViews = (this.maxHeight / 2)
                }
//                var maxHeightForViews = this.maxHeight - 200.dp
//                if (isExpanded) {
//                    maxHeightForViews = (this.maxHeight / 2) - 100.dp
//                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header
                    Row(
                        modifier = dialogModifier
                            .fillMaxWidth()
                            .padding(top = 18.dp, start = 3.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
//                                verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back button (only visible in subtitle mode)
                        if (currentState is DialogState.Subtitles) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                                contentDescription = null,
                                modifier = dialogModifier
                                    .size(20.dp, 15.dp)
                                    .clickable {
                                        currentState = DialogState.Titles(titles)
                                        selectedTitleId = ""
                                        selectedTitleName = ""
                                        showApply = false
                                    }
                            )
                        } else {
                            Spacer(modifier = dialogModifier.size(20.dp, 15.dp))
                        }

                        // Title or Second Title
                        if (currentState is DialogState.Titles) {
                            Text(
                                text = context.getString(R.string.title),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = lightBlackColor,
                                modifier = dialogModifier.padding(top = 3.dp)
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = dialogModifier.padding(top = 3.dp)
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
                                    text = (currentState as DialogState.Subtitles).parentTitle.title ?: "",
                                    fontSize = 16.sp,
                                    fontFamily = fontFamilyLato,
                                    color = lightBlackColor,
                                    modifier = dialogModifier
                                )
                            }
                        }

                        // Close button
                        Image(
                            painter = painterResource(id = R.drawable.ic_cross_golden_new),
                            contentDescription = null,
                            modifier = dialogModifier
                                .size(15.51.dp)
                                .clip(CircleShape)
                                .clickable { onDismiss() }
                        )
                    }

                    Spacer(modifier = dialogModifier.height(15.dp))

                    if (currentState is DialogState.Subtitles) {

//                 SubTitles Screen
                        /*  if (reorderedSubtitles.isEmpty() && searchQuery.isNotEmpty()) {
                              Box(
                                  modifier = dialogModifier
                                      .fillMaxWidth(),
                                      //.height(if (isKeyBoardOpen) maxHeight else 200.dp).imePadding(),
                                  //contentAlignment = Alignment.Center
                              ) {
                                  Text(
                                      text = context.getString(R.string.no_results_found),
                                      fontSize = 24.sp,
                                      fontWeight = FontWeight.SemiBold,
                                      fontFamily = fontFamilyLato,
                                      textAlign = TextAlign.Center,
                                      color = colorResource(id = R.color.black),
                                      modifier = dialogModifier.fillMaxWidth().padding(top = if (isKeyBoardOpen) 90.dp else 30.dp, bottom = if (isKeyBoardOpen) 0.dp  else 74.dp)
                                  )
                              }
                          } else {*/
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = maxHeightForViews)
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
                                            .background(color = Gray20, shape = RoundedCornerShape(35.dp))
                                            .border(1.dp, color = GrayLightBorder, shape = RoundedCornerShape(35.dp))
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
                                                value = searchQuery,
                                                onValueChange = { query ->
                                                    searchQuery = query
                                                    showNoResults = false
                                                    if (query.isEmpty()) {
                                                        showApply = selectedTitleId.isNotEmpty()
                                                    }
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
                                            if (searchQuery.isNotEmpty()) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_cancel_grey_new),
                                                    contentDescription = null,
                                                    modifier = dialogModifier
                                                        .padding(start = 10.dp, end = 16.dp) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                                                        .size(15.dp)
                                                        .clickable {
                                                            searchQuery = ""
                                                            keyboardController?.hide()
                                                            showApply = selectedTitleId.isNotEmpty()
                                                        }
                                                )
                                            } else {
                                                // Spacer to maintain consistent padding when icon is not visible
                                                Spacer(modifier = dialogModifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
                                            }
                                        }
                                    }

                                    // Search/Apply button
                                    if (showApply && selectedTitleId.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .width(70.dp)
                                                .height(35.dp)
                                                .clip(RoundedCornerShape(35.dp))
                                                .background(color = goldenColor, shape = RoundedCornerShape(35.dp))
                                                .clickable {
//                                                keyboardController?.hide()
                                                    onItemSelected(selectedTitleId, selectedTitleName)
                                                    onDismiss()
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
                                                .background(color = if (searchQuery.isEmpty()) Gray20 else whiteColor, shape = RoundedCornerShape(35.dp))
                                                .border(
                                                    width = 1.dp,
                                                    color = if (searchQuery.isEmpty()) GrayLightBorder else goldenColor,
                                                    shape = RoundedCornerShape(35.dp))
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
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = dialogModifier.height(20.dp))
                            }
                            if (reorderedSubtitles.isEmpty() && searchQuery.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = dialogModifier
                                            .fillMaxWidth(),
                                        //.height(if (isKeyBoardOpen) maxHeight else 200.dp).imePadding(),
                                        //contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = context.getString(R.string.no_results_found),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = fontFamilyLato,
                                            textAlign = TextAlign.Center,
                                            color = lightBlackColor,
                                            modifier = dialogModifier.fillMaxWidth().padding(top = if (isKeyBoardOpen) 90.dp else 30.dp, bottom = if (isKeyBoardOpen) 0.dp  else 74.dp)
                                        )
                                    }
                                }
                            } else {
                                itemsIndexed(reorderedSubtitles) { index, subtitle ->
                                    val isFirst = index == 0
                                    val isLast = index == reorderedSubtitles.lastIndex
                                    SubtitleItem(
                                        isFirstItem = isFirst,
                                        isLastItem = isLast,
                                        modifier = Modifier,
                                        subtitle = subtitle,
                                        isSelected = selectedTitleId == subtitle.uuid,
                                        onClick = {
//                                                            onItemSelected(subtitle._id ?: "", subtitle.name ?: "")
                                            if (selectedTitleId == subtitle.uuid) {
                                                selectedTitleId = ""
                                                selectedTitleName = ""
                                                showApply = false
                                            } else {
                                                selectedTitleId = subtitle.uuid ?: ""
//                                                val parentTitle = state.parentTitle.title ?: ""
                                                selectedTitleName = subtitle.name ?: ""
                                                showApply = true
                                            }
                                            keyboardController?.hide()
                                        }
                                    )
                                }
                            }
                        }
                        if (filteredClassificationSubtitles.isNotEmpty()) {
                            Spacer(modifier = dialogModifier.height(20.dp))
                            DashedLine(
                                modifier = dialogModifier
                                    .fillMaxWidth()
                                    //.height(3.dp)
                                    .padding(horizontal = 0.dp),
                                //strokeWidth = 3f
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
                                    text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
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


                            // View 2: Scrollable Only
                            // This view dynamically matches the height logic of View 1

                            if (filteredClassificationSubtitles.isNotEmpty() && isExpanded) {
                                Spacer(Modifier.height(15.dp))
                                LazyColumn(
                                    modifier = Modifier
                                        .heightIn(
                                            min = 0.dp,
                                            max = maxHeightForViews
                                        )// Distributes remaining space equally with View 1
//                                .background(Color.Cyan)
                                ) {
                                    itemsIndexed(filteredClassificationSubtitles) { index, title ->
                                        // Scrollable only, no onClick
                                        /*Text(
                                        text = "Scrollable Only: $item",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    )*/
                                        val isLast = index == filteredClassificationSubtitles.lastIndex
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
//                        }
                    } else {

//                Titles screeen
                        /* if (reorderedTitles.isEmpty() && searchQuery.isNotEmpty()) {
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
                                     color = colorResource(id = R.color.black),
                                     modifier = dialogModifier.fillMaxWidth().padding(top = if (isKeyBoardOpen) 90.dp else 30.dp, bottom = if (isKeyBoardOpen) 0.dp  else 74.dp)
                                 )
                             }
                         } else {*/
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
                                            .background(color = Gray20, shape = RoundedCornerShape(35.dp))
                                            .border(1.dp, color = GrayLightBorder, shape = RoundedCornerShape(35.dp))
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
                                                value = searchQuery,
                                                onValueChange = { query ->
                                                    searchQuery = query
                                                    showNoResults = false
                                                    if (query.isEmpty()) {
                                                        showApply = selectedTitleId.isNotEmpty()
                                                    }
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
                                            if (searchQuery.isNotEmpty()) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_cancel_grey_new),
                                                    contentDescription = null,
                                                    modifier = dialogModifier
                                                        .padding(start = 10.dp, end = 16.dp) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                                                        .size(15.dp)
                                                        .clickable {
                                                            searchQuery = ""
                                                            keyboardController?.hide()
                                                            showApply = selectedTitleId.isNotEmpty()
                                                        }
                                                )
                                            } else {
                                                // Spacer to maintain consistent padding when icon is not visible
                                                Spacer(modifier = dialogModifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
                                            }
                                        }
                                    }

                                    // Search/Apply button
                                    if (showApply && selectedTitleId.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .width(70.dp)
                                                .height(35.dp)
                                                .clip(RoundedCornerShape(35.dp))
                                                .background(color = goldenColor, shape = RoundedCornerShape(35.dp))
                                                .clickable {
//                                                keyboardController?.hide()
                                                    onItemSelected(selectedTitleId, selectedTitleName)
                                                    onDismiss()
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
                                                .background(color = if (searchQuery.isEmpty()) Gray20 else whiteColor, shape = RoundedCornerShape(35.dp))
                                                .border(
                                                    width = 1.dp,
                                                    color = if (searchQuery.isEmpty()) GrayLightBorder else goldenColor,
                                                    shape = RoundedCornerShape(35.dp))
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
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = dialogModifier.height(20.dp))
                            }
                            if (reorderedTitles.isEmpty() && searchQuery.isNotEmpty()) {
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
                                            modifier = dialogModifier.fillMaxWidth().padding(
                                                top = if (isKeyBoardOpen) 90.dp else 30.dp,
                                                bottom = if (isKeyBoardOpen) 0.dp else 74.dp
                                            )
                                        )
                                    }
                                }
                            } else {
                                itemsIndexed(reorderedTitles) { index, title ->
                                    val isFirst = index == 0
                                    val isLast = index == reorderedTitles.lastIndex
                                    TitleItem(
                                        isFirstItem = isFirst,
                                        isLastItem = isLast,
                                        title = title,
                                        isSelected = selectedTitleId == title._id,
                                        onClick = {
                                            if (selectedTitleId == title._id) {
                                                selectedTitleId = ""
                                                selectedTitleName = ""
                                                showApply = false
                                            } else {
                                                selectedTitleId = title._id ?: ""
                                                selectedTitleName =
                                                    title.title ?: ""
                                                if (title.subtitles.isNullOrEmpty()) {
                                                    showApply = true
                                                } else {
                                                    currentState =
                                                        DialogState.Subtitles(
                                                            title,
                                                            title.subtitles
                                                        )
                                                    showApply = false
                                                }
                                            }
                                            keyboardController?.hide()
                                        },
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                        if (filteredClassificationTitles.isNotEmpty()) {
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
                                    if (!isExpanded) {

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
                                    text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
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

                            // View 2: Scrollable Only
                            // This view dynamically matches the height logic of View 1

                            if (filteredClassificationTitles.isNotEmpty() && isExpanded) {
                                Spacer(dialogModifier.height(15.dp))
                                LazyColumn(
                                    modifier = Modifier
                                        .heightIn(
                                            min = 0.dp,
                                            max = maxHeightForViews
                                        ) // Distributes remaining space equally with View 1
                                ) {
                                    itemsIndexed(filteredClassificationTitles) { index, title ->
                                        val isLast = index == filteredClassificationTitles.lastIndex
                                        ClassificationItem(
                                            isLastItem = isLast,
                                            title = title.title ?: "",
                                            description = title.decriptionTitle
                                                ?: "",
                                            modifier = Modifier
                                        )
                                        //Spacer(modifier = Modifier.height(2.dp))
                                    }
                                }
                            }
                        }
//                        } ********
                    }
                }
            }

            /*when (val state = currentState) {
                is DialogState.Titles -> {
                    if (searchQuery.isEmpty()) {
                        filteredTitles = state.items
                        filteredClassificationTitles = state.items.filter {
                            !it.decriptionTitle.isNullOrEmpty()
                        }
                    } else {
                        filteredTitles = state.items.filter {
                            it.title?.contains(searchQuery, ignoreCase = true) == true
                        }
                        filteredClassificationTitles = state.items.filter {
                            !it.decriptionTitle.isNullOrEmpty() && it.title?.contains(searchQuery, ignoreCase = true) == true
                        }
                    }

                    // Reorder list: move selected item (without subtitles) to top
                    val reorderedTitles = if (searchQuery.isEmpty() && selectedTitleId.isNotEmpty()) {
                        val selectedTitle = filteredTitles.find { it._id == selectedTitleId }
                        if (selectedTitle != null && selectedTitle.subtitles.isNullOrEmpty()) {
                            // Move selected item to top, keep others in original order
                            listOf(selectedTitle) + filteredTitles.filter { it._id != selectedTitleId }
                        } else {
                            filteredTitles
                        }
                    } else {
                        filteredTitles
                    }





                }

                is DialogState.Subtitles -> {

                    if (searchQuery.isEmpty()) {
                        filteredSubtitles = state.items
                        filteredClassificationSubtitles = state.items.filter {
                            !it.description.isNullOrEmpty() }
                    } else {
                        filteredSubtitles = state.items.filter {
                            it.name?.contains(searchQuery, ignoreCase = true) == true
                        }
                        filteredClassificationSubtitles = state.items.filter {
                            !it.description.isNullOrEmpty() && it.name?.contains(searchQuery, ignoreCase = true) == true }
                    }

                    // Reorder list: move selected item (without subtitles) to top
                    val reorderedSubitles = if (searchQuery.isEmpty() && selectedTitleId.isNotEmpty()) {
                        val selectedTitle = filteredSubtitles.find { it.uuid == selectedTitleId }
                        if (selectedTitle != null && selectedTitle.subtitles.isNullOrEmpty()) {
                            // Move selected item to top, keep others in original order
                            listOf(selectedTitle) + filteredSubtitles.filter { it.uuid != selectedTitleId }
                        } else {
                            filteredSubtitles
                        }
                    } else {
                        filteredSubtitles
                    }

                    // Use BoxWithConstraints to get the maximum height available within the Card/Dialog
                    BoxWithConstraints(modifier = Modifier.padding(horizontal = 15.dp)) {
                        // Determine the maximum height each view can take (e.g., half of available height)
                        val maxHeightForViews = this.maxHeight / 2

                        Column(modifier = Modifier.fillMaxWidth()) {

                            // Header
                            Row(
                                modifier = dialogModifier
                                    .fillMaxWidth()
                                    .padding(top = 18.dp, start = 3.dp, end = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Back button (only visible in subtitle mode)
                                if (currentState is DialogState.Subtitles) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                                        contentDescription = null,
                                        modifier = dialogModifier
                                            .size(20.dp, 15.dp)
                                            .clickable {
                                                currentState = DialogState.Titles(titles)
                                                selectedTitleId = ""
                                                selectedTitleName = ""
                                                showApply = false
                                            }
                                    )
                                } else {
                                    Spacer(modifier = dialogModifier.size(20.dp, 15.dp))
                                }

                                // Title or Second Title
                                if (currentState is DialogState.Titles) {
                                    Text(
                                        text = context.getString(R.string.title),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyLato,
                                        color = colorResource(id = R.color.black),
                                        modifier = dialogModifier.padding(top = 6.dp)
                                    )
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = dialogModifier.padding(top = 6.dp)
                                    ) {
                                        Text(
                                            text = context.getString(R.string.title),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = fontFamilyLato,
                                            color = colorResource(id = R.color.black),
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
                                            text = (currentState as DialogState.Subtitles).parentTitle.title ?: "",
                                            fontSize = 16.sp,
                                            fontFamily = fontFamilyLato,
                                            color = colorResource(id = R.color.black),
                                            modifier = dialogModifier
                                        )
                                    }
                                }

                                // Close button
                                Image(
                                    painter = painterResource(id = R.drawable.ic_cross_golden),
                                    contentDescription = null,
                                    modifier = dialogModifier
                                        .size(15.dp)
                                        .clip(CircleShape)
                                        .clickable { onDismiss() }
                                )
                            }

                            Spacer(modifier = dialogModifier.height(15.dp))

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
                                        .background(color = Gray20, shape = RoundedCornerShape(35.dp))
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
                                            colorFilter = ColorFilter.tint(Gray40)
                                        )

                                        // AppBasicTextField - it has internal padding (15.dp start, 2.dp end)
                                        // We account for this in our layout
                                        AppBasicTextField(
                                            value = searchQuery,
                                            onValueChange = { query ->
                                                searchQuery = query
                                                showNoResults = false
                                                if (query.isEmpty()) {
                                                    showApply = selectedTitleId.isNotEmpty()
                                                }
                                            },
                                            placeholder = context.getString(R.string.search_speciality),
                                            modifier = dialogModifier
                                                .weight(1f)
                                                .fillMaxHeight(),
                                            textStyle = TextStyle(
                                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                                fontFamily = fontFamilyLato,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp
                                            ),
                                            containerColor = Color.Transparent,
                                            contentColor = Black,
                                            placeholderColor = hintColor,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                            maxLength = 100
                                        )

                                        // Trailing cancel icon (conditional) - account for AppBasicTextField's 2.dp end padding
                                        if (searchQuery.isNotEmpty()) {
                                            Image(
                                                painter = painterResource(id = R.drawable.ic_cancel_grey_new),
                                                contentDescription = null,
                                                modifier = dialogModifier
                                                    .padding(start = 10.dp, end = 16.dp) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                                                    .size(15.dp)
                                                    .clickable {
                                                        searchQuery = ""
                                                        keyboardController?.hide()
                                                        showApply = selectedTitleId.isNotEmpty()
                                                    }
                                            )
                                        } else {
                                            // Spacer to maintain consistent padding when icon is not visible
                                            Spacer(modifier = dialogModifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
                                        }
                                    }
                                }

                                // Search/Apply button
                                if (showApply && selectedTitleId.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .width(70.dp)
                                            .height(35.dp)
                                            .clip(RoundedCornerShape(35.dp))
                                            .background(color = goldenColor, shape = RoundedCornerShape(35.dp))
                                            .clickable {
                                                keyboardController?.hide()
                                                onItemSelected(selectedTitleId, selectedTitleName)
                                                onDismiss()
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
                                            .background(color = if (searchQuery.isEmpty()) Gray20 else whiteColor, shape = RoundedCornerShape(35.dp))
                                            .border(width = if (searchQuery.isEmpty()) 0.dp else 1.dp, color = if (searchQuery.isEmpty()) Gray40 else goldenColor, shape = RoundedCornerShape(35.dp))
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
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = dialogModifier.height(20.dp))

                            if (reorderedSubitles.isEmpty() && searchQuery.isNotEmpty()) {
                                Box(
                                    modifier = dialogModifier
                                        .fillMaxWidth()
                                        .height(if (isKeyBoardOpen) maxHeight else 200.dp).imePadding(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = context.getString(R.string.no_results_found),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyLato,
                                        color = colorResource(id = R.color.black),
                                        modifier = dialogModifier
                                    )
                                }
                            } else {

                                // View 1: Clickable and Scrollable
                                // This view's height is limited to either its content height or maxHeightForViews
                                *//*LazyColumn(
                                    modifier = Modifier
                                        .heightIn(min = 0.dp, max = maxHeightForViews)
            //                            .weight(1f) // Distributes remaining space
                                        .background(Color.LightGray)
                                ) {
                                    items(listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grape", "Honeydew")) { item ->
                                        Text(
                                            text = "Clickable: $item",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { println("$item clicked!") } // Clickable items
                                                .padding(12.dp)
                                        )
                                    }
                                }*//*
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = maxHeightForViews)
                                ) {
                                    items(reorderedSubitles) { subtitle ->
                                        SubtitleItem(
                                            modifier = Modifier,
                                            subtitle = subtitle,
                                            isSelected = selectedTitleId == subtitle.uuid,
                                            onClick = {
//                                                            onItemSelected(subtitle._id ?: "", subtitle.name ?: "")
                                                if (selectedTitleId == subtitle.uuid) {
                                                    selectedTitleId = ""
                                                    selectedTitleName = ""
                                                    showApply = false
                                                } else {
                                                    selectedTitleId = subtitle.uuid ?: ""
                                                    val parentTitle = state.parentTitle.title ?: ""
                                                    selectedTitleName = subtitle.name ?: ""
                                                    showApply = true
                                                }
                                                keyboardController?.hide()
                                            }
                                        )
                                    }
                                }
                                Spacer(modifier = dialogModifier.height(20.dp))
                                DashedLine(
                                    modifier = dialogModifier
                                        .fillMaxWidth()
                                        //.height(3.dp)
                                        .padding(horizontal = 0.dp),
                                    //strokeWidth = 3f
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
                                            Spacer(modifier = Modifier.width(1.dp))
                                        }
                                        Text(
                                            text = context.getString(R.string.clarifications),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = fontFamilyLato,
                                            color = colorResource(id = R.color.black),
                                            modifier = dialogModifier
                                        )
                                    }
                                    Text(
                                        text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
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


                                // View 2: Scrollable Only
                                // This view dynamically matches the height logic of View 1

                                if (filteredClassificationTitles.isNotEmpty() && isExpanded) {
                                    Spacer(Modifier.height(15.dp))
                                    LazyColumn(
                                        modifier = Modifier
                                            .heightIn(
                                                min = 0.dp,
                                                max = maxHeightForViews
                                            )// Distributes remaining space equally with View 1
//                                .background(Color.Cyan)
                                            .padding(bottom = 25.dp)
                                    ) {
                                        items(filteredClassificationTitles) { title ->
                                            // Scrollable only, no onClick
                                            *//*Text(
                                            text = "Scrollable Only: $item",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp)
                                        )*//*
                                            ClassificationItem(
                                                title = title.title ?: "",
                                                description = title.decriptionTitle
                                                    ?: "",
                                                modifier = Modifier
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                        }
                                    }
                                }
                                Spacer(Modifier.height(25.dp))
                            }
                        }
                    }


                }
            }*/


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
            text = title.title ?: "",
            fontSize = 16.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden60 else LightBlack,
            modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
            //modifier = Modifier.weight(1f)
        )
        if (!title.subtitles.isNullOrEmpty()) {
            Spacer( modifier = modifier.width(3.dp))
            Image(
                painter = painterResource(id = R.drawable.arrowdown_lite),
                contentDescription = null,
                modifier = modifier.size(11.dp)
            )
        }
        if (!title.decriptionTitle.isNullOrEmpty()) {
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
    subtitle: Subtitle,
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
            modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
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


@Composable
fun getTitles(): MutableList<Title> {

    var titles by remember { mutableStateOf<MutableList<Title>>(arrayListOf()) }

    val student = arrayListOf<Subtitle>(
        Subtitle(uuid = "12", name = "Associate's Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "13", name = "Bachelor's Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "14", name = "Diploma Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "15", name = "Doctoral Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "16", name = "Elementary School Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "17", name = "High School Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "18", name = "Kindergartener", description = "Refers to children (5 years).", subtitles = arrayListOf()),
        Subtitle(uuid = "19", name = "Master's Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "20", name = "Middle School Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "21", name = "Postgraduate Diploma Student", description = "", subtitles = arrayListOf()),
        Subtitle(uuid = "22", name = "Preschooler", description = "Refers to children (3-4 years).", subtitles = arrayListOf()),
        Subtitle(uuid = "23", name = "Other Student", description = "", subtitles = arrayListOf())
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
}