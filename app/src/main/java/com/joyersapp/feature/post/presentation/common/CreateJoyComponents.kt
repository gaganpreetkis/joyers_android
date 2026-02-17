package com.joyersapp.feature.post.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joyersapp.R
import com.joyersapp.feature.post.domain.model.Joyer
import com.joyersapp.theme.AvatarBorder
import com.joyersapp.theme.Golden
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.LightBlack35
import com.joyersapp.theme.LightBlack55
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable

@Composable
fun CreateJoyHeader(
    modifier: Modifier,
    title: String,
    onBack: () -> Unit = {},
    onMenu: () -> Unit = {},
) {

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(start = 16.dp)
//                .align(Alignment.CenterStart)
                .noRippleClickable { onBack() },
            contentAlignment = Alignment.CenterEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_cross_golden),
                contentDescription = "Back",
                modifier = Modifier
                    .size(13.5.dp)
            )
        }

        // Title
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                maxLines = 1,
                color = LightBlack,
                overflow = TextOverflow.Ellipsis
            )
        }

        // right menu
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(end = 15.dp)
                .align(Alignment.CenterEnd)
                .noRippleClickable { onMenu() },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Drafts",
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = fontFamilyLato,
                maxLines = 1,
                color = Golden
            )
        }

    }
}



@Composable
fun JoyersCup(modifier: Modifier) {
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_joyers_cup),
            contentDescription = "Joyers Cup",
            modifier = Modifier.size(49.dp)
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = "Joyers Cup",
            fontSize = 21.sp,
            lineHeight = 24.sp,
            color = Golden,
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamilyLato,
            maxLines = 1
        )
    }
}

@Composable
fun CameraFab(modifier: Modifier) {
    Box (
        modifier = modifier
            .clip(CircleShape)
            .background(White)
            .size(47.dp)
            .border(width = 1.5.dp, color = Golden, shape = CircleShape),
    ) {
        Image(
            painter = painterResource(id = R.drawable.camera_inside_color),
            contentDescription = "Joyers Cup",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 13.5.dp)
                .size(23.4.dp, 18.11.dp)
        )
    }
}