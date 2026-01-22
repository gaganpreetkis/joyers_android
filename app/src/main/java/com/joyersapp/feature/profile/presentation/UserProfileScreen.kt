package com.joyersapp.feature.profile.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.common_widgets.IdentificationDialog
import com.joyersapp.components.dialogs.DescriptionDialog
import com.joyersapp.components.layouts.CustomProgressIndicator
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.identity.ProfileIdentitySection
import com.joyersapp.feature.profile.presentation.status.ProfileStatusSection
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.filteredBio
import com.joyersapp.utils.flagEmoji
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.toPrettyNumber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel,
    editMagnetics: () -> Unit = {},
    onMenu: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(UserProfileEvent.Load)
    }

    HardBlockingLoader(state.isLoading)
//    if (state.isLoading) {
//        CustomProgressIndicator()
//    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
            ) {

                ProfileTopHeader(
                    state = state,
                    onBack = { viewModel.onEvent(UserProfileEvent.Logout(0)) },
                    onMenu = onMenu
                )

                // Content (scrollable)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {

                    ProfileInfo(state)

                    BioSection(
                        bioText = state.bio?:"",
                        linkText = state.websiteUrl?:"",
                        onLinkClick = {}
                    )

                    if (state.bio.isNullOrEmpty() && state.websiteUrl.isNullOrEmpty()) {
                        Spacer(Modifier.height(30.dp))
                    }

                    StatsRow(state)

                    Spacer(modifier = Modifier.height(30.dp))

                    MagneticsRow(
                        editMagnetics = {
                            viewModel.onEvent(UserProfileEvent.InitMagneticsData)
                            editMagnetics()
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DashedLine(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    CustomScrollableTabRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(27.dp),
                        tabs = state.tabs,
                        onTabClick = { index ->
                            viewModel.onEvent(UserProfileEvent.TabSelected(index))
                        },
                        selectedTabIndex = state.selectedTab,
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    ProfileTabsContainer(state, viewModel)

                }
            }
            // Identification Dialog
            if (state.showIdentificationDialog) {
                IdentificationDialog(
                    viewModel = viewModel,
                    onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false)) },
                    onApply = {
                        viewModel.onEvent(UserProfileEvent.InitMagneticsData)
                        viewModel.onEvent(UserProfileEvent.OnApplyIdentification(it))
                        viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(false))
                        // navigate to edit magnetics
                        editMagnetics()
                    },
                )
            }

            if (state.showDescriptionDialog) {
                DescriptionDialog (
                    initList = viewModel.uiState.value.titles,
                    selectedTitle = ProfileTitlesData(
                        id = viewModel.uiState.value.magneticsData.title?.id?:"",
                        name = viewModel.uiState.value.magneticsData.title?.name?:"",
                    ),
                    selectedSubTitle = ProfileTitlesData(
                        id = viewModel.uiState.value.magneticsData.subTitle?.id,
                        name = viewModel.uiState.value.magneticsData.subTitle?.name,
                    ),
                    onDismiss = { viewModel.onEvent(UserProfileEvent.ToggleDescriptionDialog(false)) },
                    onApply = { title, subTitle ->
                        viewModel.onEvent(UserProfileEvent.InitMagneticsData)
                        viewModel.onEvent(UserProfileEvent.OnApplyDescription(title, subTitle))
                        editMagnetics()
                    }
                )
            }
        }
//    }
}


