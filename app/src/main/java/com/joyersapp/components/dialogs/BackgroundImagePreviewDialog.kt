package com.joyersapp.components.dialogs

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.joyersapp.R
import com.joyersapp.theme.Black
import com.joyersapp.theme.DividerColor30
import com.joyersapp.theme.Gray20
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.Red
import com.joyersapp.theme.White
import com.joyersapp.utils.fontFamilyLato

@Composable
fun BackgroundImagePreviewDialog(
    showDialog: Boolean,
    isImageCropped: Boolean,
    imageUri: Uri?,
    imagePath: String?,
    onDismiss: () -> Unit,
    onChangePicture: () -> Unit,
    onDelete: () -> Unit,
    onCrop: () -> Unit,
    onDone: () -> Unit
) {
    if (showDialog) {
        var showDeleteConfirm by remember { mutableStateOf(false) }

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Black)
                    .systemBarsPadding()
            ) {
                // Close icon - top left
                Image(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "Close",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 25.dp, end = 20.dp)
                        .clickable { onDismiss() }
                )

                // More options icon - top right
               /* Image(
                    painter = painterResource(id = R.drawable.ic_menu_dots_horizontal_white),
                    contentDescription = "More options",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 29.dp, end = 20.dp)
                        .clickable {
                            // TODO: Implement more options menu if needed
                        }
                )*/

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Background image - centered horizontally, 23px up from center
                    val configuration = LocalConfiguration.current
                    val density = LocalDensity.current
                    val screenWidth = with(density) { configuration.screenWidthDp.dp }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .offset(y = (-23).dp) // 23px up from center
                                    .clip(RoundedCornerShape(0.dp))
                                    .background(Gray20)
                            ) {
                                if (imageUri != null) {
                                    // Use imagePath if available (for local files), otherwise use imageUri
                                    val imageModel = imagePath ?: imageUri
                                    AsyncImage(
                                        model = imageModel,
                                        contentDescription = "Background image preview",
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_nav_joyers_home),
                                        contentDescription = "Upload placeholder",
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }


                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 0.dp)
                    ) {

                        // Change Picture button - 50px above bottom row
                        Box(
                            modifier = Modifier
                                .width(148.dp)
                                .height(35.dp)
                                .border(1.dp, White, RoundedCornerShape(50))
                                .clickable { onChangePicture() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (imageUri == null) "Upload Picture" else "Change Picture",
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = White,
                                modifier = Modifier.offset(y = -1.dp)
                            )
                        }

                        Spacer(Modifier.height(50.dp))

                        // Bottom row: show only when an image exists
                        if (imageUri != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .padding(top = 0.dp, bottom = 0.dp, start = 30.dp, end = 30.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                // Delete button - 30px from left
                                Box(
                                    modifier = Modifier
                                        .width(87.dp)
                                        .height(35.dp)
                                        .border(1.dp, White, RoundedCornerShape(50))
                                        .clickable { showDeleteConfirm = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Delete",
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyLato,
                                        color = White,
                                        modifier = Modifier.offset(y = -1.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                // Crop button - centered between Delete and Done
                                Box(
                                    modifier = Modifier
                                        .width(87.dp)
                                        .height(35.dp)
                                        .border(1.dp, White, RoundedCornerShape(50))
                                        .clickable { onCrop() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Crop",
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyLato,
                                        color = White,
                                        modifier = Modifier.offset(y = -1.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                // Done button - 30px from right
                                Box(
                                    modifier = Modifier
                                        .width(87.dp)
                                        .height(35.dp)
                                        .border(1.dp, White, RoundedCornerShape(50))
                                        .clickable { onDone() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isImageCropped) "Save" else "Done",
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyLato,
                                        color = White,
                                        modifier = Modifier.offset(y = -1.dp)
                                    )
                                }
                            }
                        } else Spacer(Modifier.height(35.dp))

                        Spacer(Modifier.height(60.dp))

                    }
                }

            }
        }

        if (showDeleteConfirm) {
            Dialog(
                onDismissRequest = { showDeleteConfirm = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    decorFitsSystemWindows = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(LightBlack.copy(alpha = 0.3f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { showDeleteConfirm = false }
                        .systemBarsPadding(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(horizontal = 15.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { /* consume taps inside */ },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Combined title + delete card with divider
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(25.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Delete background image?",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = fontFamilyLato,
                                    color = LightBlack,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 20.dp, bottom = 20.5.dp)
                                )

                                HorizontalDivider(thickness = 1.dp, color = DividerColor30)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showDeleteConfirm = false
                                            onDelete()
                                        }
                                        .padding(top = 21.5.dp, bottom = 25.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Delete",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = fontFamilyLato,
                                        color = Red
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(15.dp))

                        // Cancel card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(20.dp))
                                .clickable { showDeleteConfirm = false }
                                .padding(top = 22.dp, bottom = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = LightBlack
                            )
                        }
                    }
                }
            }
        }
    }
}

