package com.joyersapp.auth.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.response.Subtitle
import com.joyersapp.response.Title
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.common_widgets.DualViewDialog
import com.joyersapp.common_widgets.ImagePickerBottomSheet
import com.joyersapp.common_widgets.ImagePickerBottomSheetBack
import com.joyersapp.common_widgets.showCCPDialog
import com.joyersapp.theme.Black
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.Red
import com.joyersapp.utils.annotatedFromBoldTags
import com.joyersapp.utils.containsEmoji
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.isAllowedIdentityNameChars
import com.joyersapp.utils.isValidNameAdvanced
import kotlinx.coroutines.launch
@Preview
@Composable
fun IdentityScreen(
    initialPage: Int = 0,
//    signupViewModel: SignupViewModel,
//    preferencesManager: PreferencesManager,
//    activity: AppCompatActivity,
    onNavigateBack: () -> Unit = {},
    onNavigateToNext: () -> Unit = {}
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(initialPage = initialPage) { 3 }
    var currentPage by remember { mutableStateOf(initialPage) }
    val coroutineScope = rememberCoroutineScope()

    val pageTitles = listOf(
        context.getString(R.string.identity),
        context.getString(R.string.status),
        context.getString(R.string.status)
    )

    val pageCounts = listOf("1/3", "2/3", "3/3")
    val progressValues = listOf(33, 66, 100)

    // Update current page when pager state changes
    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }

    // Update pager when initial page changes
    LaunchedEffect(initialPage) {
        if (pagerState.currentPage != initialPage) {
            pagerState.animateScrollToPage(initialPage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
// Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
// Back button
                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxSize()
                        .clickable(enabled = currentPage > 0) {
                            if (currentPage > 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage - 1)
                                }
                            }
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (currentPage > 0) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .size(20.dp, 17.dp),
                            colorFilter = ColorFilter.tint(Golden60)
                        )
                    }
                }

// Title
                Text(
                    text = pageTitles[currentPage],
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = Black,
                    modifier = Modifier.weight(0.6f),
                    textAlign = TextAlign.Center
                )

                // Page count
                Text(
                    text = pageCounts[currentPage],
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = Golden60,
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(end = 20.dp),
                    textAlign = TextAlign.End
                )
            }

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(Gray20)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progressValues[currentPage] / 100f)
                        .fillMaxHeight()
                        .background(Golden60)
                )
            }

            // Pager content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> PageOneContent(
                        onNext = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        onNavigateToNext = { onNavigateToNext() }
//                        signupViewModel = signupViewModel,
//                        preferencesManager = preferencesManager,
//                        activity = activity
                    )
                    1 -> PageTwoContent(
                        onNext = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page + 1)
                            }
                        },
                        onBack = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page - 1)
                            }
                        },
////                        signupViewModel = signupViewModel,
////                        preferencesManager = preferencesManager,
////                        activity = activity
                    )
                    2 -> PageThreeContent(
                        onBack = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page - 1)
                            }
                        },
                        onNavigateToNext = {onNavigateToNext()}
////                        signupViewModel = signupViewModel,
////                        preferencesManager = preferencesManager,
////                        activity = activity
                    )
                }
            }
        }
    }
}



