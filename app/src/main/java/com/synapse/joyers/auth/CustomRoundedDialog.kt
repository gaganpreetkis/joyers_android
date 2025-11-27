package com.synapse.joyers.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.synapse.joyers.R
import com.synapse.joyers.apiData.response.Subtitle
import com.synapse.joyers.apiData.response.Title
import com.synapse.joyers.common_widgets.AppBasicTextField
import com.synapse.joyers.common_widgets.DashedLine
import com.synapse.joyers.ui.theme.Black
import com.synapse.joyers.ui.theme.Golden60
import com.synapse.joyers.ui.theme.Gray20
import com.synapse.joyers.ui.theme.Gray40
import com.synapse.joyers.utils.fontFamilyLato
import com.synapse.joyers.utils.rememberIsKeyboardOpen

sealed class DialogState {
    data class Titles(val items: MutableList<Title>) : DialogState()
    data class Subtitles(val parentTitle: Title, val items: MutableList<Subtitle>) : DialogState()
}

@Composable
fun CustomRoundedDialog(
    titles: MutableList<Title>,
    onDismiss: () -> Unit,
    onItemSelected: (titleId: String, titleName: String) -> Unit
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
    val lightBlackColor = Black
    val hintColor = Gray40
    val whiteColor = Color.White

    val configuration = LocalConfiguration.current
    // Calculate maximum height: screen height - 100.dp (50.dp top + 50.dp bottom)
    val maxHeight = remember(configuration) {
        configuration.screenHeightDp.dp - 100.dp
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = maxHeight)
                    .wrapContentHeight()
                    .padding(horizontal = 0.dp, vertical = 0.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp, start = 18.dp, end = 23.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back button (only visible in subtitle mode)
                        if (currentState is DialogState.Subtitles) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(20.dp, 15.dp)
                                    .clickable {
                                        currentState = DialogState.Titles(titles)
                                        selectedTitleId = ""
                                        selectedTitleName = ""
                                        showApply = false
                                    }
                            )
                        } else {
                            Spacer(modifier = Modifier.size(20.dp, 15.dp))
                        }

                        // Title or Second Title
                        if (currentState is DialogState.Titles) {
                            Text(
                                text = context.getString(R.string.title),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = colorResource(id = R.color.black)
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = context.getString(R.string.title),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamilyLato,
                                    color = colorResource(id = R.color.black)
                                )
                                Spacer(modifier = Modifier.width(11.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_forward_black),
                                    contentDescription = null,
                                    modifier = Modifier.size(6.dp, 10.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = (currentState as DialogState.Subtitles).parentTitle.title ?: "",
                                    fontSize = 16.sp,
                                    fontFamily = fontFamilyLato,
                                    color = colorResource(id = R.color.black)
                                )
                            }
                        }

                        // Close button
                        Image(
                            painter = painterResource(id = R.drawable.ic_cross_golden),
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .clickable { onDismiss() }
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Search bar and buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .padding(horizontal = 30.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Search field with icons
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(35.dp)
                                .clip(shape = RoundedCornerShape(35.dp))
                                .background(color = Gray20, shape = RoundedCornerShape(35.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Leading search icon - positioned to match Material3 TextField icon spacing
                                Image(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = null,
                                    modifier = Modifier
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
                                    modifier = Modifier
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
                                        painter = painterResource(id = R.drawable.ic_cancel_grey),
                                        contentDescription = null,
                                        modifier = Modifier
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
                                    Spacer(modifier = Modifier.width(41.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
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
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(35.dp)
                                    .padding(0.dp)
                                    .clip(RoundedCornerShape(35.dp))
                                    .background(color = Gray20, shape = RoundedCornerShape(35.dp))
                                    .border(width = 0.dp, color = Gray40, shape = RoundedCornerShape(35.dp))
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
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Content area - height depends on content, refreshes on search
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .heightIn(max = maxHeight - 100.dp) // Reserve space for header and search
                    ) {
                        when (val state = currentState) {
                            is DialogState.Titles -> {
                                val filteredTitles = if (searchQuery.isEmpty()) {
                                    state.items
                                } else {
                                    state.items.filter {
                                        it.title?.contains(searchQuery, ignoreCase = true) == true
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

                                if (reorderedTitles.isEmpty() && searchQuery.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(if (isKeyBoardOpen) maxHeight else 200.dp).imePadding(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = context.getString(R.string.no_results_found),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = fontFamilyLato,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }
                                } else {
                                    val classificationTitles = state.items.filter { !it.decriptionTitle.isNullOrEmpty() }
                                    val contentMaxHeight =
                                        maxHeight// Reserve space for header and search
                                    // Main content: Layout changes based on expanded state
                                    if (classificationTitles.isNotEmpty() && isExpanded) {
                                        // When expanded: Titles scroll independently, Clarifications scroll separately
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            // Titles list - LazyColumn scrolls independently
                                            val titlesListMaxHeight = (maxHeight - 100.dp) - 250.dp // Reserve space for clarifications
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = titlesListMaxHeight.coerceAtLeast(150.dp))
                                            ) {
                                                items(reorderedTitles) { title ->
                                                    TitleItem(
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
                                                        }
                                                    )
                                                }
                                            }

                                            // Clarifications section - scrollable separately
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = 250.dp) // Fixed max height for clarifications
                                            ) {
                                                Spacer(modifier = Modifier.height(20.dp))
                                                DashedLine(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(3.dp)
                                                        .padding(horizontal = 15.dp),
                                                    strokeWidth = 3f
                                                )
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 15.dp, vertical = 15.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = context.getString(R.string.strik_right_space),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Black,
                                                            fontFamily = fontFamilyLato,
                                                            color = goldenColor
                                                        )
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                        Text(
                                                            text = context.getString(R.string.clarifications),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = fontFamilyLato,
                                                            color = colorResource(id = R.color.black)
                                                        )
                                                    }
                                                    Text(
                                                        text = context.getString(R.string.hide),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = fontFamilyLato,
                                                        color = goldenColor,
                                                        modifier = Modifier.clickable {
                                                            isExpanded = !isExpanded
                                                        }
                                                    )
                                                }

                                                // Clarifications list - scrollable separately with fixed height
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(150.dp) // Fixed height for scrollable area
                                                        .verticalScroll(rememberScrollState())
                                                        .padding(horizontal = 15.dp)
                                                ) {
                                                    classificationTitles.forEach { title ->
                                                        ClassificationItem(
                                                            title = title.title ?: "",
                                                            description = title.decriptionTitle
                                                                ?: ""
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                    }
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            }
                                        }
                                    } else {
                                        // When clarifications are hidden or not available
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .heightIn(max = contentMaxHeight)
                                        ) {
                                            // Titles list - wraps content
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                                    .heightIn(max = contentMaxHeight - 200.dp)
                                            ) {
                                                items(reorderedTitles) { title ->
                                                    TitleItem(
                                                        title = title,
                                                        isSelected = selectedTitleId == title._id,
                                                        onClick = {
                                                            if (selectedTitleId == title._id) {
                                                                selectedTitleId = ""
                                                                selectedTitleName = ""
                                                                showApply = false
                                                            } else {
                                                                selectedTitleId = title._id ?: ""
                                                                selectedTitleName = title.title ?: ""
                                                                if (title.subtitles.isNullOrEmpty()) {
                                                                    showApply = true
                                                                } else {
                                                                    currentState = DialogState.Subtitles(title, title.subtitles)
                                                                    showApply = false
                                                                }
                                                            }
                                                            keyboardController?.hide()
                                                        }
                                                    )
                                                }
                                            }

                                            // Clarification header - always visible if classifications exist
                                            if (classificationTitles.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(20.dp))
                                                DashedLine(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(3.dp)
                                                        .padding(horizontal = 15.dp),
                                                    strokeWidth = 3f
                                                )
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 15.dp, vertical = 15.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = context.getString(R.string.strik_right_space),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Black,
                                                            fontFamily = fontFamilyLato,
                                                            color = goldenColor
                                                        )
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                        Text(
                                                            text = context.getString(R.string.clarifications),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = fontFamilyLato,
                                                            color = colorResource(id = R.color.black)
                                                        )
                                                    }
                                                    Text(
                                                        text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = fontFamilyLato,
                                                        color = goldenColor,
                                                        modifier = Modifier.clickable {
                                                            isExpanded = !isExpanded
                                                        }
                                                    )
                                                }

                                                // Clarifications list - only visible when expanded
                                                AnimatedVisibility(
                                                    visible = isExpanded,
                                                    enter = expandVertically(
                                                        animationSpec = tween(300),
                                                        expandFrom = Alignment.Top
                                                    ) + fadeIn(animationSpec = tween(300)),
                                                    exit = shrinkVertically(
                                                        animationSpec = tween(300),
                                                        shrinkTowards = Alignment.Top
                                                    ) + fadeOut(animationSpec = tween(300))
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 15.dp)
                                                    ) {
                                                        classificationTitles.forEach { title ->
                                                            ClassificationItem(
                                                                title = title.title ?: "",
                                                                description = title.decriptionTitle ?: ""
                                                            )
                                                            Spacer(modifier = Modifier.height(2.dp))
                                                        }
                                                        Spacer(modifier = Modifier.height(15.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            is DialogState.Subtitles -> {
                                val filteredSubtitles = if (searchQuery.isEmpty()) {
                                    state.items
                                } else {
                                    state.items.filter {
                                        it.name?.contains(searchQuery, ignoreCase = true) == true
                                    }
                                }

                                if (filteredSubtitles.isEmpty() && searchQuery.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(if (isKeyBoardOpen) maxHeight else 200.dp).imePadding(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = context.getString(R.string.no_results_found),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            fontFamily = fontFamilyLato,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }
                                } else {
                                    val classificationSubtitles = state.items.filter { !it.description.isNullOrEmpty() }

                                    // Main content: Layout changes based on expanded state
                                    if (classificationSubtitles.isNotEmpty() && isExpanded) {
                                        // When expanded: Subtitles scroll independently, Clarifications scroll separately
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            // Subtitles list - LazyColumn scrolls independently
                                            val subtitlesListMaxHeight = (maxHeight - 100.dp) - 250.dp // Reserve space for clarifications
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = subtitlesListMaxHeight.coerceAtLeast(150.dp))
                                            ) {
                                                items(filteredSubtitles) { subtitle ->
                                                    SubtitleItem(
                                                        subtitle = subtitle,
                                                        isSelected = selectedTitleId == subtitle._id,
                                                        onClick = {
                                                            onItemSelected(subtitle._id ?: "", subtitle.name ?: "")
                                                            keyboardController?.hide()
                                                        }
                                                    )
                                                }
                                            }

                                            // Clarifications section - scrollable separately
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = 250.dp) // Fixed max height for clarifications
                                            ) {
                                                Spacer(modifier = Modifier.height(20.dp))
                                                DashedLine(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(3.dp)
                                                        .padding(horizontal = 15.dp),
                                                    strokeWidth = 3f
                                                )
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 15.dp, vertical = 15.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = context.getString(R.string.strik_right_space),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Black,
                                                            fontFamily = fontFamilyLato,
                                                            color = goldenColor
                                                        )
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                        Text(
                                                            text = context.getString(R.string.clarifications),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = fontFamilyLato,
                                                            color = colorResource(id = R.color.black)
                                                        )
                                                    }
                                                    Text(
                                                        text = context.getString(R.string.hide),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = fontFamilyLato,
                                                        color = goldenColor,
                                                        modifier = Modifier.clickable {
                                                            isExpanded = !isExpanded
                                                        }
                                                    )
                                                }

                                                // Clarifications list - scrollable separately with fixed height
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(150.dp) // Fixed height for scrollable area
                                                        .verticalScroll(rememberScrollState())
                                                        .padding(horizontal = 15.dp)
                                                ) {
                                                    classificationSubtitles.forEach { subtitle ->
                                                        ClassificationItem(
                                                            title = subtitle.name ?: "",
                                                            description = subtitle.description ?: ""
                                                        )
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                    }
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            }
                                        }
                                    } else {
                                        // When clarifications are hidden or not available
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                        ) {
                                            // Subtitles list - wraps content
                                            LazyColumn(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                            ) {
                                                items(filteredSubtitles) { subtitle ->
                                                    SubtitleItem(
                                                        subtitle = subtitle,
                                                        isSelected = selectedTitleId == subtitle._id,
                                                        onClick = {
                                                            onItemSelected(subtitle._id ?: "", subtitle.name ?: "")
                                                            keyboardController?.hide()
                                                        }
                                                    )
                                                }
                                            }

                                            // Clarification header - always visible if classifications exist
                                            if (classificationSubtitles.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(20.dp))
                                                DashedLine(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(3.dp)
                                                        .padding(horizontal = 15.dp),
                                                    strokeWidth = 3f
                                                )
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 15.dp, vertical = 15.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(
                                                            text = context.getString(R.string.strik_right_space),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Black,
                                                            fontFamily = fontFamilyLato,
                                                            color = goldenColor
                                                        )
                                                        Spacer(modifier = Modifier.width(5.dp))
                                                        Text(
                                                            text = context.getString(R.string.clarifications),
                                                            fontSize = 16.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            fontFamily = fontFamilyLato,
                                                            color = colorResource(id = R.color.black)
                                                        )
                                                    }
                                                    Text(
                                                        text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = fontFamilyLato,
                                                        color = goldenColor,
                                                        modifier = Modifier.clickable {
                                                            isExpanded = !isExpanded
                                                        }
                                                    )
                                                }

                                                // Clarifications list - only visible when expanded
                                                AnimatedVisibility(
                                                    visible = isExpanded,
                                                    enter = expandVertically(
                                                        animationSpec = tween(300),
                                                        expandFrom = Alignment.Top
                                                    ) + fadeIn(animationSpec = tween(300)),
                                                    exit = shrinkVertically(
                                                        animationSpec = tween(300),
                                                        shrinkTowards = Alignment.Top
                                                    ) + fadeOut(animationSpec = tween(300))
                                                ) {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 15.dp)
                                                    ) {
                                                        classificationSubtitles.forEach { subtitle ->
                                                            ClassificationItem(
                                                                title = subtitle.name ?: "",
                                                                description = subtitle.description ?: ""
                                                            )
                                                            Spacer(modifier = Modifier.height(2.dp))
                                                        }
                                                        Spacer(modifier = Modifier.height(15.dp))
                                                    }
                                                }
                                            }
                                        }
                                    }
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
fun TitleItem(
    title: Title,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 15.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = title.title ?: "",
            fontSize = 14.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden60 else Black,
            //modifier = Modifier.weight(1f)
        )
        if (!title.subtitles.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.arrowdown_lite),
                contentDescription = null,
                modifier = Modifier.size(11.dp)
            )
        }
        if (!title.decriptionTitle.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = fontFamilyLato,
                color = Golden60
            )
        }
    }
}

@Composable
fun SubtitleItem(
    subtitle: Subtitle,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 15.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subtitle.name ?: "",
            fontSize = 14.sp,
            fontFamily = fontFamilyLato,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden60 else Black,
            modifier = Modifier.weight(1f)
        )
        if (!subtitle.description.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Black,
                color = Golden60
            )
        }
    }
}

@Composable
fun ClassificationItem(
    title: String,
    description: String
) {
    val context = LocalContext.current
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Golden60, fontWeight = FontWeight.SemiBold)) {
                append(title)
            }
            append(" :  ")
            withStyle(style = SpanStyle(color = Black)) {
                append(description)
            }
        },
        fontSize = 14.sp,
        fontFamily = fontFamilyLato,
    )
}
