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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import com.joyersapp.R
import com.joyersapp.theme.White

@Composable
fun CenteredHeader(
    username: String,
    lockBadgeCount: Int? = null,
    onBack: () -> Unit = {},
    onMenu: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val sideIconWidth = 56.dp

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White,
        tonalElevation = 0.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
            ) {

                /** ---------- LEFT: Back Button Image ---------- */
                Box(
                    modifier = Modifier
                        .width(sideIconWidth)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back_arrow_golden),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(26.dp)
                            .clickable { onBack() }
                    )
                }

                /** ---------- CENTER: username + lock + badge + arrow ---------- */
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    /** Lock Image + Badge */
                    Box(contentAlignment = Alignment.TopEnd) {
                        Image(
                            painter = painterResource(R.drawable.ic_back_arrow_golden),
                            contentDescription = "Private",
                            modifier = Modifier.size(18.dp)
                        )

                        /** Small Badge */
                        if (lockBadgeCount != null && lockBadgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .offset(x = (-6).dp, y = 6.dp)
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3B30)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lockBadgeCount.toString(),
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(6.dp))

                    /** Username + Chevron */
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = username,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Image(
                            painter = painterResource(com.joyersapp.R.drawable.ic_back_arrow_golden),
                            contentDescription = "Dropdown",
                            modifier = Modifier
                                .size(20.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }

                /** ---------- RIGHT: Menu Image ---------- */
                Box(
                    modifier = Modifier
                        .width(sideIconWidth)
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back_arrow_golden),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onMenu() }
                    )
                }
            }

            /** Bottom Divider */
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE6E6E6))
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
private fun PreviewCenteredHeader() {
    CenteredHeader(
        username = "Sara_99",
        lockBadgeCount = 7,
        onBack = {},
        onMenu = {}
    )
}