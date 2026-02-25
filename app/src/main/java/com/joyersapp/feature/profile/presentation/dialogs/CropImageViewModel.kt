package com.joyersapp.feature.profile.presentation.dialogs

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.div


data class CropUiState(
    val imageUri: Uri? = null,
    val imageBitmap: ImageBitmap? = null,
    val scale: Float = 1f,
    val offset: Offset = Offset.Zero,
    val isLoading: Boolean = false,
    val croppedUri: Uri? = null,
    val croppedPath: String? = null
)

sealed class CropNavigationEvent {
    data object Back : CropNavigationEvent()
    data class OnCropped(val uri: Uri, val scale: Float, val offset: Offset) : CropNavigationEvent()
}

@HiltViewModel
class CropImageViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private val _uiState = MutableStateFlow(CropUiState())
    val uiState: StateFlow<CropUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<CropNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private var circleRadiusPx: Float = 0f

    fun setCircleRadius(radius: Float) {
        circleRadiusPx = radius
    }

    fun loadImage(uri: Uri, initialScale: Float = 1f, initialOffset: Offset) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            val bitmap = decodeAndRotate(uri)

            bitmap?.let {
                val imageBitmap = it.asImageBitmap()

                _uiState.update { state ->
                    state.copy(
                        imageUri = uri,
                        imageBitmap = imageBitmap,
                        isLoading = false
                    )
                }

                calculateInitialScale(imageBitmap, initialScale, initialOffset)
            }
        }
    }

    private fun decodeAndRotate(uri: Uri): Bitmap? {
        return try {
            val resolver = context.contentResolver

            val orientation = resolver.openInputStream(uri)?.use {
                val exif = ExifInterface(it)
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
            } ?: ExifInterface.ORIENTATION_NORMAL

            val bitmap = resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            } ?: return null

            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }

            if (orientation != ExifInterface.ORIENTATION_NORMAL) {
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else bitmap

        } catch (e: Exception) {
            null
        }
    }

    private fun calculateInitialScale(bitmap: ImageBitmap, initialScale: Float = 1f, initialOffset: Offset) {
        val minScaleX = (circleRadiusPx * 2) / bitmap.width
        val minScaleY = (circleRadiusPx * 2) / bitmap.height
        val minScale = maxOf(minScaleX, minScaleY)
        val newScale = (initialScale).coerceAtLeast(minScale)

        // 3. Clamp Offset to keep image covering the circle
        val maxOffsetHorizontal = (bitmap.width * initialScale - circleRadiusPx * 2) / 2
        val maxOffsetVertical = (bitmap.height * newScale - circleRadiusPx * 2) / 2

        _uiState.update {
            it.copy(
                scale = newScale,
                offset = Offset(
                    x = initialOffset.x.coerceIn(-maxOffsetHorizontal, maxOffsetHorizontal),
                    y = initialOffset.y.coerceIn(-maxOffsetVertical, maxOffsetVertical)
                )
                )
        }
    }

    fun updateTransform(pan: Offset, zoom: Float, circleDiameterPx: Float) {
        val state = _uiState.value
        state.imageBitmap?.let { bitmap ->
            // 1. Update Scale (with a minimum floor)
            val minScale = maxOf(circleDiameterPx / bitmap.width, circleDiameterPx / bitmap.height)
            val newScale = (state.scale * zoom).coerceAtLeast(minScale)

            // 2. Calculate New Offset
            val newOffset = state.offset + pan

            // 3. Clamp Offset to keep image covering the circle
            val maxOffsetHorizontal = (bitmap.width * state.scale - circleDiameterPx) / 2
            val maxOffsetVertical = (bitmap.height * state.scale - circleDiameterPx) / 2

            _uiState.update { state ->
                state.copy(
                    scale = newScale.coerceIn(0f, 5f),
                    offset = Offset(
                        x = newOffset.x.coerceIn(-maxOffsetHorizontal, maxOffsetHorizontal),
                        y = newOffset.y.coerceIn(-maxOffsetVertical, maxOffsetVertical)
                    )
                )
            }
        }
    }

    fun cropAndSave() {
        val state = _uiState.value
        val image = state.imageBitmap ?: return

        viewModelScope.launch(Dispatchers.IO) {
            val croppedBitmap = getCircularCroppedBitmap(
                source = image,
                offset = state.offset,
                scale = state.scale,
                circleRadiusPx = circleRadiusPx
            )

            val file = File(
                context.cacheDir,
                "cropped_${System.currentTimeMillis()}.png"
            )

            FileOutputStream(file).use {
                croppedBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            val fileUri = Uri.fromFile(file)

            _uiState.update {
                it.copy(
                    croppedUri = fileUri,
                    croppedPath = file.path
                )
            }
            _navigationEvent.send(CropNavigationEvent.OnCropped(
                fileUri,
                state.scale,
                state.offset
            ))
        }
    }
}

private fun getCircularCroppedBitmap(
    source: ImageBitmap,
    offset: Offset,
    scale: Float,
    circleRadiusPx: Float
): Bitmap {
    val androidBitmap = source.asAndroidBitmap()
    val diameter = (circleRadiusPx * 2).toInt()

    //  Create a blank square bitmap (the size of your circle)
    val output = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(output)
    val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)

    //  Draw the image with the SAME transformations used in the UI
    val matrix = android.graphics.Matrix()
    matrix.postTranslate(-source.width / 2f, -source.height / 2f) // Center image
    matrix.postScale(scale, scale) // Apply zoom
    matrix.postTranslate(circleRadiusPx + offset.x, circleRadiusPx + offset.y) // Apply pan

    canvas.drawBitmap(androidBitmap, matrix, paint)

    return output
}