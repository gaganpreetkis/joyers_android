package com.joyersapp.feature.post.presentation.create_joy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.feature.post.domain.model.Joyer
import com.joyersapp.feature.post.presentation.common.CreateJoyHeader
import com.joyersapp.feature.post.presentation.common.JoyersCup
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack35
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable

@Preview
@Composable
private fun ScreenPreview() {
    CreateJoyScafold(
        CreateJoyUiState(

        ),
        navCreatePost = {}
    )
}

@Composable
fun CreateJoyScreen(
    viewModel: CreateJoyViewModel,
    navCreatePost: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CreateJoyEvent.LoadData)
    }

    HardBlockingLoader(state.isLoading)

    CreateJoyScafold(
        state,
        navCreatePost = navCreatePost
    )

//    }
}

@Composable
private fun CreateJoyScafold(
    state: CreateJoyUiState,
    navCreatePost: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .noRippleClickable{
                    navCreatePost()
                }
        ) {
            CreateJoyHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
                    .background(White),
                title = "Create Joy",
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
                    .padding(horizontal = 15.dp),
            Joyer(
                id = "1",
                name = "James",
                username = "james",
                profilePicture = "",
                tag = "Engineer",
                starsCount = 1,
                isLockVisible = true
            ))
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

    }
}

@Composable
private fun JoyerRow(
    modifier: Modifier,
    joyer: Joyer
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
                    text = joyer.name,
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    color = LightBlack,
                    fontFamily = fontFamilyLato,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.width(2.dp))
                repeat(joyer.starsCount) {
                    Spacer(modifier = Modifier.width(3.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_star_golden),
                        contentDescription = "Star",
                        modifier = Modifier
                            .size(14.dp, 13.dp)
                    )
                }

                if (joyer.isLockVisible) {

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
            }

            Spacer(Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = joyer.tag,
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