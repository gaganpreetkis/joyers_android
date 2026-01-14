package com.joyersapp.components.dialogs

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.CustomTextField
import com.joyersapp.common_widgets.ImagePickerBottomSheet
import com.joyersapp.common_widgets.ImagePickerBottomSheetBack
import com.joyersapp.feature.profile.presentation.ProfileHeaderData
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.theme.DisabledTextColor
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayBG5
import com.joyersapp.theme.GrayInnerBorder
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack10
import com.joyersapp.theme.LightBlack13
import com.joyersapp.theme.LightBlack40
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.LightBlack9
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.UiText
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.graphemeCount
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.uriToFile

@Preview
@Composable
private fun preview() {
    EditProfileHeaderDialog(
        viewModel = hiltViewModel()
    )
}

@Composable
fun EditProfileHeaderDialog(
    onDismiss: () -> Unit = {},
    onApply: (data: ProfileHeaderData) -> Unit = {},
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val profileHeaderData = state.profileHeaderData
    val isKeyboardVisible = rememberIsKeyboardOpen()
    var showProfilePlaceholder by remember { mutableStateOf(true) }
    var showImagePickerBottomSheet by remember { mutableStateOf(false) }
    var showImagePickerBottomSheetBack by remember { mutableStateOf(false) }
    var showHeaderPicker by remember { mutableStateOf(true) }
    var showProfilePicturePreview by remember { mutableStateOf(false) }
    var showCropDialog by remember { mutableStateOf(false) }
    var selectedProfileImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var selectedProfileImagePath by remember { mutableStateOf<String?>(null) }

    // Background image preview and crop states
    var showBackgroundImagePreview by remember { mutableStateOf(false) }
    var showBackgroundCropDialog by remember { mutableStateOf(false) }
    var selectedBackgroundImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var selectedBackgroundImagePath by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onEvent(UserProfileEvent.UpdateProfileHeaderData(state.magneticsData.profileHeaderData))
    }

    BaseDialog(
        onDismiss = { onDismiss() },
        titles = arrayListOf("Profile Header")

    ) { dialogModifier, dialogFocusManager, maxHeight ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 15.dp,
//                    bottom = if (isKeyboardVisible) 0.dp else 35.dp
                )
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
        ) {
            // ---------- HEADER SECTION ----------

            Text(
                text = "Profile Picture",
                fontSize = 16.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)
            )

            EditableProfilePictureCard(
                backgroundPicturePath = state.profileHeaderData.backgroundPicture ?: "",
                profilePicturePath = state.profileHeaderData.profilePicture ?: "",
                onHeaderPicker = {
                    showImagePickerBottomSheetBack = true
                },
                onClearHeaderImage = {
                    viewModel.onEvent(
                        UserProfileEvent.UpdateProfileHeaderData(
                            state.profileHeaderData.copy(
                                backgroundPicture = ""
                            )
                        )
                    )
                },
                onProfilePicturePicker = {
                    showImagePickerBottomSheet = true
                },
                onClearProfilePicture = {
                    viewModel.onEvent(
                        UserProfileEvent.UpdateProfileHeaderData(
                            state.profileHeaderData.copy(
                                profilePicture = ""
                            )
                        )
                    )
                }
            )

            // ---------- BIO SECTION ----------
            Text(
                text = "Bio",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = LightBlack,
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )

            BioEditor(
                context = context,
                selectedTab = state.profileHeaderData.selectedTab,
                bioValidationError = state.profileHeaderData.bioValidationError,
                overviewText = profileHeaderData.overviewFieldValue,
                highlightText = profileHeaderData.highlightFieldValue,
                websiteUrl = profileHeaderData.websiteUrl,
                remainingChars = if (state.profileHeaderData.selectedTab == "overview") {
                    profileHeaderData.overviewRemainingChars.toString()
                } else {
                    profileHeaderData.highlightsRemainingChars.toString()
                },
                onOverviewChange = {
                    viewModel.onEvent(UserProfileEvent.OnOverviewChanged(it))
                    if (it.text.endsWith(" @")) {
                        viewModel.onEvent(UserProfileEvent.ToggleMentionJoyersDialog(true))
                    }
                },
                onHighlightChange = {
                    viewModel.onEvent(UserProfileEvent.OnHighlightChanged(it))
                    if (it.text.endsWith(" @")) {
                        viewModel.onEvent(UserProfileEvent.ToggleMentionJoyersDialog(true))
                    }
                },
                onSelectedTabChange = {
                    viewModel.onEvent(UserProfileEvent.OnToggleBioEditor(it))
                },
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ---------- WEBSITE SECTION ----------

            WebsiteTextField(
                label = "Website",
                hintText = "Domain Link",
                value = profileHeaderData.websiteUrl,
                onValueChange = { viewModel.onEvent(UserProfileEvent.OnWebsiteUrlChanged(it)) },
                onClear = { viewModel.onEvent(UserProfileEvent.OnWebsiteUrlChanged("")) }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ---------- APPLY BUTTON ----------
            Button(
                enabled = state.profileHeaderData.bioValidationError == null,
                onClick = {
                    if (state.profileHeaderData.bioValidationError == null) {
                        onApply(state.profileHeaderData)
                    }
                },
                modifier = Modifier
                    .width(190.dp)
                    .align(Alignment.CenterHorizontally)
                    .height(47.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Golden,
                    disabledContainerColor = Golden,
                    contentColor = White,
                    disabledContentColor = DisabledTextColor
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    modifier = Modifier.offset(y = -1.dp),
                    text = "Apply",
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                )
            }

            Spacer(Modifier.height(if (isKeyboardVisible) 25.dp else 35.dp))
        }
    }

    // Image Picker Bottom Sheet Profile Picture
    ImagePickerBottomSheet(
        showBottomSheet = showImagePickerBottomSheet,
        onDismiss = { showImagePickerBottomSheet = false },
        allowMultipleSelection = false,
        onImagesPicked = { uris ->
            val profileImageUri = uris[0]
            if (profileImageUri.path!!.isNotEmpty()) {
                selectedProfileImageUri = profileImageUri
                val file = uriToFile(context, profileImageUri)
                selectedProfileImagePath = file.path.toString()
                showImagePickerBottomSheet = false
                showProfilePicturePreview = true
            }
        },
        onCameraImagePicked = { uri ->
            val profileImageUri = uri
            if (profileImageUri.path!!.isNotEmpty()) {
                selectedProfileImageUri = profileImageUri
                val file = uriToFile(context, profileImageUri)
                selectedProfileImagePath = file.path.toString()
                showImagePickerBottomSheet = false
                showProfilePicturePreview = true
            }
        }
    )

    // Profile Picture Preview Dialog
    ProfilePicturePreviewDialog(
        showDialog = showProfilePicturePreview,
        imageUri = selectedProfileImageUri,
        imagePath = selectedProfileImagePath,
        onDismiss = {
            showProfilePicturePreview = false
            selectedProfileImageUri = null
            selectedProfileImagePath = null
        },
        onChangePicture = {
            showImagePickerBottomSheet = true
        },
        onDelete = {
            selectedProfileImageUri = null
            selectedProfileImagePath = null
            showProfilePlaceholder = true
            showProfilePicturePreview = true
        },
        onCrop = {
            if (selectedProfileImageUri != null) {
                //showProfilePicturePreview = false // Close preview before opening crop
                showCropDialog = true
            }
        },
        onDone = {
            selectedProfileImagePath?.let { path ->
                showProfilePlaceholder = false
                viewModel.onEvent(UserProfileEvent.ProfilePicturePathChanged(path))
            }
            showProfilePicturePreview = false
            selectedProfileImageUri = null
            selectedProfileImagePath = null
        }
    )

    // Crop Dialog
    CropImageDialog(
        showDialog = showCropDialog,
        imageUri = selectedProfileImageUri,
        onDismiss = {
            showCropDialog = false
            // Reopen preview dialog if we had an image selected
            if (selectedProfileImageUri != null) {
                showProfilePicturePreview = true
            }
        },
        onCropped = { newUri, newPath ->
            showCropDialog = false
            // Update state with cropped image
            selectedProfileImageUri = newUri
            selectedProfileImagePath = newPath
            // Show preview dialog with updated cropped image
            showProfilePicturePreview = true
        }
    )

    // Image Picker Bottom Sheet Background Picture
    ImagePickerBottomSheetBack(
        showBottomSheet = showImagePickerBottomSheetBack,
        onDismiss = { showImagePickerBottomSheetBack = false },
        allowMultipleSelection = false,
        onImagesPicked = { uris ->
            val headerImageUri = uris[0]
            if (headerImageUri.path!!.isNotEmpty()) {
                selectedBackgroundImageUri = headerImageUri
                val file = uriToFile(context, headerImageUri)
                selectedBackgroundImagePath = file.path.toString()
                showImagePickerBottomSheetBack = false
                showBackgroundImagePreview = true
            }
        },
        onCameraImagePicked = { uri ->
            val headerImageUri = uri
            if (headerImageUri.path!!.isNotEmpty()) {
                selectedBackgroundImageUri = headerImageUri
                val file = uriToFile(context, headerImageUri)
                selectedBackgroundImagePath = file.path.toString()
                showImagePickerBottomSheetBack = false
                showBackgroundImagePreview = true
            }
        }
    )

    // Background Image Preview Dialog
    BackgroundImagePreviewDialog(
        showDialog = showBackgroundImagePreview,
        imageUri = selectedBackgroundImageUri,
        imagePath = selectedBackgroundImagePath,
        onDismiss = {
            showBackgroundImagePreview = false
            selectedBackgroundImageUri = null
            selectedBackgroundImagePath = null
        },
        onChangePicture = {
            showImagePickerBottomSheetBack = true
        },
        onDelete = {
            selectedBackgroundImageUri = null
            selectedBackgroundImagePath = null
            viewModel.onEvent(
                UserProfileEvent.UpdateProfileHeaderData(
                    state.profileHeaderData.copy(
                        backgroundPicture = ""
                    )
                )
            )
            showBackgroundImagePreview = false
        },
        onCrop = {
            if (selectedBackgroundImageUri != null) {
                //showBackgroundImagePreview = false // Close preview before opening crop
                showBackgroundCropDialog = true
            }
        },
        onDone = {
            selectedBackgroundImagePath?.let { path ->
                viewModel.onEvent(UserProfileEvent.BackgroundPicturePathChanged(path))
            }
            showBackgroundImagePreview = false
            selectedBackgroundImageUri = null
            selectedBackgroundImagePath = null
        }
    )

    // Background Image Crop Dialog
    CropBackgroundImageDialog(
        showDialog = showBackgroundCropDialog,
        imageUri = selectedBackgroundImageUri,
        onDismiss = {
            showBackgroundCropDialog = false
            // Reopen preview dialog if we had an image selected
            if (selectedBackgroundImageUri != null) {
                showBackgroundImagePreview = true
            }
        },
        onCropped = { newUri, newPath ->
            showBackgroundCropDialog = false
            // Update state with cropped image
            selectedBackgroundImageUri = newUri
            selectedBackgroundImagePath = newPath
            // Show preview dialog with updated cropped image
            showBackgroundImagePreview = true
        }
    )
}

