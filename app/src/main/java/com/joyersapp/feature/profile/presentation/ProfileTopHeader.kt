package com.joyersapp.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

//@Preview
@Composable
fun ProfileTopHeader(
    modifier: Modifier = Modifier,
    state: UserProfileUiState,
    onBack: () -> Unit = {},
    onMenu: () -> Unit = {},
) {
    val sideWidth = 56.dp
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(63.dp),
        color = White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            // left back image
            Box(
                modifier = Modifier
                    .width(sideWidth)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_golden),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(20.dp, 17.dp)
                )
            }

            // center username block
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // lock
                Box(contentAlignment = Alignment.TopEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_lock_heart_black), // replace
                        contentDescription = "Lock",
                        modifier = Modifier.size(13.39.dp, 20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(7.01.dp))

                // Username
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.username,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(7.dp))
                    Image(
                        painter = painterResource(id = R.drawable.arrowdown_lite),
                        contentDescription = "Dropdown",
                        modifier = Modifier.size(14.dp, 8.dp)
                    )
                }
            }

            // right menu
            Box(
                modifier = Modifier
                    .width(sideWidth)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_menu_dots_horizontal),
                    contentDescription = "Menu",
                    modifier = Modifier
                        .size(18.dp, 4.dp)
                        .clickable { onMenu() }
                )
            }
        }
    }
}