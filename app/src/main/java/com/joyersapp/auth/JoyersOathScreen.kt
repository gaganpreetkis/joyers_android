package com.joyersapp.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joyersapp.common_widgets.AutoSizingHtmlText
import com.joyersapp.theme.Golden60
import com.joyersapp.utils.parseHtmlString
import com.joyersapp.R

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun JoyersOathScreen(
    onFabClick: () -> Unit = {},
) {
    val context = LocalContext.current

    // Parse HTML strings - parse fresh each time to ensure it works
    val title1 = parseHtmlString(context.getString(R.string.joyer_auth_1_line_1))
    val title2 = parseHtmlString(context.getString(R.string.joyer_auth_1_line_2))
    val title3 = parseHtmlString(context.getString(R.string.joyer_auth_1_line_3))
    val title4 = parseHtmlString(context.getString(R.string.joyer_auth_1_line_4))
    val title5 = parseHtmlString(context.getString(R.string.joyer_auth_1_line_5))
    val title6 = parseHtmlString(context.getString(R.string.joyer_auth_2_line_1))
    val title7 = parseHtmlString(context.getString(R.string.joyer_auth_2_line_2))
    val title8 = parseHtmlString(context.getString(R.string.joyer_auth_3_line_1))
    val title9 = parseHtmlString(context.getString(R.string.joyer_auth_4_line_1))
    val title10 = parseHtmlString(context.getString(R.string.joyer_auth_4_line_2))

    // State for auto-sizing text - collect all text sizes and apply minimum to all
    val textSizes = remember { mutableStateOf<MutableMap<Int, Float>>(mutableMapOf()) }
    var minTextSize by remember { mutableStateOf<Float?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .imePadding()
        ) {
            // Logo Image - centered with margins 79dp left/right, 50dp top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 79.dp, top = 50.dp, end = 79.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.joyer_auth),
                    contentDescription = "Joyer Auth",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }

            // Content Box with light grey background - margins 20dp left/right, 50dp top
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 50.dp, end = 20.dp)
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.color_border_light),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .background(
                        color = colorResource(id = R.color.color_light_grey_bg),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(15.dp)
            ) {
                // First section (5 lines)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AutoSizingHtmlText(
                        annotatedString = title1,
                        modifier = Modifier.padding(vertical = 2.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[1] = it },
                        key = 1
                    )
                    AutoSizingHtmlText(
                        annotatedString = title2,
                        modifier = Modifier.padding(vertical = 2.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[2] = it },
                        key = 2
                    )
                    AutoSizingHtmlText(
                        annotatedString = title3,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[3] = it },
                        key = 3
                    )
                    AutoSizingHtmlText(
                        annotatedString = title4,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[4] = it },
                        key = 4
                    )
                    AutoSizingHtmlText(
                        annotatedString = title5,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[5] = it },
                        key = 5
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Second section (2 lines)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AutoSizingHtmlText(
                        annotatedString = title6,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[6] = it },
                        key = 6
                    )
                    AutoSizingHtmlText(
                        annotatedString = title7,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[7] = it },
                        key = 7
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Third section (1 line)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AutoSizingHtmlText(
                        annotatedString = title8,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[8] = it },
                        key = 8
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Fourth section (2 lines)
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AutoSizingHtmlText(
                        annotatedString = title9,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[9] = it },
                        key = 9
                    )
                    AutoSizingHtmlText(
                        annotatedString = title10,
                        modifier = Modifier.padding(vertical = 3.dp),
                        minTextSize = minTextSize,
                        onTextSizeMeasured = { textSizes.value[10] = it },
                        key = 10
                    )
                }
            }

            // FAB Button - positioned at bottom end with marginTop 50dp, marginEnd 30dp
            Spacer(modifier = Modifier.height(50.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Golden60)
                        .clickable(onClick = { onFabClick() }),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_forward_arrow_white),
                        contentDescription = stringResource(id = R.string.floating_action_button),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Bottom spacing - 15dp as per XML
            Spacer(modifier = Modifier.height(15.dp))
        }

        // Calculate minimum text size after all texts are measured
        LaunchedEffect(textSizes.value.size) {
            if (textSizes.value.size == 10) {
                val minSize = textSizes.value.values.minOrNull()
                if (minSize != null && minSize > 0f) {
                    minTextSize = minSize
                }
            }
        }
    }
}
