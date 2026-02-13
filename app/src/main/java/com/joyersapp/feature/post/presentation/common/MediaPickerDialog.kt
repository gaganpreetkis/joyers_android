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
import androidx.core.content.FileProvider
import com.joyersapp.theme.DividerColor30
import com.joyersapp.theme.LightBlack
import com.joyersapp.utils.fontFamilyLato
import com.joyersapp.utils.noRippleClickable
import java.io.File
import kotlin.math.min

@Composable
fun MediaPickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onMediaPicked: (List<Uri>) -> Unit,
) {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraVideoUri by remember { mutableStateOf<Uri?>(null) }
    val selectedUris = remember { mutableListOf<Uri>() }
    val mediaSelectionLimit = 5
    var isVideo = false

    val cameraImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { onMediaPicked(listOf(it)) }
        onDismiss()
    }

    val cameraVideoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success) cameraVideoUri?.let { onMediaPicked(listOf(it)) }
        onDismiss()
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        if (uris.isNotEmpty()) {
            onMediaPicked(uris)
        }
    }

    fun launchCamera(isVideo: Boolean) {

        if (isVideo) {
            val uri = createVideoUri(context)
            cameraVideoUri = uri
            cameraVideoPicker.launch(uri)
        } else {
            val uri = createImageUri(context)
            cameraImageUri = uri
            cameraImagePicker.launch(uri)
        }
    }


    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera(isVideo)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    fun checkPermissionAndLaunchCamera(isVideo: Boolean) {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            launchCamera(isVideo)
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun openLegacyMediaPicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageAndVideo
        ))
    }

    if (show) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightBlack.copy(alpha = 0.3f))
                    .noRippleClickable { onDismiss() }
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
                                .clickable {
                                    openLegacyMediaPicker()
                                }
                                .padding(top = 21.5.dp, bottom = 25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Choose Picture",
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
                                    isVideo = false
                                    checkPermissionAndLaunchCamera(false)
                                }
                                .padding(top = 21.5.dp, bottom = 25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Take Picture",
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
                                    isVideo = true
                                    checkPermissionAndLaunchCamera(true)
                                }
                                .padding(top = 21.5.dp, bottom = 25.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Take Video",
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = fontFamilyLato,
                                color = LightBlack
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Cancel card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .clickable { onDismiss() }
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

private fun createImageUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "Joyers_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues,
    )!!
}

fun createVideoUri(context: Context): Uri {
    val videoFile = File(context.cacheDir, "Joyers_video_${System.currentTimeMillis()}.mp4")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider", // Must match Manifest
        videoFile
    )
}