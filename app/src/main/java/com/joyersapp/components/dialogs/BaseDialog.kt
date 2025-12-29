package com.joyersapp.components.dialogs

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.common_widgets.AppBasicTextField
import com.joyersapp.common_widgets.DashedLine
import com.joyersapp.theme.Golden60
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.Gray40
import com.joyersapp.theme.GrayLightBorder
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.dismissKeyboardOnScroll
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import com.joyersapp.utils.rememberIsKeyboardOpen
import com.joyersapp.utils.tapToDismissKeyboard


//@Preview
@Composable
fun BaseDialog(
    onDismiss: () -> Unit,
    titles: List<String>,
    showBackButton: Boolean = false,
    onBack: () -> Unit = {},
    dialogContent: @Composable () -> Unit
) {

    val context = LocalContext.current
    val isKeyBoardOpen = rememberIsKeyboardOpen()

    val goldenColor = Golden60
    val lightBlackColor = LightBlack

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )) {
        val dialogFocusManager = LocalFocusManager.current
        val dialogModifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    dialogFocusManager.clearFocus()
                }
            }

        val configuration = LocalWindowInfo.current.containerSize
        // Calculate maximum height: screen height - 100.dp (50.dp top + 50.dp bottom)
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
                .heightIn(max = maxHeight)
                .wrapContentHeight()
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
                .tapToDismissKeyboard()
            ,shape = RoundedCornerShape(25.dp),
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
                            .noRippleClickable { onDismiss() }
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
                    LazyRow (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = dialogModifier.padding(top = 2.dp, bottom = 2.dp)
                    ) {
                        itemsIndexed(titles) { index, item ->
                            Text(
                                text = item,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
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

                // Close button
                Image(
                    painter = painterResource(id = R.drawable.ic_cross_golden),
                    contentDescription = null,
                    modifier = dialogModifier
                        .width(15.51.dp)
                        .noRippleClickable { onDismiss() }
                )
            }
            dialogContent()
        }
    }
}