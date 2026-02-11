package com.joyersapp.feature.post.presentation

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.common_widgets.IdentificationDialog
import com.joyersapp.components.dialogs.BirthdayDatePickerDialog
import com.joyersapp.components.dialogs.DescriptionDialog
import com.joyersapp.components.dialogs.EditProfileHeaderDialog
import com.joyersapp.components.dialogs.HighlightBullet
import com.joyersapp.components.dialogs.LanguageSelectionDialog
import com.joyersapp.components.dialogs.MentionJoyersDialog
import com.joyersapp.components.dialogs.MultipleSelectionsDialog
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.core.NetworkConfig
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.BulletRowText
import com.joyersapp.feature.profile.presentation.HighlightedText
import com.joyersapp.feature.profile.presentation.ProfileTopHeader
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileUiState
import com.joyersapp.feature.profile.presentation.UserProfileViewModel
import com.joyersapp.feature.profile.presentation.identity.ProfileIdentitySection
import com.joyersapp.feature.profile.presentation.status.ProfileStatusSection
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayBG
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack35
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.filteredBio
import com.joyersapp.utils.flagEmoji
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.toPrettyNumber

@Preview
@Composable
private fun ScreenPreview() {
    CreateJoyScafold(
        CreateJoyUiState(

        )
    )
}

@Composable
fun CreateJoyScreen(
    viewModel: CreateJoyViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()



    HardBlockingLoader(state.isLoading)

    CreateJoyScafold(state)

//    }
}

@Composable
private fun CreateJoyScafold(state: CreateJoyUiState) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CreateJoyHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
                    .background(White),
                onBack = {},
                onMenu = {}
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = GrayOuterBorder
            )

            JoyerRow(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth()
                    .height(57.dp)
                    .padding(horizontal = 15.dp)
            )

        }

        PlaceholderRow(
            Modifier
                .align(Alignment.Center)
                .padding(bottom = 57.dp)
        )

        JoyersCup(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 45.dp)
        )

        CameraFab(
            Modifier
                .padding(bottom = 45.dp, end = 15.dp)
                .align(Alignment.BottomEnd)
        )

    }
}

@Composable
private fun CreateJoyHeader(
    modifier: Modifier,
    onBack: () -> Unit = {},
    onMenu: () -> Unit = {},
) {

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(start = 16.dp)
//                .align(Alignment.CenterStart)
                .noRippleClickable { onBack() },
            contentAlignment = Alignment.CenterEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cross_golden),
                contentDescription = "Back",
                modifier = Modifier
                    .size(13.5.dp)
            )
        }

        // Title
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Create Post",
                fontSize = 24.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                maxLines = 1,
                color = LightBlack,
                overflow = TextOverflow.Ellipsis
            )
        }

        // right menu
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(end = 15.dp)
                .align(Alignment.CenterEnd)
                .noRippleClickable { onMenu() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Drafts",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                maxLines = 1,
                color = Golden
            )
        }

    }
}

@Composable
private fun JoyerRow(
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Avatar
        Box(
            modifier = Modifier
                .size(57.dp)
                .border(width = 2.dp, color = AvatarBorder, shape = CircleShape)
                .padding(2.dp)
                .border(width = 2.5.dp, color = White, shape = CircleShape)
                .clip(CircleShape)
                .background(Gray20),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "avatar", modifier = Modifier.size(52.5.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Texts
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "James",
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = LightBlack,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

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

                Spacer(modifier = Modifier.width(7.dp))
                Box(
                    modifier = Modifier
                        .size(3.dp)
                        .clip(CircleShape)
                        .background(LightBlack55)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Image(
                    painter = painterResource(R.drawable.ic_lock_heart_black),
                    contentDescription = "Star",
                    modifier = Modifier
                        .size(9.5.dp, 14.19.dp)
                )
            }

            Spacer(Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Engineer",
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    color = Golden,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, false)
                )
            }
        }
    }
}

@Composable
private fun PlaceholderRow(modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Let’s Joy",
            fontSize = 50.sp,
            lineHeight = 24.sp,
            color = LightBlack35,
            fontWeight = FontWeight.Normal,
            fontFamily = fontFamilyLato,
            maxLines = 1
        )

        Spacer(Modifier.width(20.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_joy_emoji),
            contentDescription = "Let’s Joy",
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun JoyersCup(modifier: Modifier) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_joy_emoji),
            contentDescription = "Joyers Cup",
            modifier = Modifier.size(49.dp)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = "Joyers Cup",
            fontSize = 21.sp,
            lineHeight = 24.sp,
            color = Golden,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamilyLato,
            maxLines = 1
        )
    }
}

@Composable
private fun CameraFab(modifier: Modifier) {
    Box (
        modifier = modifier
            .clip(CircleShape)
            .size(47.dp)
            .border(width = 1.5.dp, color = Golden, shape = CircleShape),
    ) {
        Image(
            painter = painterResource(id = R.drawable.camera_inside_color),
            contentDescription = "Joyers Cup",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 13.5.dp)
                .size(23.4.dp, 18.11.dp)
        )
    }
}