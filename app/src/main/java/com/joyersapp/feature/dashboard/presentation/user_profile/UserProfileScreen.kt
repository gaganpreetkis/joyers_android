package com.joyersapp.feature.dashboard.presentation.user_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileScreen(

) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp)    // <= this is vital: reserves space under the bottomBar
    ) {
        item { /* header / banner */ }
        items(50) { index -> /* content items */
            Box() {
                Text(
                    text = "User Profile"
                )
            }
        }
        // no manual spacer required here to avoid gap
    }
}