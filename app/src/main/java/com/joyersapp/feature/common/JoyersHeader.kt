package com.joyersapp.feature.common

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
import androidx.compose.ui.tooling.preview.Preview
import com.joyersapp.R
import com.joyersapp.theme.White

@Composable
fun JoyersHeader(
    onMenuClick: () -> Unit = {},
    onLampClick: () -> Unit = {}
) {
    val gold = Color(0xFFD59B32)   // Replace with your Golden60 if needed

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(63.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Left Menu Icon
        Image(
            painter = painterResource(id = R.drawable.ic_menu_golden),
            contentDescription = "Menu",
            modifier = Modifier
                .size(24.dp)
                .clickable { onMenuClick() },
        )

        // Center Logo (Joyer wordmark)
        Image(
            painter = painterResource(id = R.drawable.joyer_logo),
            contentDescription = "Joyers Logo",
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
                .height(28.dp),        // Adjust based on your logo
            alignment = Alignment.Center
        )

        // Right Lamp Icon
        Image(
            painter = painterResource(id = R.drawable.ic_lamp_golden),
            contentDescription = "Lamp",
            modifier = Modifier
                .size(28.dp)
                .clickable { onLampClick() }
        )
    }
}