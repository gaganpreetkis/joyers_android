package com.joyersapp.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.auth.presentation.identity.TitleEvent
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.presentation.UserProfileNavigationEvent
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.feature.profile.presentation.dialogs.MentionJoyersEvent
import com.joyersapp.feature.profile.presentation.dialogs.MentionJoyersNavEvent
import com.joyersapp.feature.profile.presentation.dialogs.MentionJoyersViewModel
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayBG5
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.tapToDismissKeyboard

@Preview
@Composable
fun composePreview() {

//    MentionJoyerRow(EditMagneticsUserListData(
//        first_name = "Har",
//        last_name = "Kirat Kirat Kirat Kidgd fgrgc frrgdf",
//        starCount = 3,
//        showLock = true
//    )) { }
    MentionJoyersDialog(
        initList = arrayListOf(),
        onDismiss = {},
        onApply = {},
        viewmodel = hiltViewModel()
        )
}

@Composable
fun MentionJoyersDialog(
    initList: List<EditMagneticsUserListData>,
    onDismiss: () -> Unit,
    onApply: (List<EditMagneticsUserListData>) -> Unit,
    viewmodel: MentionJoyersViewModel = hiltViewModel()
) {

    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    val searchQuery = state.searchQuery
    val userList = state.filteredUserList
    val selectedUserList = state.filteredSelectedUserList

    LaunchedEffect(initList) {
        viewmodel.onEvent(MentionJoyersEvent.InitUserList(initList))
    }

    LaunchedEffect(Unit) {
        viewmodel.navigationEvents.collect { event ->
            when(event) {
                is MentionJoyersNavEvent.OnApply -> { onApply(event.selectedUsers) }
            }
        }
    }


    BaseMentionJoyersDialog(
        onDismiss = { onDismiss() },
        titles = arrayListOf("Profile Header"),
        isApplyEnabled = state.isApplyEnabled,
        onApply = { viewmodel.onEvent(MentionJoyersEvent.OnApply)}
    ) { dialogModifier, dialogFocusManager, maxHeight ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 25.dp)
                .background(Color.White)
        ) {
            // ---------- HEADER SECTION ----------
            if (state.isClearMentionsMode) {
                ClearMentionsActions(
                    onClear = { viewmodel.onEvent(MentionJoyersEvent.OnSelectionsCleared) },
                    onCancel = { viewmodel.onEvent(MentionJoyersEvent.OnToggleClearMentionsMode(false)) },
                )
            } else {
                Row(
                    modifier = Modifier
                        .padding(bottom = 13.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row() {
                        Text(
                            text = if (state.selectedUsersCount.isNotEmpty()) "Mention Joyers : " else "Mention Joyers",
                            fontSize = 16.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack,
                        )

                        Text(
                            text = state.selectedUsersCount,
                            fontSize = 16.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
                            color = Golden,
                        )
                    }

                    if (state.selectedUsersCount.isNotEmpty()) {
                        Text(
                            text = "Clear List",
                            fontSize = 12.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = Golden,
                            modifier = Modifier.noRippleClickable() {
                                viewmodel.onEvent(MentionJoyersEvent.OnToggleClearMentionsMode(true))
                            }
                        )
                    }
                }
            }



            // Card with profile and header images
            Card(
                modifier = Modifier
                    .width(384.dp)
                    .border(
                        width = 1.dp, color = GrayLightBorder, shape = RoundedCornerShape(5.dp)
                    ),
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = GrayBG5)
            ) {
                Column() {
                    Spacer(modifier = Modifier.height(14.dp))
                    SearchBarRowForEditMaganetic(
                        searchQuery = searchQuery,
                        onSearchQueryChanged = { viewmodel.onEvent(MentionJoyersEvent.OnSearchQueryChanged(it)) },
                        isAddMentionsEnabled = state.isAddMentionsEnabled,
                        onAddMentionsClick = { viewmodel.onEvent(MentionJoyersEvent.OnAddMentionsClicked) }
                    )

                    if (!state.isAddMentionsMode) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 15.dp, end = 15.dp)
                                .width(354.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(
                                    color = Color.White, shape = RoundedCornerShape(5.dp)
                                )
//            .border(
//                width = 1.dp, color = GrayLightBorder, shape = RoundedCornerShape(5.dp)
//            ),
                        ) {
                            JoyersList(userList, onUserClick = { selectedUser ->
                                viewmodel.onEvent(MentionJoyersEvent.OnUserSelectionToggled(selectedUser))
                            })
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }


            }

            if (state.isAddMentionsMode) {
                Spacer(modifier = Modifier.height(10.dp))
                SelectedUsersColumn(
                    selectedUserList,
                    onUserClick = { selectedUser ->
                        viewmodel.onEvent(MentionJoyersEvent.OnUserSelectionToggled(selectedUser))
                    }
                )
            }
        }
    }
}