@Composable
fun EditableProfilePictureCard(
    backgroundPicturePath: String,
    profilePicturePath: String,
    onHeaderPicker: () -> Unit,
    onClearHeaderImage: () -> Unit,
    onProfilePicturePicker: () -> Unit,
    onClearProfilePicture: () -> Unit

) {
    // Card with profile and header images
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .border(
                width = 1.dp,
                color = GrayOuterBorder,
                shape = RoundedCornerShape(5.dp)
            ),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = GrayBG5)
    ) {

        var showHeaderPicker by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            // Header/Background Image
            if (backgroundPicturePath.isNotEmpty()) {
                AsyncImage(
                    model = "https://joyers-api-dev.krishnais.com/uploads/$backgroundPicturePath",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Close button for header
                Image(
                    painter = painterResource(id = R.drawable.ic_cancel_round_golden),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(15.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            onClearHeaderImage()
                        }
                )
            } else {
                // Header picker button
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(vertical = 15.dp, horizontal = 14.97.dp)
                        .noRippleClickable {
                            onHeaderPicker()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(37.92.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, LightBlack13, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.camera_inside_color),
                            contentDescription = "Edit Background",
                            modifier = Modifier.width(20.82.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.header),
                        fontSize = 11.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        color = LightBlack60,
                        lineHeight = 20.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(189.dp)
                    .align(Alignment.Center)
            ) {

                // Profile Image (centered)
                Box(
                    modifier = Modifier
                        .size(189.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(
                            2.dp,
                            if (profilePicturePath.isNotEmpty()) Color.Transparent else LightBlack9,
                            CircleShape
                        )
                ) {
                    if (profilePicturePath.isNotEmpty()) {
                        AsyncImage(
                            model = "https://joyers-api-dev.krishnais.com/uploads/$profilePicturePath",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    onProfilePicturePicker()
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.camera_outline_colored),
                                contentDescription = null,
                                modifier = Modifier.size(71.dp, 55.dp)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = stringResource(R.string.upload_picture),
                                fontSize = 15.sp,
                                fontFamily = fontFamilyLato,
                                fontStyle = FontStyle.Normal,
                                color = LightBlack60,
                                lineHeight = 21.sp,
                            )
                        }
                    }
                }

                if (profilePicturePath.isNotEmpty()) {
                    // Close button for profile
                    Image(
                        painter = painterResource(id = R.drawable.ic_cross_round_border_golden),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                            .size(40.dp)
                            .align(Alignment.BottomEnd)
                            .clickable {
                                onClearProfilePicture()
                            }
                    )
                }

            }
        }
    }
}

