package com.joyersapp.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun emptyPart(name: String): MultipartBody.Part {
    val emptyRequest = "".toRequestBody("*/*".toMediaType())
    return MultipartBody.Part.createFormData(name, "", emptyRequest)
}