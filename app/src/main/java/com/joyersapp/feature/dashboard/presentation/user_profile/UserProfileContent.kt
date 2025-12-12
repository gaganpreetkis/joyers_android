package com.joyersapp.feature.dashboard.presentation.user_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.joyersapp.R

// ---- Profile Content ----
@Composable
fun UserProfileContent(
    state: UserProfileUiState,
    onEditProfile: () -> Unit,
    onMessage: () -> Unit,
    onNotify: () -> Unit,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    // main scrollable column content (scrolls under fixed header)
    Column(modifier = modifier.fillMaxSize()) {
        // Banner + Avatar stack
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)) {

            if (!state.bannerUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.bannerUrl)
                        .crossfade(true).build(),
                    contentDescription = "banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFD0942F))) // gold placeholder
            }

            // Avatar overlay with double ring
            val avatarSize = 110.dp
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .align(Alignment.BottomStart)
                    .offset(x = 20.dp, y = (avatarSize / 2) * -1)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // outer ring
                Box(modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(Color(0xFFF6E9DC))) {
                    // inner white ring
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White)
                    ) {
                        // actual avatar
                        if (!state.avatarUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(state.avatarUrl)
                                    .crossfade(true).build(),
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back_arrow_golden), // replace
                                contentDescription = "avatar",
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }

                // small circular action (refresh/edit) at bottom-right of avatar
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = 8.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_reload), // replace
                        contentDescription = "action",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Spacer to account for overlapping avatar
        Spacer(modifier = Modifier.height(56.dp))

        // Name, subtitle, location, action button and stats
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = state.displayName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = state.title, fontSize = 14.sp, color = Color(0xFFCF9A3B), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = state.location, fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(painter = painterResource(id = com.hbb20.R.drawable.flag_united_states_of_america), contentDescription = "flag", modifier = Modifier.size(18.dp))
                    }
                }

                // Right side quick icons column (message, bell, bookmark)
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(onClick = onMessage, modifier = Modifier.size(44.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_back_arrow_golden), contentDescription = "msg", modifier = Modifier.size(22.dp))
                    }
                    IconButton(onClick = onNotify, modifier = Modifier.size(44.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_back_arrow_golden), contentDescription = "notify", modifier = Modifier.size(22.dp))
                    }
                    IconButton(onClick = onBookmark, modifier = Modifier.size(44.dp)) {
                        Image(painter = painterResource(id = R.drawable.ic_back_arrow_golden), contentDescription = "bookmark", modifier = Modifier.size(22.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stats row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Likes", color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.likes, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Following", color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.following, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Followers", color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = state.followers, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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