@Composable
fun BioEditor(
    context: Context,
    selectedTab: String,
    bioValidationError: UiText?,
    overviewText: TextFieldValue,
    highlightText: TextFieldValue,
    websiteUrl: String,
    remainingChars: String,
    onOverviewChange: (TextFieldValue) -> Unit,
    onHighlightChange: (TextFieldValue) -> Unit,
    onSelectedTabChange: (String) -> Unit,
) {

    Column() {
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, if (bioValidationError == null) GrayOuterBorder else Red),
            colors = CardDefaults.cardColors(containerColor = GrayBG5),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {

                // ---------------------- TAB HEADER ----------------------
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    TabItem(
                        title = "Overview",
                        selected = selectedTab == "overview",
                        enabled = true
                    ) { onSelectedTabChange("overview") }
                    VerticalDivider(color = LightBlack10)
                    TabItem(
                        title = "Highlights",
                        selected = selectedTab == "highlights",
                        enabled = bioValidationError == null
                    ) { onSelectedTabChange("highlights") }
                }

                HorizontalDivider(color = LightBlack10)

                // ---------------------- TEXT EDITOR ----------------------
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Text(
                        text = remainingChars,
                        color = if (bioValidationError == null) LightBlack60 else Red,
                        fontSize = 12.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fontFamilyLato,
                        modifier = Modifier
                            .padding(top = 5.5.dp, bottom = 1.dp, end = 7.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.End
                    )

                    Box(Modifier.padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)) {
                        if (selectedTab == "overview") {
                            OverviewEditor(
                                text = overviewText,
                                onChange = {
                                    onOverviewChange(it)
                                }
                            )
                        } else {
                            HighlightsEditor(
                                websiteUrl = websiteUrl,
                                textState = highlightText,
                                onChange = {
                                    onHighlightChange(it)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (bioValidationError != null) {
            Text(
                text = bioValidationError.asString(context),
                color = Red,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}

@Composable
fun TabItem(title: String, selected: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .noRippleClickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.offset(y = -1.dp),
            text = title,
            color = if (selected) Golden else if (enabled) LightBlack else LightBlack40,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = fontFamilyLato
        )
    }
}

@Composable
fun OverviewEditor(
    text: TextFieldValue,
    onChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = {
//            if (it.text.graphemeCount() > 150) return@BasicTextField
            onChange(it)
        },
        visualTransformation = { textValue ->
            TransformedText(
                highlightWords(textValue.text),
                OffsetMapping.Identity
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            color = Color.Transparent // we paint using AnnotatedString
        ),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { inner ->
            Box(Modifier.fillMaxSize()) {

                // Editable transparent text overlay
                inner()
                // Placeholder
                if (text.text.isEmpty()) {
                    Text(
                        "About Joyer",
                        color = LightBlack40,
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = fontFamilyLato
                    )
                }
            }
        }
    )
}

fun highlightWords(text: String): AnnotatedString {
    val parts = text.split(" ")

    return buildAnnotatedString {
        parts.forEachIndexed { index, word ->

            val isMention = word.startsWith("@")
            val isHashtag = word.startsWith("#")
            val isUrl =
                word.startsWith("http") || word.startsWith("https") || word.startsWith("www")

            val color = if (isMention || isHashtag || isUrl) Golden else LightBlack
            val fontWeight =
                if (isMention || isHashtag || isUrl) FontWeight.SemiBold else FontWeight.Normal


            withStyle(
                style = SpanStyle(
                    color = color,
                    fontWeight = fontWeight
                )
            ) {
                append(word)
            }

            if (index != parts.lastIndex) append(" ")
        }
    }
}

/*@Composable
fun HighlightsEditor(
) {
    var bullets by remember { mutableStateOf("• ") }

    BasicTextField(
        value = bullets,
        onValueChange = { newValue ->
            bullets = newValue

            // Auto-add bullet when pressing Enter
            if (newValue.endsWith("\n")) {
                bullets += "• "
            }
        },
        textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        decorationBox = { inner ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                inner()
            }
        }
    )
}*/
@Composable
fun HighlightsEditor(
    websiteUrl: String,
    textState: TextFieldValue,
    onChange: (TextFieldValue) -> Unit
) {

    BasicTextField(
        value = textState,
        onValueChange = { newValue ->
            onChange(newValue)
        },
        visualTransformation = { textValue ->
            TransformedText(
                highlightWords(textValue.text),
                OffsetMapping.Identity
            )
        },
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        decorationBox = { inner ->
            Box(
                Modifier
                    .fillMaxSize()
//                    .padding(4.dp)
            ) {
                inner()
            }
        }
    )
}

//@Preview
@Composable
fun WebsiteTextField(
    label: String,
    hintText: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val lightBlackColor = LightBlack
    val fieldOuterBg = GrayBG5

    Column {

        // Outer field container (light grey rectangle)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(fieldOuterBg, RoundedCornerShape(5.dp))
                .border(1.dp, GrayOuterBorder, RoundedCornerShape(5.dp))
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = label,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.SemiBold,
                color = lightBlackColor,
            )

            Spacer(Modifier.width(10.dp))

            // Inner pill container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .border(1.dp, GrayInnerBorder, RoundedCornerShape(50))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppBasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .offset(y = -1.dp),
                        placeholder = hintText,
                        containerColor = Color.Transparent,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 16.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 22.sp,
                            color = lightBlackColor
                        )
                    )

                    if (value.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(5.dp))
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
