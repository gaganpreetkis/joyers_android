package com.joyersapp.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.theme.Golden
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.dismissKeyboardOnScroll
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.tapToDismissKeyboard


@Preview
@Composable
fun BaseDialog(
    onDismiss: () -> Unit = {},
    titles: List<String> = arrayListOf("",),
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    dialogContent: @Composable (dialogModifier: Modifier, dialogFocusManager: FocusManager, maxHeight: Dp) -> Unit = { dialogModifier, dialogFocusManager, maxHeight -> }
) {

    val context = LocalContext.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val goldenColor = Golden
    val lightBlackColor = LightBlack

//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(White)
//    ) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val dialogFocusManager = LocalFocusManager.current
        val dialogModifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    dialogFocusManager.clearFocus()
                }
            }

        val configuration = LocalWindowInfo.current.containerSize
        // Calculate maximum height: screen height - 100.dp (50.dp top + 50.dp bottom)
        val minHeight = 275.dp
        val maxHeight = remember(configuration) {
            configuration.height.dp - 100.dp
        }

        // Determine the height modifier dynamically
        val dialogHeightModifier = if (isKeyBoardOpen) {
            // When keyboard is visible, the parent Column will resize to full height
            Modifier
                .height(maxHeight)
                .padding(top = 50.dp)
        } else {
            // When keyboard is hidden, use a standard dialog height constraint
            Modifier
                .wrapContentHeight()
                .heightIn(min = minHeight, max = maxHeight)
                .padding(top = 50.dp, bottom = 50.dp)
        }

        Card(
            modifier = dialogModifier

                .windowInsetsPadding(WindowInsets.systemBars)
                .then(dialogHeightModifier) // Apply dynamic height
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White) // Ensure background captures taps
                .dismissKeyboardOnScroll()
                .tapToDismissKeyboard(), shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            // Header
            Row(
                modifier = dialogModifier
                    .fillMaxWidth()
                    .padding(top = 16.7.dp, start = 18.dp, end = 23.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Back button (only visible in subtitle mode)
                if (showBackButton) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow_golden),
                        contentDescription = null,
                        modifier = dialogModifier
                            .size(20.dp, 15.dp)
                            .noRippleClickable { onBack() }
                    )
                } else {
                    Spacer(modifier = dialogModifier.size(20.dp, 15.dp))
                }

                // Title or Second Title
                if (titles.size == 1) {
                    Text(
                        text = titles[0],
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = fontFamilyLato,
                        color = lightBlackColor,
                        modifier = dialogModifier.padding(top = 0.dp)
                    )
                } else {
                    FlowRow(
                        modifier = dialogModifier.padding(top = 2.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        titles.forEachIndexed { index, item ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item,
                                    fontSize = 16.sp,
                                    lineHeight = if (index == 0) 19.sp else 22.sp,
                                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = fontFamilyLato,
                                    color = lightBlackColor,
                                    modifier = dialogModifier
                                )
                                if (index < titles.size - 1) {
                                    Spacer(modifier = dialogModifier.width(11.dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_forward_black),
                                        contentDescription = null,
                                        modifier = dialogModifier.size(6.dp, 10.dp)
                                    )
                                    Spacer(modifier = dialogModifier.width(10.dp))
                                }
                            }
                        }

                    }
                }

                // Close button
                Image(
                    painter = painterResource(id = R.drawable.ic_cross_golden),
                    contentDescription = null,
                    modifier = dialogModifier
                        .width(15.51.dp)
                        .noRippleClickable { onDismiss() }
                )
            }
            dialogContent(dialogModifier, dialogFocusManager, maxHeight)
        }
//        }
    }
}