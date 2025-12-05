package com.joyersapp.utils

import com.google.gson.Gson
import com.joyersapp.auth.data.remote.dto.ApiErrorDto
import retrofit2.HttpException

fun parseNetworkError(e: HttpException): String {
    return try {
        val errorJson = e.response()?.errorBody()?.string()

        if (errorJson.isNullOrEmpty()) {
            "Something went wrong"
        } else {
            // Using Gson:
            val dto = Gson().fromJson(errorJson, ApiErrorDto::class.java)
            dto.message
                ?: dto.error
                ?: "Something went wrong"
        }
    } catch (_: Exception) {
        "Something went wrong"
    }
}

fun getErrorDto(e: HttpException): ApiErrorDto {
    return try {
        val errorJson = e.response()?.errorBody()?.string()

        if (errorJson.isNullOrEmpty()) {
            ApiErrorDto(message = "Something went wrong")
        } else {
            // Using Gson:
            Gson().fromJson(errorJson, ApiErrorDto::class.java)
        }
    } catch (_: Exception) {
        ApiErrorDto(message = "Something went wrong")
    }
}