package com.joyersapp.common_widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.joyersapp.theme.Black
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato

@Composable
fun AutoResizeText(
    text: String,
    modifier: Modifier = Modifier,
    maxTextSize: TextUnit = 16.sp,
    minTextSize: TextUnit = 12.sp,
    stepSize: Float = 1f,
    color: Color = LightBlack,
    fontFamily: FontFamily = fontFamilyLato,
    fontWeight: FontWeight = FontWeight.Normal
) {
    var textSize by remember { mutableStateOf(maxTextSize) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        fontSize = textSize,
        maxLines = 1,
        overflow = TextOverflow.Clip,
        color = color,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        softWrap = false,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        onTextLayout = { result ->
            if (!readyToDraw) {
                if (result.didOverflowWidth && textSize > minTextSize) {
                    textSize = (textSize.value - stepSize).sp
                } else {
                    readyToDraw = true
                }
            }
        }
    )
}
