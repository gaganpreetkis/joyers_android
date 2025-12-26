package com.joyersapp.feature.home.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joyersapp.R
import com.joyersapp.components.layouts.JoyersHeader
import com.joyersapp.theme.GrayBG

@Composable
fun HomeScreen() {
    Scaffold() { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            JoyersHeader(
                onMenuClick = {

                }
            )
            // Avatar content
            Box(
                modifier = Modifier
                    .fillMaxSize()
//            .border(width = 3.dp, color = White, shape = CircleShape)
//            .padding(3.dp)
//            .border(width = 3.dp, color = Golden60, shape = CircleShape)
//            .padding(3.dp)
//            .border(width = 3.dp, color = White, shape = CircleShape)
//            .size(115.dp)
//            .clip(CircleShape)
                    .background(GrayBG),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nav_joyers_home), // your J icon
                    contentDescription = "avatar",
                    modifier = Modifier.size(250.dp)
                )
            }
        }
    }
}