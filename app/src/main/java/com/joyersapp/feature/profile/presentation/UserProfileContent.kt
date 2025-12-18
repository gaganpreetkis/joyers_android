package com.joyersapp.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.feature.profile.presentation.identity.ProfileIdentitySection
import com.joyersapp.feature.profile.presentation.status.ProfileStatusSection
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable

// ---- Profile Content ----
@Preview
@Composable
fun UserProfileContent(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    onEditProfile: () -> Unit = {},
    onMessage: () -> Unit = {},
    onNotify: () -> Unit = {},
    onBookmark: () -> Unit = {},
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // main scrollable column content (scrolls under fixed header)

    Surface() {
        Column(modifier = modifier
            .fillMaxSize()
            .background(White)) {

            val gold = Golden60
            val lightBlackText = LightBlack

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(241.dp)
            ) {

                /** ---------------- Banner ---------------- */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(gold)
                )

                /** ---------------- Avatar ---------------- */

                // Avatar content
                Box(
                    modifier = Modifier
                        .offset(x = 20.dp, y = 87.dp)
                        .border(width = 3.dp, color = White, shape = CircleShape)
                        .padding(3.dp)
                        .border(width = 3.dp, color = Golden60, shape = CircleShape)
                        .padding(3.dp)
                        .border(width = 3.dp, color = White, shape = CircleShape)
                        .size(115.dp)
                        .clip(CircleShape)
                        .background(Gray20),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_nav_joyers_home), // your J icon
                        contentDescription = "avatar",
                        modifier = Modifier.size(66.dp)
                    )
                }

                /** Refresh badge */
                Box(
                    modifier = Modifier
                        .offset(x = 106.dp, y = 183.dp)
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_refresh_golden),
                        contentDescription = "refresh",
                        modifier = Modifier.size(20.dp)
                    )
                }

                /** ---------------- Text Content ---------------- */
                // Name, subtitle, location
                Column(
                    modifier = Modifier
                        .offset(x = 154.dp, y = 130.dp)
                ) {

                    Text(
                        text = state.displayName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamilyLato,
                        color = lightBlackText,
                        lineHeight = 22.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = state.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = gold,
                        lineHeight = 17.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = state.location,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fontFamilyLato,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.width(5.dp))

                        Image(
                            painter = painterResource(id = com.hbb20.R.drawable.flag_united_states_of_america),
                            contentDescription = "flag",
                            modifier = Modifier.size(18.76.dp, 12.22.dp)
                        )
                    }
                }
            }

            // action button and stats
            Column {

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Likes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack60,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.likes,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = lightBlackText,
                            lineHeight = 22.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Following",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack60,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.following,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = lightBlackText,
                            lineHeight = 22.sp
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Followers",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = fontFamilyLato,
                            color = LightBlack60,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.followers,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fontFamilyLato,
                            color = lightBlackText,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    // Action card (Edit Magnetics 80%)
                    Card(
                        modifier = Modifier.size(150.dp, 35.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = CardDefaults.cardColors(containerColor = LightBlack),

                        ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Edit Magnetics   80%",
                                color = White,
                                fontSize = 12.sp,
                                lineHeight = 15.sp,
                                fontFamily = fontFamilyLato,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                    }

                    // Right side quick icons column (message, bell, bookmark)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                    IconButton(onClick = onMessage, modifier = Modifier.size(23.04.dp, 16.13.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mail_golden),
                            contentDescription = "msg",
                            modifier = Modifier.size(23.04.dp, 16.13.dp)
                        )
                        Spacer(modifier = Modifier.width(27.dp))
//                    }
//                    IconButton(onClick = onNotify, modifier = Modifier.size(20.dp, 27.56.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_notification_bell_golden),
                            contentDescription = "notify",
                            modifier = Modifier.size(24.47.dp, 27.56.dp)
                        )
//                    }
//                    IconButton(onClick = onBookmark, modifier = Modifier.size(24.dp, 35.dp)) {
                        Spacer(modifier = Modifier.width(27.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_bookmark_golden),
                            contentDescription = "bookmark",
                            modifier = Modifier.size(24.dp, 35.dp)
                        )
//                    }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // dashed divider (approx)
                DashedLine(Modifier.fillMaxWidth().padding(horizontal = 20.dp))

                Spacer(modifier = Modifier.height(11.dp))

                // Tabs row
                CustomScrollableTabRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp),
                    tabs = state.tabs,
                    onTabClick = { index ->
                        viewModel.onEvent(UserProfileEvent.TabSelected(index))
                    },
                    selectedTabIndex = state.selectedTab,
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tab content (sample)
                when (state.selectedTab) {
                    0 -> Column {
                        ProfileStatusSection(
                            onEditDescription = {
                                viewModel.onEvent(UserProfileEvent.OnEditDescriptionClicked(state.selectedTab))
                            }
                        )
                    }
                    1 -> Column {
                        ProfileIdentitySection(
                            onEditDescription = {
                                viewModel.onEvent(UserProfileEvent.OnEditDescriptionClicked(state.selectedTab))
                            }
                        )
                    }

                    else -> Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Content for ${state.tabs[state.selectedTab]}")
                    }
                }
            }
        }
    }
}


@Composable
fun CustomScrollableTabRow(
    modifier: Modifier,
    tabs: List<String>,
    onTabClick: (Int) -> Unit,
    selectedTabIndex: Int,
) {
    // Custom LazyRow for tabs (replaces ScrollableTabRow)
    LazyRow(
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),  // 🔥 Zero spacing between items
        contentPadding = PaddingValues(horizontal = 0.dp),  // No edge padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(tabs) { idx, title ->
            val isTabSelected = selectedTabIndex == idx
            var textWidth by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current

            Column (
                modifier = Modifier
                    .wrapContentWidth()
                    .height(28.dp)
                    .noRippleClickable() {
                        onTabClick(idx)
                    },
//                horizontalAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    color = if (isTabSelected) LightBlack else LightBlack60,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)  // No horizontal padding
                        .height(19.dp)
                        .onGloballyPositioned { layoutCoordinates ->
                            textWidth = with(localDensity) { layoutCoordinates.size.width.toDp() }

                        }
                )
                Spacer(Modifier.height(6.dp))
                if (isTabSelected) {
                    Box(
                        modifier = Modifier
                            .width(textWidth)
                            .height(3.dp)
                            .background(Golden60)
                    )
                }
            }
        }
    }
/*
// Custom indicator (place this below the LazyRow, e.g., in a Box overlay)
    val tabPositions = remember { mutableStateListOf<TabPosition>() }  // You'll need to track positions manually
// ... (implement position tracking similar to ScrollableTabRow's internal logic, or use a library)
// Then draw the indicator as before:
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = *//* calculate based on tabPositions *//*)
            .width(state.textWidths[state.selectedTab] ?: edgePadding)
            .height(3.dp)
            .background(color = gold)
    )*/
}