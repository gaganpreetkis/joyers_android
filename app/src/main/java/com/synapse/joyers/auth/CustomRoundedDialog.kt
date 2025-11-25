package com.synapse.joyers.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.synapse.joyers.R
import com.synapse.joyers.apiData.response.Subtitle
import com.synapse.joyers.apiData.response.Title
import com.synapse.joyers.ui.theme.Black
import com.synapse.joyers.ui.theme.Golden60
import com.synapse.joyers.ui.theme.Gray40

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
                    .fillMaxHeight(0.9f)
                    .padding(horizontal = 0.dp, vertical = 50.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 17.dp, start = 18.dp, end = 23.dp),
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
                                color = colorResource(id = R.color.black)
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = context.getString(R.string.title),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.black)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.ic_forward_black),
                                    contentDescription = null,
                                    modifier = Modifier.size(6.dp, 10.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = (currentState as DialogState.Subtitles).parentTitle.title ?: "",
                                    fontSize = 16.sp,
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
                        // Search field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { query ->
                                searchQuery = query
                                showNoResults = false
                                if (query.isEmpty()) {
                                    showApply = selectedTitleId.isNotEmpty()
                                }
                            },
                            placeholder = { Text(context.getString(R.string.search_speciality), color = hintColor) },
                            modifier = Modifier.weight(1f),
                            leadingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = null,
                                    modifier = Modifier.size(17.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_cancel_grey),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(15.dp)
                                            .clickable {
                                                searchQuery = ""
                                                keyboardController?.hide()
                                                showApply = selectedTitleId.isNotEmpty()
                                            }
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            singleLine = true,
                            shape = RoundedCornerShape(0.dp)
                        )

                        // Search/Apply button
                        if (showApply && selectedTitleId.isNotEmpty()) {
                            Button(
                                onClick = {
                                    keyboardController?.hide()
                                    onItemSelected(selectedTitleId, selectedTitleName)
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .width(63.dp)
                                    .height(35.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Golden60
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = context.getString(R.string.apply),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            TextButton(
                                onClick = {
                                    keyboardController?.hide()
                                },
                                modifier = Modifier
                                    .width(63.dp)
                                    .height(35.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = colorResource(id = R.color.black)
                                ),
                                shape = RoundedCornerShape(0.dp)
                            ) {
                                Text(
                                    text = context.getString(R.string.search),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Content area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
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

                                if (filteredTitles.isEmpty() && searchQuery.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = context.getString(R.string.no_results_found),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
//                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        // Titles list
                                        LazyColumn {
                                            items(filteredTitles) { title ->
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

                                        // Classification section
                                        val classificationTitles = state.items.filter { !it.decriptionTitle.isNullOrEmpty() }
                                        if (classificationTitles.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(20.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(3.dp)
                                                    .padding(horizontal = 15.dp)
                                                    .background(
                                                        color = goldenColor,
                                                        shape = RoundedCornerShape(1.5.dp)
                                                    )
                                            )
                                            Spacer(modifier = Modifier.height(15.dp))
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 15.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = context.getString(R.string.strik_right_space),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = goldenColor
                                                    )
                                                    Spacer(modifier = Modifier.width(5.dp))
                                                    Text(
                                                        text = context.getString(R.string.clarifications),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = colorResource(id = R.color.black)
                                                    )
                                                }
                                                Text(
                                                    text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = goldenColor,
                                                    modifier = Modifier.clickable {
                                                        isExpanded = !isExpanded
                                                    }
                                                )
                                            }

                                            AnimatedVisibility(
                                                visible = isExpanded,
                                                enter = fadeIn(),
                                                exit = fadeOut()
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 15.dp, vertical = 15.dp)
                                                ) {
                                                    classificationTitles.forEach { title ->
                                                        ClassificationItem(
                                                            title = title.title ?: "",
                                                            description = title.decriptionTitle ?: ""
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
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
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = context.getString(R.string.no_results_found),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = colorResource(id = R.color.black)
                                        )
                                    }
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
//                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        LazyColumn {
                                            items(filteredSubtitles) { subtitle ->
                                                SubtitleItem(
                                                    subtitle = subtitle,
                                                    isSelected = selectedTitleId == subtitle._id,
                                                    onClick = {
                                                        if (selectedTitleId == subtitle._id) {
                                                            selectedTitleId = ""
                                                            selectedTitleName = ""
                                                            showApply = false
                                                        } else {
                                                            selectedTitleId = subtitle._id ?: ""
                                                            val parentTitle = state.parentTitle.title ?: ""
                                                            selectedTitleName = if (parentTitle.equals("Pet", ignoreCase = true)) {
                                                                "Pet ( ${subtitle.name ?: ""} )"
                                                            } else {
                                                                subtitle.name ?: ""
                                                            }
                                                            showApply = true
                                                        }
                                                        keyboardController?.hide()
                                                    }
                                                )
                                            }
                                        }

                                        // Classification section for subtitles
                                        val classificationSubtitles = state.items.filter { !it.description.isNullOrEmpty() }
                                        if (classificationSubtitles.isNotEmpty()) {
                                            Spacer(modifier = Modifier.height(20.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(3.dp)
                                                    .padding(horizontal = 15.dp)
                                                    .background(
                                                        color = goldenColor,
                                                        shape = RoundedCornerShape(1.5.dp)
                                                    )
                                            )
                                            Spacer(modifier = Modifier.height(15.dp))
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 15.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = context.getString(R.string.strik_right_space),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = goldenColor
                                                    )
                                                    Spacer(modifier = Modifier.width(5.dp))
                                                    Text(
                                                        text = context.getString(R.string.clarifications),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = colorResource(id = R.color.black)
                                                    )
                                                }
                                                Text(
                                                    text = if (isExpanded) context.getString(R.string.hide) else context.getString(R.string.show),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = goldenColor,
                                                    modifier = Modifier.clickable {
                                                        isExpanded = !isExpanded
                                                    }
                                                )
                                            }

                                            AnimatedVisibility(
                                                visible = isExpanded,
                                                enter = fadeIn(),
                                                exit = fadeOut()
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 15.dp, vertical = 15.dp)
                                                ) {
                                                    classificationSubtitles.forEach { subtitle ->
                                                        ClassificationItem(
                                                            title = subtitle.name ?: "",
                                                            description = subtitle.description ?: ""
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
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
            .padding(horizontal = 15.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title.title ?: "",
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden60 else Black,
            modifier = Modifier.weight(1f)
        )
        if (!title.subtitles.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_gray),
                contentDescription = null,
                modifier = Modifier.size(20.dp, 12.dp)
            )
        }
        if (!title.decriptionTitle.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
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
            .padding(horizontal = 15.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = subtitle.name ?: "",
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Golden60 else Black,
            modifier = Modifier.weight(1f)
        )
        if (!subtitle.description.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = context.getString(R.string.strik_right_space),
                fontSize = 16.sp,
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
        fontSize = 16.sp
    )
}