@Composable
fun PageOneContent(
    onNext: () -> Unit,
    onNavigateToNext: () -> Unit,
//    signupViewModel: SignupViewModel? = null,
//    preferencesManager: PreferencesManager? = null,
//    activity: AppCompatActivity? = null
) {
    var username by remember { mutableStateOf("") }
    var isUsernamFocused by remember { mutableStateOf(false) }
    var countryName by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var headerImageUri by remember { mutableStateOf<Uri?>(null) }
    var showProfilePlaceholder by remember { mutableStateOf(true) }
    var showHeaderPicker by remember { mutableStateOf(true) }
    var showImagePickerBottomSheet by remember { mutableStateOf(false) }
    var showImagePickerBottomSheetBack by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    var headerPath by remember { mutableStateOf<String?>(null) }
    var selectedCountryCode by remember { mutableStateOf("") }

    val context = LocalContext.current
    val goldenColor = Golden60
    val lightBlackColor = LightBlack
    val hintColor = Gray40
    val whiteColor = Color.White
    val redColor = Red
    val astrikeColor = colorResource(id = R.color.astrike_color)
    val focusManager = LocalFocusManager.current

    val maxLength = 45
    var remainingChars by remember { mutableStateOf(maxLength) }

    if (!isUsernamFocused && username.isNotEmpty() && remainingChars > 43) {
        usernameError = stringResource(R.string.username_error)
    }

    // Image picker launchers
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            profileImageUri = it
            showProfilePlaceholder = false
//            signupViewModel?.let { vm ->
//                preferencesManager?.let { pm ->
//                    val scope = (context as? AppCompatActivity)?.lifecycleScope
//                    scope?.launch {
//                        val token = pm.getAccessToken()
//                        if (token != null) {
//                            vm.uploadImage(it, token)
//                        }
//                    }
//                }
//            }
        }
    }

    // Observe API responses
//    val imageUploadResponse = signupViewModel?.imageUploadResponse?.observeAsState()
//    val setPageResponse = signupViewModel?.setPageResponse?.observeAsState()
//
//    LaunchedEffect(imageUploadResponse?.value) {
//        imageUploadResponse?.value?.let { response ->
//            val apiResultHandler = ApiResultHandler<UploadResponse>(
//                context as AppCompatActivity,
//                onLoading = { },
//                onSuccess = {
//                    // Store image path based on which image was selected
//                    if (profileImageUri != null) {
//                        imagePath = it?.data?._id
//                    } else if (headerImageUri != null) {
//                        headerPath = it?.data?._id
//                    }
//                },
//                onFailure = { }
//            )
//            apiResultHandler.handleApiResult(response)
//        }
//    }

