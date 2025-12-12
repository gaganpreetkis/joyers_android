package com.joyersapp.feature.dashboard.presentation.user_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.theme.White


@Composable
fun ProfileTopHeader(
    username: String,
    badgeCount: Int?,
    onBack: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sideWidth = 56.dp
    Surface(modifier = modifier.fillMaxWidth(), color = White) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
            ) {
                // left back image
                Box(
                    modifier = Modifier
                        .width(sideWidth)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow_golden), // replace
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onBack() }
                    )
                }

                // center username block
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // lock + badge
                    Box(contentAlignment = Alignment.TopEnd) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back_arrow_golden), // replace
                            contentDescription = "Lock",
                            modifier = Modifier.size(18.dp)
                        )
                        if (badgeCount != null && badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(x = (-6).dp, y = 6.dp)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3B30)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeCount.toString(),
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = username,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(4.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_back_arrow_golden), // replace
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(20.dp)
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
                        painter = painterResource(id = R.drawable.ic_back_arrow_golden), // replace
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onMenu() }
                    )
                }
            }

            // divider
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE6E6E6)))
        }
    }
}