@Composable
fun ProfileInfo(state: UserProfileUiState) {
    val gold = Golden
    val lightBlackText = LightBlack

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidth = with(density) { configuration.screenWidthDp.dp }

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .height(241.dp)
    ) {

        /** ---------------- Banner ---------------- */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(gold)
        )

        if (!state.backgroundPicture.isNullOrEmpty()) {
            AsyncImage(
                model = "${NetworkConfig.IMAGE_BASE_URL}${state.backgroundPicture}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop,
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(35.dp)
                .clip(CircleShape)
                .background(White.copy(alpha = 0.75f))
                .clickable { }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_edit_pencil),
                contentDescription = "Edit",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(15.7.dp)
            )
        }

        /** ---------------- Profile Picture ---------------- */
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 87.dp)
                .border(width = 3.dp, color = White, shape = CircleShape)
                .padding(3.dp)
                .border(
                    width = 3.dp,
                    color = if (!state.profilePicture.isNullOrEmpty()) AvatarBorder else Golden,
                    shape = CircleShape
                )
                .padding(3.dp)
                .border(width = 3.dp, color = White, shape = CircleShape)
                .size(115.dp)
                .clip(CircleShape)
                .background(Gray20), contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_nav_joyers_home), // your J icon
                contentDescription = "avatar", modifier = Modifier.size(66.dp)
            )
            if (!state.profilePicture.isNullOrEmpty()) {
                AsyncImage(
                    model = "${NetworkConfig.IMAGE_BASE_URL}${state.profilePicture}",
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(109.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        /** Refresh badge */
        if (!state.profilePicture.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .offset(x = 106.dp, y = 183.dp)
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(Color.White), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_refresh_golden),
                    contentDescription = "refresh",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        /** ---------------- Text Content ---------------- */
        // Name, subtitle, location
        Column(
            modifier = Modifier.padding(start = 154.dp, top = 130.dp)
                .width(screenWidth - 174.dp)
        ) {
            // fullname
            Text(
                text = state.fullname,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = lightBlackText,
                lineHeight = 22.sp,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            // typical joyer
            Text(
                text = state.subTitle?.name?: state.title?.name?: state.joyerStatus?:"Typical Joyer" ,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                color = gold,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(4.dp))

            // location
            if (!state.location?.name.isNullOrEmpty() || !state.joyerLocation.isNullOrEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.location?.name ?: state.joyerLocation ?: "",
                        fontSize = 12.sp,
                        color = LightBlack60,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fontFamilyLato,
                        lineHeight = 17.sp
                    )

                    Spacer(Modifier.width(5.dp))

//                Image(
//                    painter = painterResource(id = com.hbb20.R.drawable.flag_united_states_of_america),
//                    contentDescription = "flag",
//                    modifier = Modifier.size(18.76.dp, 12.22.dp)
//                )

                    Text(
                        modifier = Modifier.size(18.76.dp, 12.22.dp),
                        text = flagEmoji(state.location?.name ?: state.joyerLocation ?: ""),
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BioSection(
    bioText: String,
    linkText: String,
    onLinkClick: () -> Unit
) {
    if (bioText.isNotEmpty() || linkText.isNotEmpty()) {
        Column (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 17.dp)
                .fillMaxWidth(),
        ) {

            if (bioText.isNotEmpty()) {
                // ----- BIO RICH TEXT -----
                HighlightedText(bioText.filteredBio())
            }

            if (linkText.isNotEmpty()) {

                if (bioText.isNotEmpty()) Spacer(Modifier.height(10.dp))

                // ----- LINK ROW -----
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onLinkClick() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = null,
                        modifier = Modifier.height(14.5.dp).width(14.5.dp)
                    )
                    Spacer(Modifier.width(5.dp))

                    Text(
                        text = linkText,
                        fontSize = 14.sp,
                        color = Golden,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp,
                        fontFamily = fontFamilyLato,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(15.dp))
            DashedLine(modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(15.dp))
        }
    }


}

@Composable
fun StatsRow(state: UserProfileUiState) {
    val lightBlackText = LightBlack
    // Stats row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // likes
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
                text = state.likes?.toInt()?.toPrettyNumber()?:"",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = lightBlackText,
                lineHeight = 22.sp
            )
        }
        // following
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
                text = state.following?.toInt()?.toPrettyNumber()?:"",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = lightBlackText,
                lineHeight = 22.sp
            )
        }
        // followers
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
                text = state.followers?.toInt()?.toPrettyNumber()?:"",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamilyLato,
                color = lightBlackText,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun MagneticsRow(editMagnetics: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Action card (Edit Magnetics 80%)
        Card(
            modifier = Modifier
                .size(150.dp, 35.dp)
                .clickable {
                    editMagnetics()
                },
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlack),

            ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Edit Magnetics   80%",
                    color = White,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.offset(y = -1.dp)
                )
            }
        }

        // Right side quick icons column (message, bell, bookmark)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_mail_golden),
                contentDescription = "msg",
                modifier = Modifier.width(23.04.dp)
            )
            Spacer(modifier = Modifier.width(27.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_notification_bell_golden),
                contentDescription = "notify",
                modifier = Modifier.size(24.47.dp, 27.56.dp)
            )
            Spacer(modifier = Modifier.width(27.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_bookmark_golden),
                contentDescription = "bookmark",
                modifier = Modifier.size(24.dp, 35.dp)
            )
//                    }
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
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 0.dp),  // No edge padding
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(tabs) { idx, title ->
            val isTabSelected = selectedTabIndex == idx
            var textWidth by remember { mutableStateOf(0.dp) }
            val localDensity = LocalDensity.current

            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(27.dp)
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
                    lineHeight = 19.sp,
                    modifier = Modifier
//                        .height(19.dp)
                        .padding(
                            start = 0.dp,
                            end = 0.dp,
                            top = 0.dp,
                            bottom = 0.dp
                        )  // No horizontal padding
                        .onGloballyPositioned { layoutCoordinates ->
                            textWidth = with(localDensity) { layoutCoordinates.size.width.toDp() }

                        }
                )
                Spacer(Modifier.height(5.dp))
                if (isTabSelected) {
                    Box(
                        modifier = Modifier
                            .width(textWidth)
                            .height(3.dp)
                            .background(Golden)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileTabsContainer(state: UserProfileUiState, viewModel: UserProfileViewModel) {
    // Tab content
    when (state.selectedTab) {
        0 -> Column {
            val headers = arrayListOf("Description", "Joyer Status", state.joyerStatus)
            if (state.title != null) headers.add(state.title?.name?: "")
            if (state.subTitle != null) headers.add(state.subTitle?.name?: "")
            ProfileStatusSection(
                state = state,
                onEditDescription = {
                    viewModel.onEvent(
                        UserProfileEvent.ToggleDescriptionDialog(
                            show = true,
                            titlesData = state.titles,
                        )
                    )
                })
        }

        1 -> Column {
            ProfileIdentitySection(
                state = state,
                onEditIdentity = {
                    viewModel.onEvent(UserProfileEvent.ToggleIdentificationDialog(true))
                },
            )
        }

        else -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp), contentAlignment = Alignment.Center
        ) {
            Text("Content for ${state.tabs[state.selectedTab]}")
        }
    }
}