//    LaunchedEffect(setPageResponse?.value) {
//        setPageResponse?.value?.let { response ->
//            val apiResultHandler = ApiResultHandler<BaseResponse>(
//                context as AppCompatActivity,
//                onLoading = { },
//                onSuccess = {
//                    val intent = Intent(context, JoyersAuthActivity::class.java)
//                    context.startActivity(intent)
//                    (context as AppCompatActivity).finish()
//                },
//                onFailure = { }
//            )
//            apiResultHandler.handleApiResult(response)
//        }
//    }

    // Validation
    val isNameValid = remember(username) {
        username.isEmpty() || (isValidNameAdvanced(username) && username.length > 1)
    }

    val showNextButton = remember(username, countryName) {
        username.isNotEmpty() && countryName.isNotEmpty() &&
                /*isValidNameAdvanced(username) && */username.length > 1
    }

    val showSkipButton = remember(username, countryName) {
        username.isEmpty() && countryName.isEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(25.dp)
            .imePadding()
    ) {
        // Profile Picture Section
        Text(
            text = context.getString(R.string.profile_picture),
            fontSize = 18.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.SemiBold,
            color = lightBlackColor,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Card with profile and header images
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .border(
                    width = 1.dp,
                    color = GrayLightBorder,
                    shape = RoundedCornerShape(5.dp)
                ),
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(containerColor = Gray20)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Header/Background Image
                if (headerImageUri != null) {
                    AsyncImage(
                        model = headerImageUri,
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
                                headerImageUri = null
                                showHeaderPicker = true
                                headerPath = null
                            }
                    )
                } else {
                    // Header picker button
                    if (showHeaderPicker) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(15.dp)
                                .clickable(interactionSource = remember { MutableInteractionSource()},indication = null) {
//                                    headerImagePickerLauncher.launch("image/*")
                                    showImagePickerBottomSheetBack = true
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(37.dp)
                                    .background(Color.White, CircleShape)
                                    .border(1.dp, GrayLightBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.camera_inside_color),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = context.getString(R.string.header),
                                fontSize = 11.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Normal,
                                color = LightBlack60
                            )
                        }
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
                                if (showProfilePlaceholder) GrayLightBorder else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        if (profileImageUri != null) {
                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                        } else if (showProfilePlaceholder) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        showImagePickerBottomSheet = true
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
                                    text = context.getString(R.string.upload_picture),
                                    fontSize = 15.sp,
                                    fontFamily = fontFamilyLato,
                                    fontStyle = FontStyle.Normal,
                                    color = LightBlack60
                                )
                            }
                        }
                    }

                    if (profileImageUri != null) {
                        // Close button for profile
                        Image(
                            painter = painterResource(id = R.drawable.ic_cross_round_border_golden),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(40.dp)
                                .align(Alignment.BottomEnd)
                                .clickable {
                                    profileImageUri = null
                                    showProfilePlaceholder = true
                                    imagePath = null
                                }
                        )
                    }

                }


            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Name Section
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = context.getString(R.string.name_only),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                color = lightBlackColor
            )
            Text(
                text = " *",
                fontSize = 24.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.ExtraBold,
                color = astrikeColor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Name Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(
                    color = Gray20,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (usernameError != null) Red else GrayLightBorder,
                    shape = RoundedCornerShape(5.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            AppBasicTextField(
                value = username,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                onValueChange = {
                    if (it.length <= maxLength) {
                        username = it
                        if (it.isEmpty()) {
                            remainingChars = maxLength - it.length
                            usernameError = null
                            return@AppBasicTextField
                        }
                        if (containsEmoji(it) || !isAllowedIdentityNameChars(it)) {
                            remainingChars = maxLength - (it.length / 2)
                            usernameError = context.getString(R.string.username_error)
                        } else {
                            remainingChars = maxLength - it.length
                            usernameError = null
                        }
                    }
                },
                placeholder = stringResource(R.string.joyer_name),
                modifier = Modifier.weight(0.8f)
                    .fillMaxWidth()
                    .imePadding()
                    .focusRequester(remember { FocusRequester() })
                    .onFocusChanged { focusState ->
                        isUsernamFocused = focusState.isFocused
                    },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = if (username.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    fontFamily = fontFamilyLato
                ),
                maxLength = 45
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = remainingChars.toString(),
                fontSize = 12.sp,
                color = if (remainingChars == 0) redColor else hintColor,
                modifier = Modifier.fillMaxHeight().padding(top = 5.dp, end = 7.dp),
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.Normal,
            )
        }

        if (usernameError != null) {
            Text(
                text = usernameError!!,
                color = redColor,
                fontSize = 14.sp,
                fontFamily = fontFamilyLato,
                fontStyle = FontStyle.Normal,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 3.dp)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Location Section
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = context.getString(R.string.joyer_location_only),
                fontSize = 18.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.SemiBold,
                color = lightBlackColor
            )
            Text(
                text = context.getString(R.string.strik_left_space),
                fontSize = 24.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.ExtraBold,
                color = astrikeColor
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Country Selection
        Box(
            Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(Gray20, RoundedCornerShape(5.dp))
                .border(1.dp,GrayLightBorder, RoundedCornerShape(5.dp))
                .clickable {
//                    activity?.let {
                    showCCPDialog(
                        context,
                        showPhoneCode = false
                    ) { code, name, flag, _ ->
                        countryName = name
                        selectedCountryCode = code
                    }
//                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (countryName.isNotEmpty()) countryName else context.getString(R.string.select_location),
                fontSize = 16.sp,
                fontWeight = if (countryName.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                fontFamily = fontFamilyLato,
                color = if (countryName.isNotEmpty()) lightBlackColor else hintColor,
//                    modifier = Modifier.weight(0.33f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ) {

                Image(
                    painter = painterResource(id = R.drawable.drop_down),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 15.dp).size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Next/Skip Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (showSkipButton) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            color = whiteColor,
                            shape = CircleShape
                        )
                        .border(1.dp, goldenColor, CircleShape)
                        .clickable {
                            focusManager.clearFocus()
                            onNavigateToNext()
//                            preferencesManager?.let { pm ->
//                                activity?.lifecycleScope?.launch {
//                                    val token = pm.getAccessToken()
//                                    val userID = pm.getUserId()
//                                    if (token != null && userID != null) {
//                                        signupViewModel?.setPageNumber(
//                                            token = token,
//                                            userId = userID,
//                                            updateRequest = UpdateUserRequest(is_skipped = true)
//                                        )
//                                    }
//                                }
//                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = context.getString(R.string.skip),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = goldenColor
                    )
                }
            }

            if (showNextButton) {
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(goldenColor, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            focusManager.clearFocus()
                            if (username.isNotEmpty() && isValidNameAdvanced(username) && username.length > 1) {
                                onNext()
                                // API call to update user would go here
                            } else {
                                usernameError = context.getString(R.string.username_error)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward_arrow_white),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp,25.dp),
                        colorFilter = ColorFilter.tint(whiteColor)
                    )
                }
            }
        }
        Spacer(Modifier.height(0.dp))
    }

    // Image Picker Bottom Sheet
    ImagePickerBottomSheet(
        showBottomSheet = showImagePickerBottomSheet,
        onDismiss = { showImagePickerBottomSheet = false },
        allowMultipleSelection = false,
        onImagesPicked = { uris ->
            profileImageUri = uris[0]
            showProfilePlaceholder = false
//            signupViewModel?.let { vm ->
//                preferencesManager?.let { pm ->
//                    activity?.lifecycleScope?.launch {
//                        val token = pm.getAccessToken()
//                        if (token != null) {
//                            vm.uploadImage(uris[0], token)
//                        }
//                    }
//                }
//            }
        },
        onCameraImagePicked = { uri ->
            profileImageUri = uri
            showProfilePlaceholder = false
//            signupViewModel?.let { vm ->
//                preferencesManager?.let { pm ->
//                    activity?.lifecycleScope?.launch {
//                        val token = pm.getAccessToken()
//                        if (token != null) {
//                            vm.uploadImage(uri, token)
//                        }
//                    }
//                }
//            }
        }
    )

    // Image Picker Bottom Sheet
    ImagePickerBottomSheetBack(
        showBottomSheet = showImagePickerBottomSheetBack,
        onDismiss = { showImagePickerBottomSheetBack = false },
        allowMultipleSelection = false,
        onImagesPicked = { uris ->
            headerImageUri = uris[0]
            showHeaderPicker = false
//            signupViewModel?.let { vm ->
//                preferencesManager?.let { pm ->
//                    activity?.lifecycleScope?.launch {
//                        val token = pm.getAccessToken()
//                        if (token != null) {
//                            vm.uploadImage(uris[0], token)
//                        }
//                    }
//                }
//            }
        },
        onCameraImagePicked = { uri ->
            headerImageUri = uri
            showHeaderPicker = false
//            signupViewModel?.let { vm ->
//                preferencesManager?.let { pm ->
//                    activity?.lifecycleScope?.launch {
//                        val token = pm.getAccessToken()
//                        if (token != null) {
//                            vm.uploadImage(uri, token)
//                        }
//                    }
//                }
//            }
        }
    )
}

