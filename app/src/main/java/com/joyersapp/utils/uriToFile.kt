package com.joyersapp.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return tempFile
}