@Composable
fun SearchBarRowForEditMaganetic(
    dialogModifier: Modifier = Modifier,
    searchQuery: String,
    isAddMentionsEnabled: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onAddMentionsClick: () -> Unit
) {
    val lightBlackColor = LightBlack
    val hintColor = LightBlack.copy(alpha = 0.60f)
    val whiteColor = White

    Row(
        modifier = dialogModifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(start = 14.dp, end = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        /* ---------------- SEARCH BAR ---------------- */
        Row(
            modifier = dialogModifier
                .width(314.dp) // 🔥 KEY FIX
                .height(30.dp)
                .clip(RoundedCornerShape(50))
                .background(whiteColor)
                .border(
                    width = 1.dp,
                    color = GrayLightBorder,
                    shape = RoundedCornerShape(50)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppBasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = "Search Joyer",
                modifier = dialogModifier
                    .fillMaxHeight()
                    .padding(),
                textStyle = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                ),
                containerColor = Color.White,
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
                        .padding(start = 10.dp, end = 14.dp) // 10.dp to account for AppBasicTextField's 2.dp end padding + 8.dp spacing
                        .size(15.dp)
                        .clickable { onSearchQueryChanged("") }
                )
            } else {
                // Spacer to maintain consistent padding when icon is not visible
                Spacer(modifier = dialogModifier.width(39.dp)) // 10.dp + 15.dp icon + 16.dp = 41.dp total
            }
        }


        /* ---------------- PLUS BUTTON ---------------- */
        Box(
            modifier = dialogModifier
                .size(30.dp)
                .alpha(if (isAddMentionsEnabled) 1f else 0.7f)
                .noRippleClickable(enabled = isAddMentionsEnabled) { onAddMentionsClick() },
        ) {
            Image(
                painter = painterResource(R.drawable.ic_add_round),
                contentDescription = "Add Joyer",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


@Composable
fun MentionJoyersScreen(userlist: List<EditMagneticsUserListData>, onUserClick: (EditMagneticsUserListData) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .width(354.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(
                color = Color.White, shape = RoundedCornerShape(5.dp)
            )
//            .border(
//                width = 1.dp, color = GrayLightBorder, shape = RoundedCornerShape(5.dp)
//            ),
    ) {
        JoyersList(userlist, onUserClick = onUserClick)
    }
}

@Composable
fun JoyersList(getPreviewJoyerList: List<EditMagneticsUserListData>,
               onUserClick: (EditMagneticsUserListData) -> Unit) {
    LazyColumn {
        itemsIndexed(getPreviewJoyerList) { index, user ->
//            if (index < 5) {
                Spacer(Modifier.height(15.dp))
                MentionJoyerRow(showCancelButton = false, user, onUserClick = onUserClick)
                if (getPreviewJoyerList.size - 1 == index) {
                    Spacer(Modifier.height(25.dp))
                }
//            }
        }
    }
}

@Composable
fun SelectedUsersColumn(selectedUsers: List<EditMagneticsUserListData>,
               onUserClick: (EditMagneticsUserListData) -> Unit) {
    LazyColumn {
        itemsIndexed(selectedUsers) { index, user ->
            Spacer(Modifier.height(15.dp))
            MentionJoyerRow(showCancelButton = true, user, onUserClick = onUserClick)
            if (selectedUsers.size -1  == index) {
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}

@Composable
fun MentionJoyerRow(
    showCancelButton: Boolean = false,
    joyer: EditMagneticsUserListData,
    onUserClick: (EditMagneticsUserListData) -> Unit
) {
    Row(
        modifier = Modifier
            .width(if (showCancelButton) 379.dp else 354.dp)
            .height(37.dp)
            .noRippleClickable {
                onUserClick(joyer)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (!showCancelButton) {
            // Radio
            Box(
                Modifier
                    .padding(horizontal = 10.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (joyer.isSelected) Golden else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (joyer.isSelected) {
                    Image(
                        painterResource(R.drawable.ic_tick),
                        contentDescription = "Radio Button",
                        Modifier.size(16.3.dp, 16.2.dp),
                        colorFilter = ColorFilter.tint(White)
                    )
                } else {
                    Image(
                        painterResource(R.drawable.ic_radio_button_unselected),
                        contentDescription = "Radio Button",
                        Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(LightBlack)
                    )
                }
            }
        }

//        Spacer(modifier = Modifier.width(10.dp))
        // Avatar
        Box(
            modifier = Modifier
                .border(width = 1.dp, color = AvatarBorder, shape = CircleShape)
                .padding(1.dp)
                .border(width = 1.dp, color = White, shape = CircleShape)
                .size(37.dp)
                .clip(CircleShape)
                .background(Gray20),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar), // your J icon
                contentDescription = "avatar", modifier = Modifier.size(37.dp)
            )
        }
//        AsyncImage(
//            model = "${NetworkConfig.IMAGE_BASE_URL}${joyer.profile_picture}",
//            contentDescription = "",
//            modifier = Modifier
//                .size(37.dp)
//                .clip(CircleShape)
//                .border(2.dp, Color.White, CircleShape)
//        )

        Spacer(modifier = Modifier.width(10.dp))

        // Texts
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                ) {
                Text(
                    text = joyer.getDisplayName(),
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = LightBlack,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, false)
                )

//                if (joyer.starCount > 0) {
                    Spacer(modifier = Modifier.width(2.dp))
                    repeat(1) {
                        Spacer(modifier = Modifier.width(3.dp))
                        Image(
                            painter = painterResource(R.drawable.ic_star_golden),
                            contentDescription = "Star",
                            modifier = Modifier
                                .size(14.dp, 13.dp)
                        )
                    }
//                }

//                if (joyer.showLock && joyer.starCount > 0) {
                    Spacer(modifier = Modifier.width(7.dp))
                    Box(modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(LightBlack55))
                    Spacer(modifier = Modifier.width(7.dp))
//                }

//                if (joyer.showLock) {
                    Image(
                        painter = painterResource(R.drawable.ic_lock_heart_black),
                        contentDescription = "Star",
                        modifier = Modifier
                            .size(9.5.dp, 14.19.dp)
                    )
//                }
            }

            Row(verticalAlignment = Alignment.CenterVertically,) {
                Text(
                    text = "Data Entry & Information Process Data Entry & Information",
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = Golden,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, false)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(modifier = Modifier
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(LightBlack55))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Following",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = LightBlack60,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )


            }

        }

        if (showCancelButton) {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_cross), // your J icon
                contentDescription = "cross",
            )
        } else {
            Spacer(modifier = Modifier.width(15.dp))
        }
    }
}

@Composable
fun ClearMentionsActions(
    onClear: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        // 🗑 Clear All
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_lock_heart_black), // replace with your icon
                contentDescription = null,
                modifier = Modifier.size(14.2.dp, 15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Clear All",
                fontSize = 12.sp,
                lineHeight = 24.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Bold,
                color = LightBlack
            )
        }

        Spacer(modifier = Modifier.width(17.dp))

        // Clear Button
        ActionChip(
            label = "Clear",
            onClick = onClear
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Cancel Button
        ActionChip(
            label = "Cancel",
            onClick = onCancel
        )
    }
}

