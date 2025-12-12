package com.joyersapp.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleHeader(
    title: String?,
    subtitle: String?,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                if (title != null) Text(title)
                if (subtitle != null) Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(id = android.R.drawable.ic_media_previous),
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painterResource(id = android.R.drawable.ic_menu_more),
                    contentDescription = "menu"
                )
            }
        }
    )
}