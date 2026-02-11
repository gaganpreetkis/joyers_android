package com.joyersapp.feature.post.presentation.create_post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joyersapp.R
import com.joyersapp.components.layouts.HardBlockingLoader
import com.joyersapp.feature.post.domain.model.Joyer
import com.joyersapp.feature.post.presentation.common.CameraFab
import com.joyersapp.feature.post.presentation.common.CreateJoyHeader
import com.joyersapp.feature.post.presentation.common.JoyerRow
import com.joyersapp.feature.post.presentation.common.JoyersCup
import com.joyersapp.theme.Golden
import com.joyersapp.theme.GrayOuterBorder
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

@Preview
@Composable
private fun ScreenPreview() {
    CreatePostScafold(
        CreatePostUiState(

        )
    )
}

@Composable
fun CreatePostScreen(
    viewModel: CreatePostViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()



    HardBlockingLoader(state.isLoading)

    CreatePostScafold(state)

//    }
}

@Composable
private fun CreatePostScafold(state: CreatePostUiState) {

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

            Row() {
                JoyerRow(
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                        .height(57.dp)
                        .padding(start = 15.dp),
                    Joyer(
                        id = "1",
                        name = "James Spiegel James Spie Jame Spiegel James Spie James Spie",
                        username = "james",
                        profilePicture = "",
                        tag = "Engineer",
                        starsCount = 1,
                        isLockVisible = true
                    ))

                Text(
                    modifier = Modifier
                        .padding(top = 11.dp, start = 15.dp, end = 18.dp),
                    text = "300",
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    color = Golden,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamilyLato,
                    maxLines = 1
                )
            }

        }

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