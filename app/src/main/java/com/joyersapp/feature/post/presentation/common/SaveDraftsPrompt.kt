package com.joyersapp.feature.post.presentation.common

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.joyersapp.theme.DividerColor30
import com.joyersapp.theme.Golden
import com.joyersapp.theme.LightBlack
import com.joyersapp.theme.Red
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import java.io.File
import kotlin.math.min

@Composable
fun SaveDraftsPrompt(
    show: Boolean,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
) {

    if (show) {
        Dialog(
            onDismissRequest = onCancel,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBlack.copy(alpha = 0.3f))
//                    .noRippleClickable { onCancel() }
                    .systemBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(25.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Save this Joy as a draft?",
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = LightBlack
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = DividerColor30)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSave()
                                }
                                .padding(top = 22.dp, bottom = 23.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Save Draft",
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = Golden
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = DividerColor30)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDelete()
                                }
                                .padding(top = 21.dp, bottom = 25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Delete",
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = Red
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Cancel card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .clickable { onCancel() }
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