@Composable
fun PageTwoContent(
    onNext: () -> Unit,
    onBack: () -> Unit,
//    signupViewModel: SignupViewModel? = null,
//    preferencesManager: PreferencesManager? = null,
//    activity: AppCompatActivity? = null
) {
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val goldenColor = Golden60
    val lightBlackColor = Black
    val blackColor = colorResource(id = R.color.black)
    val whiteColor = Color.White

    val statusOptions = listOf(
        "Classic" to context.getString(R.string.classic),
        "Celebrity" to context.getString(R.string.celebrity),
        "Proficient" to context.getString(R.string.proficient),
        "Leader" to context.getString(R.string.leader),
        "Provider" to context.getString(R.string.provider)
    )

//    val userInfoResponse = signupViewModel?.userInfoResponse?.observeAsState()

//    LaunchedEffect(userInfoResponse?.value) {
//        userInfoResponse?.value?.let { response ->
//            val apiResultHandler = ApiResultHandler<BaseResponse>(
//                context as AppCompatActivity,
//                onLoading = { },
//                onSuccess = {
//                    onNext()
//                },
//                onFailure = { }
//            )
//            apiResultHandler.handleApiResult(response)
//        }
//    }

    Box() {

        val showNextButton = selectedStatus == "Classic"
        val spaceHeight = if (showNextButton) 150.dp else 60.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(25.dp)
        ) {
            Text(
                text = context.getString(R.string.joyer_status),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                color = lightBlackColor,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status options
            statusOptions.forEach { (statusKey, statusText) ->
                val isSelected = selectedStatus == statusKey
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(
                            color = if (isSelected) goldenColor else whiteColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Gray20,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable {
                            selectedStatus = if (selectedStatus == statusKey) null else statusKey
                        }
                        .padding(17.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusText,
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) whiteColor else blackColor
                    )
                }
                if (statusKey != statusOptions.last().first) {
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Dashed line
            DashedLine(
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(2.dp)
                    .padding(horizontal = 0.dp),
                //strokeWidth = 1f
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Clarifications section
            Text(
                text = context.getString(R.string.clarifications),
                fontSize = 16.sp,
                fontFamily = fontFamilyLato,
                fontWeight = FontWeight.SemiBold,
                color = LightBlack,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth().border(
                    width = 1.dp,
                    color = GrayLightBorder,
                    shape = RoundedCornerShape(5.dp)
                ),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        text = context.getString(R.string.title1),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        color = lightBlackColor
                    )

                    Spacer(modifier = Modifier.height(13.dp))

                    // Styled text for each status
                    statusOptions.forEach { (statusKey, _) ->
                        val fullText = when (statusKey) {
                            "Classic" -> context.getString(R.string.classic_text)
                            "Celebrity" -> context.getString(R.string.celebrity_text)
                            "Proficient" -> context.getString(R.string.proficient_text)
                            "Leader" -> context.getString(R.string.leader_text)
                            "Provider" -> context.getString(R.string.provider_text)
                            else -> ""
                        }


                        if (fullText.isNotEmpty()) {
                            Text(
                                text = buildAnnotatedString {
                                    val heading = statusKey
                                    val start = fullText.indexOf(heading)
                                    val end = start + heading.length

                                    append(fullText.substring(0, start))
                                    withStyle(
                                        style = SpanStyle(
                                            color = goldenColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        append(heading)
                                    }
                                    append(fullText.substring(end))
                                },
                                fontSize = 16.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Normal,
                                color = lightBlackColor,
                                modifier = Modifier.padding(top = if (statusKey != "Classic") 7.dp else 0.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = annotatedFromBoldTags("Based on this classification, Joyers are registered under either social or professional titles, which correspond to one of five statuses: social titles fall under the <b>Classic</b> status, while professional titles fall under <b>Celebrity, Proficient, Leader,</b> or <b>Provider</b> statuses."),
                        fontSize = 16.sp, fontFamily = fontFamilyLato,
                        color = lightBlackColor
                    )


                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = annotatedFromBoldTags("In light of these classifications, <b>Classic</b> Joyers have access to all social and professional features of the Joyers app, with the exception of offering or selling products and services online. They can request or purchase products and services from other Joyers but are not permitted to sell them. Conversely, Joyers with the <b>Celebrity, Proficient, Leader,</b> or <b>Provider</b> status can access all social and professional features of the Joyers app, including the ability to offer and sell products and services online to other Joyers."),
                        fontSize = 16.sp, fontFamily = fontFamilyLato,
                        color = lightBlackColor
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = context.getString(R.string.title4),
                        fontSize = 16.sp,
                        fontFamily = fontFamilyLato,
                        fontWeight = FontWeight.Normal,
                        color = lightBlackColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(spaceHeight))
        }

        // Next button (floating)
        if (showNextButton) {
            Box(
                modifier = Modifier
                    .padding(end = 30.dp, bottom = 90.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(goldenColor, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            onNext()
                            // API call would go here
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward_arrow_white),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp, 25.dp),
                        alignment = Alignment.Center,
                        colorFilter = ColorFilter.tint(whiteColor)
                    )
                }
            }
        }
    }
}

@Composable
fun PageThreeContent(
    onBack: () -> Unit,
    onNavigateToNext: () -> Unit,
//    signupViewModel: SignupViewModel? = null,
//    preferencesManager: PreferencesManager? = null,
//    activity: AppCompatActivity? = null
) {
    var selectedTitle by remember { mutableStateOf<String?>(null) }
    var selectedTitleId by remember { mutableStateOf("") }
    var showNextButton by remember { mutableStateOf(false) }
    var showTitleDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val goldenColor = Golden60
    val lightBlackColor = Black
    val whiteColor = Color.White

//    val titlesApiResponse = signupViewModel?.titlesApiResponse?.observeAsState()
//    val userInfoResponse = signupViewModel?.userInfoResponse?.observeAsState()
    var titles by remember { mutableStateOf<List<Title>>(emptyList()) }

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

    // Load titles
//    LaunchedEffect(Unit) {
//        preferencesManager?.let { pm ->
//            activity?.lifecycleScope?.launch {
//                val token = pm.getAccessToken()
//                if (token != null) {
//                    signupViewModel?.getTitles(token)
//                }
//            }
//        }
//    }

//    LaunchedEffect(titlesApiResponse?.value) {
//        titlesApiResponse?.value?.let { response ->
//            val apiResultHandler = ApiResultHandler<com.synapse.joyers.apiData.response.TitlesApiResponse>(
//                context as AppCompatActivity,
//                onLoading = { },
//                onSuccess = {
//                    titles = response.data?.data?.data ?: emptyList()
//                },
//                onFailure = { }
//            )
//            apiResultHandler.handleApiResult(response)
//        }
//    }

//    LaunchedEffect(userInfoResponse?.value) {
//        userInfoResponse?.value?.let { response ->
//            val apiResultHandler = ApiResultHandler<BaseResponse>(
//                context as AppCompatActivity,
//                onLoading = { },
//                onSuccess = {
//                    val intent = Intent(context, com.synapse.joyers.ui.auth.JoyersAuthActivity::class.java)
//                    context.startActivity(intent)
//                    (context as AppCompatActivity).finish()
//                },
//                onFailure = { }
//            )
//            apiResultHandler.handleApiResult(response)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(2) {
                Spacer(modifier = Modifier.height(25.dp))
// Joyers Status
                if (it == 0) {
                    Text(
                        text = context.getString(R.string.joyer_status),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = lightBlackColor
                    )

                    Spacer(Modifier.height(5.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .background(
                                color = goldenColor,
                                shape = RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.classic),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = whiteColor
                        )
                    }
                } else {
//Title selection
                    Text(
                        text = stringResource(R.string.title),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = lightBlackColor
                    )

                    Spacer(Modifier.height(5.dp))

// Title selection button
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .background(
                                color = if (selectedTitle != null) goldenColor else whiteColor,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = if (selectedTitle == null) 1.dp else 0.dp,
                                color = goldenColor,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                showTitleDialog = !showTitleDialog
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = selectedTitle ?: context.getString(R.string.select_title),
                            fontSize = 16.sp,
                            fontFamily = fontFamilyLato,
                            fontWeight = if (selectedTitle != null) FontWeight.Bold else FontWeight.SemiBold,
                            color = if (selectedTitle != null) whiteColor else goldenColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        if (showNextButton) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(goldenColor, CircleShape)
                        .clickable {
                            // API call would go here
                            onNavigateToNext()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward_arrow_white),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp, 25.dp),
                        colorFilter = ColorFilter.tint(whiteColor)
                    )
                }
            }
        }
    }



    // Title Selection Dialog
    if (showTitleDialog) {
//        selectedTitle = "Master's Student"
//        showNextButton = true
        DualViewDialog(
//            titles = titles.toMutableList(),
            onDismiss = { showTitleDialog = false },
            onItemSelected = { titleId, titleName ->
                selectedTitle = titleName
                selectedTitleId = titleId
                showNextButton = true
                showTitleDialog = false
            }
        )
    } else {
//        selectedTitle = null
//        showNextButton = false
    }
}
