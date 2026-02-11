package com.joyersapp.feature.post.presentation.create_joy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.feature.post.domain.model.Joyer
import com.joyersapp.feature.post.presentation.common.CreateJoyHeader
import com.joyersapp.feature.post.presentation.common.JoyerRow
import com.joyersapp.feature.post.presentation.common.JoyersCup
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.LightBlack35
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

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