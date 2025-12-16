package com.joyersapp.feature.dashboard.presentation.user_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray30
import com.joyersapp.theme.GrayBorder
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack60
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

// ---- Profile Content ----
@Preview
@Composable
fun UserProfileContent(
    state: UserProfileUiState = UserProfileUiState(),
    onEditProfile: () -> Unit = {},
    onMessage: () -> Unit = {},
    onNotify: () -> Unit = {},
    onBookmark: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // main scrollable column content (scrolls under fixed header)
    Column(modifier = modifier.fillMaxSize()) {

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
                        fontSize = 13.sp,
                        color = Color.Gray
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Likes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = LightBlack60,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.likes,
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
                    Spacer(modifier = Modifier.width(8.dp))
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
                    Spacer(modifier = Modifier.width(8.dp))
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 20.dp
                    )
            ) {
                // Action card (Edit Magnetics 80%)
                Card(
                    modifier = Modifier.wrapContentWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF333333))
                ) {
                    Text(
                        text = "Edit Magnetics   80%",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
                    )
                }
                // Right side quick icons column (message, bell, bookmark)
                Row() {
                    IconButton(onClick = onMessage, modifier = Modifier.size(44.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_mail_golden), contentDescription = "msg", modifier = Modifier.size(22.dp))
                    }
                    IconButton(onClick = onNotify, modifier = Modifier.size(44.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_notification_bell_golden), contentDescription = "notify", modifier = Modifier.size(22.dp))
                    }
                    IconButton(onClick = onBookmark, modifier = Modifier.size(44.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_back_arrow_golden), contentDescription = "bookmark", modifier = Modifier.size(22.dp))
                    }
                }
            }



            Spacer(modifier = Modifier.height(18.dp))

            // dashed divider (approx)
            Divider(thickness = 1.dp, color = Color(0xFFE6E6E6))

            Spacer(modifier = Modifier.height(18.dp))

            // Tabs row
            val tabs = listOf("Status", "Identity", "Sparks", "Cards", "Gallery")
            var selectedTab by remember { mutableStateOf(0) }
            ScrollableTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { idx, title ->
                    Tab(selected = selectedTab == idx, onClick = { selectedTab = idx }) {
                        Text(text = title, modifier = Modifier.padding(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab content (sample)
            when (selectedTab) {
                0 -> Column {
                    repeat(6) { i ->
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp), shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Status item #$i", fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Shared • 2h ago", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
                else -> Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                    Text("Content for ${tabs[selectedTab]}")
                }
            }

            Spacer(modifier = Modifier.height(88.dp)) // ensure space for floating bottom nav
        }
    }
}