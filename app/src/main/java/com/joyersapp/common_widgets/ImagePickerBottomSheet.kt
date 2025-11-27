package com.joyersapp.common_widgets

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.joyersapp.R
import com.joyersapp.theme.Black
import kotlin.math.min

@Composable
fun ImagePickerBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    allowMultipleSelection: Boolean = false,
    onImagesPicked: (List<Uri>) -> Unit,
    onCameraImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val selectedUris = remember { mutableListOf<Uri>() }
    val mediaSelectionLimit = 5

    val singlePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            onImagesPicked(listOf(it))
        }
        onDismiss()
    }

    val multiplePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val limitedUris = if (uris.size > 5) uris.take(5) else uris
            limitedUris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            onImagesPicked(limitedUris)
        }
        onDismiss()
    }

    val cameraPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { onCameraImagePicked(it) }
        onDismiss()
    }

    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            data?.let {
                selectedUris.clear()

                val clipData = it.clipData
                if (clipData != null) {
                    val count = min(clipData.itemCount, mediaSelectionLimit)
                    for (i in 0 until count) {
                        val uri: Uri = clipData.getItemAt(i).uri
                        selectedUris.add(uri)
                    }
                } else {
                    it.data?.let { uri ->
                        selectedUris.add(uri)
                    }
                }

                onImagesPicked(selectedUris)
                onDismiss()
            }
        }
    }

    fun launchCamera() {
        val uri = createImageUri(context)
        cameraImageUri = uri
        cameraPicker.launch(uri)
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun openLegacyMediaPicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
        }
        pickImagesLauncher.launch(Intent.createChooser(intent, "Select images and videos"))
    }

    if (showBottomSheet) {
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
                    .background(Color(0x80000000))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .clickable(enabled = false) {},
                ) {
                    // Title
                    Text(
                        text = context.getString(R.string.pick_image),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)

                    // Camera option
                    Text(
                        text = context.getString(R.string.take_photo_from_camera),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (androidx.core.content.ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    launchCamera()
                                } else {
                                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                            .padding(vertical = 20.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)

                    if (allowMultipleSelection) {
                        // Multiple selection option
                        Text(
                            text = context.getString(R.string.pick_multiple_images),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openLegacyMediaPicker() }
                                .padding(vertical = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    } else {
                        // Single selection option
                        Text(
                            text = context.getString(R.string.take_image_from_gallery),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openLegacyMediaPicker() }
                                .padding(vertical = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Cancel button
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 15.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = context.getString(R.string.cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDismiss() }
                                .padding(vertical = 23.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "camera_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues,
    )!!
}





@Composable
fun ImagePickerBottomSheetBack(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    allowMultipleSelection: Boolean = false,
    onImagesPicked: (List<Uri>) -> Unit,
    onCameraImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val selectedUris = remember { mutableListOf<Uri>() }
    val mediaSelectionLimit = 5

    val singlePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
            onImagesPicked(listOf(it))
        }
        onDismiss()
    }

    val multiplePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val limitedUris = if (uris.size > 5) uris.take(5) else uris
            limitedUris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            onImagesPicked(limitedUris)
        }
        onDismiss()
    }

    val cameraPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) cameraImageUri?.let { onCameraImagePicked(it) }
        onDismiss()
    }

    val pickImagesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            data?.let {
                selectedUris.clear()

                val clipData = it.clipData
                if (clipData != null) {
                    val count = min(clipData.itemCount, mediaSelectionLimit)
                    for (i in 0 until count) {
                        val uri: Uri = clipData.getItemAt(i).uri
                        selectedUris.add(uri)
                    }
                } else {
                    it.data?.let { uri ->
                        selectedUris.add(uri)
                    }
                }

                onImagesPicked(selectedUris)
                onDismiss()
            }
        }
    }

    fun launchCamera() {
        val uri = createImageUri(context)
        cameraImageUri = uri
        cameraPicker.launch(uri)
    }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun openLegacyMediaPicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
        }
        pickImagesLauncher.launch(Intent.createChooser(intent, "Select images and videos"))
    }

    if (showBottomSheet) {
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
                    .background(Color(0x80000000))
                    .clickable { onDismiss() },
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .clickable(enabled = false) {},
                ) {
                    // Title
                    Text(
                        text = context.getString(R.string.pick_image),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)

                    // Camera option
                    Text(
                        text = context.getString(R.string.take_photo_from_camera),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (androidx.core.content.ContextCompat.checkSelfPermission(
                                        context, Manifest.permission.CAMERA
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    launchCamera()
                                } else {
                                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                            .padding(vertical = 20.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)

                    if (allowMultipleSelection) {
                        // Multiple selection option
                        Text(
                            text = context.getString(R.string.pick_multiple_images),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openLegacyMediaPicker() }
                                .padding(vertical = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    } else {
                        // Single selection option
                        Text(
                            text = context.getString(R.string.take_image_from_gallery),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openLegacyMediaPicker() }
                                .padding(vertical = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Cancel button
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 15.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = context.getString(R.string.cancel),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDismiss() }
                                .padding(vertical = 23.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
