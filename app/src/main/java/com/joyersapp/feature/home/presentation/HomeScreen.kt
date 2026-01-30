package com.joyersapp.feature.home.presentation

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.components.layouts.JoyersHeader
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack40
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.highlightWords
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.tapToDismissKeyboard
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    Scaffold(
        modifier = Modifier.background(White)
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            JoyersHeader(
                onMenuClick = {

                }
            )
            // Avatar content
            Box(
                modifier = Modifier
                    .fillMaxSize()
//            .border(width = 3.dp, color = White, shape = CircleShape)
//            .padding(3.dp)
//            .border(width = 3.dp, color = Golden, shape = CircleShape)
//            .padding(3.dp)
//            .border(width = 3.dp, color = White, shape = CircleShape)
//            .size(115.dp)
//            .clip(CircleShape)
                    .background(GrayBG),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nav_joyers_home), // your J icon
                    contentDescription = "avatar",
                    modifier = Modifier.size(250.dp)
                )
            }
        }
//        var value by remember {
//            mutableStateOf(TextFieldValue(""))
//        }
//
//        value = value.copy(text = "klfjndksjfn")
//        OverviewEditor(
//            value,
//            onChange = { value = it }
//        )

//        LanguageDialog {  }
//        Dialog(onDismissRequest = {  },
//            properties = DialogProperties(
//                dismissOnBackPress = true,
//                dismissOnClickOutside = true,
//                usePlatformDefaultWidth = false,
//                decorFitsSystemWindows = false
//            )) {
//            HideSearchBarOnScrollScreen()
//        }

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
            onChange(it)
        },
        visualTransformation = { textValue ->
            TransformedText(
                highlightWords(textValue.text),
                OffsetMapping.Identity
            )
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            color = Color.Red // we paint using AnnotatedString
        ),
        modifier = Modifier.fillMaxWidth()
            .focusable(),
//            .defaultMinSize(minHeight = 140.dp),
        decorationBox = { inner ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )  {

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HideSearchBarOnScrollScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            // You can replace TopAppBar with the M3 SearchBar if needed,
            // managing its active state separately.
            // Wrap your SearchBar inside a TopAppBar
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = White,
                    scrolledContainerColor = White,
                    navigationIconContentColor = White,
                    titleContentColor = White,
                    actionIconContentColor = White
                ),
                title = {
                    SearchBarRow(
                        // Your SearchBar logic here
                    )
                },
                scrollBehavior = scrollBehavior
            )
            // Or use the M3 SearchBar:
            /*
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { *//* handle search *//* },
                active = false, // Set based on focus state if you want it to expand
                onActiveChange = { *//* handle active change *//* },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search items") }
            )
            */
        },
        content = { innerPadding ->


            LazyColumn(
                contentPadding = innerPadding,
//                modifier = Modifier.fillMaxSize()
            ) {
                items(50) { index ->
                    Text(
                        text = "Item #$index",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                }
            }
        }
    )
}


@Composable
fun LanguageDialog(
    onDismiss: () -> Unit
) {

    val listState = rememberLazyListState()
    var showSearchBar by remember { mutableStateOf(true) }

    LaunchedEffect(listState) {
        var lastOffset = 0
        val threshold = 20    // ⬅️ adjust sensitivity (4..12 works best)

        snapshotFlow { listState.firstVisibleItemScrollOffset }
//            .debounce(40) // ms interval
            .distinctUntilChanged()
            .collect { offset ->

                val delta = offset - lastOffset

                when {
                    delta > threshold -> {
                        // Scroll down → hide
                        if (showSearchBar) showSearchBar = false
                    }
                    delta < -threshold -> {
                        // Scroll up → show
                        if (!showSearchBar) showSearchBar = true
                    }
                }

                lastOffset = offset
            }
    }



    Dialog(onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(vertical = 50.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column {

                // 🔹 Animated Search Bar
                AnimatedVisibility(
                    visible = showSearchBar,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
                ) {
                    SearchBarRow(
                        searchQuery = "searchQuery",
                        showApplyButton = true,
                        onApply = {  },
                        onSearchQueryChanged = {  }
                    )
//                    Spacer(modifier = dialogModifier.height(20.dp))
                }

                // 🔹 Scrollable List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(50) { item ->
                        LanguageItem()
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBarRow(
    dialogModifier: Modifier = Modifier,
    searchQuery: String = "",
    showApplyButton: Boolean =true,
    onApply: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {}
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
                    placeholder = "Search Language",
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
                            .noRippleClickable() {
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
                    .noRippleClickable {
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
private fun LanguageItem(
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .noRippleClickable() { },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = "Language Name",
            fontSize = 16.sp,
            lineHeight = 22.sp,
            fontFamily = fontFamilyLato,
            fontWeight = FontWeight.Normal,
            color = LightBlack,
            //modifier = modifier.padding(top = if (isFirstItem && isSelected) 2.dp else 0.dp, bottom = if (isFirstItem && isSelected) 2.dp else 0.dp)
            //modifier = Modifier.weight(1f)
        )

        }
}