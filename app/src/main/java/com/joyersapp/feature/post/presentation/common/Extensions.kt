package com.joyersapp.feature.post.presentation.common

import android.content.Context
import android.net.Uri
import com.joyersapp.feature.post.presentation.create_post.MediaType

fun Context.getMediaType(uri: Uri): MediaType {
    val mimeType = contentResolver.getType(uri)
    return if (mimeType?.startsWith("video") == true) {
        MediaType.VIDEO
    } else {
        MediaType.IMAGE
    }
}