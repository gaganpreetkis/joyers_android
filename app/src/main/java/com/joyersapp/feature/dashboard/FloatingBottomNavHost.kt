package com.joyersapp.feature.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joyersapp.R
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.GrayBorder
import com.joyersapp.theme.LightBlack

@Composable
fun FloatingBottomNavHost(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    selectedTab: BottomTab,
    onBottomTabSelected: (BottomTab) -> Unit = {},
    content: @Composable (innerPadding: PaddingValues) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = topBar,
            bottomBar = {}
        ) { innerPadding ->

            content(innerPadding)
        }
        BottomNavBar(
            modifier = modifier.align(Alignment.BottomCenter),
            selected = selectedTab,
            onTabSelected = onBottomTabSelected
        )

    }
}

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    selected: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    val bottomLift = 20.dp
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)// narrower than full width for "floating" card look
            .padding(bottom = bottomLift)                 // lift above parent bottom
            .navigationBarsPadding(), shape = RectangleShape,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.White)
                .border(1.dp, GrayBorder),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomTab.entries.forEach { tab ->
                // Replace with your Nav icons
                Image(
                    painter = painterResource(tab.selectedIcon),
                    contentDescription = "Toggle Password",
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onTabSelected(tab)
                        },
                    colorFilter = ColorFilter.tint(if (tab == selected) Golden60 else LightBlack)
                )
            }
        }
    }
}


enum class BottomTab(val label: String, val selectedIcon: Int, val unselectedIcon: Int) {
    HOME("Home", R.drawable.ic_nav_joyers_home, R.drawable.ic_cancel_grey),
    FRIENDS("Search", R.drawable.ic_nav_joyers_friends, R.drawable.ic_cancel_grey),
    POST("Add", R.drawable.ic_nav_joyers_add, R.drawable.ic_cancel_grey),
    CONTACTS("Chat", R.drawable.ic_nav_joyers_contact, R.drawable.ic_cancel_grey),
    NOTIFICATIONS("Profile", R.drawable.ic_nav_joyers_notifications, R.drawable.ic_cancel_grey)
}