@Composable
fun ActionChip(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(23.dp)
            .width(60.dp)
            .clip(RoundedCornerShape(50))
            .border(
                width = 1.dp,
                color = Golden,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Golden,
            fontFamily = fontFamilyLato,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun BaseMentionJoyersDialog(
    onDismiss: () -> Unit,
    titles: List<String>,
    isApplyEnabled: Boolean,
    onApply: () -> Unit,
    dialogContent: @Composable (dialogModifier: Modifier, dialogFocusManager: FocusManager, maxHeight: Dp) -> Unit = { dialogModifier, dialogFocusManager, maxHeight -> }
) {

    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val lightBlackColor = LightBlack

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

        Card(
            modifier = dialogModifier
                .windowInsetsPadding(WindowInsets.systemBars)
                .then(dialogHeightModifier) // Apply dynamic height
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White) // Ensure background captures taps
                .imePadding()
                .tapToDismissKeyboard(), shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            // Header
            Row(
                modifier = dialogModifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 18.dp, end = 19.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Back button (only visible in subtitle mode)
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                        contentDescription = null,
                        modifier = dialogModifier
                            .size(20.dp, 15.dp)
                            .noRippleClickable { onDismiss() }
                    )


                // Title or Second Title
                if (titles.size == 1) {
                    Text(
                        text = titles[0],
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = lightBlackColor,
                        modifier = dialogModifier.padding(top = 2.dp)
                    )
                } else {
                    FlowRow(
                        modifier = dialogModifier.padding(top = 4.dp, bottom = 2.dp, start = 10.dp, end = 10.dp),
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

                // Apply button
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = if (isApplyEnabled) Golden else Golden60,
                    modifier = dialogModifier
                        .noRippleClickable(enabled = isApplyEnabled) {
                            onApply()
                        }
                )
            }
            dialogContent(dialogModifier, dialogFocusManager, maxHeight)
        }
//        }
    }
}