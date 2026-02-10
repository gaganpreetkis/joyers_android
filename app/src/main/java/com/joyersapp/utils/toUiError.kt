package com.joyersapp.utils

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUiError(): UiError {
    return when (this) {
        is UnknownHostException, is ConnectException -> UiError.NetworkConnection
        is SocketTimeoutException -> UiError.Timeout
        is HttpException -> {
            // You can parse your error body here if needed
            UiError.ServerError("Server error: ${this.code()}")
        }
        is ApiErrorException -> UiError.ServerError(this.message ?: "API Error")
        else -> UiError.Unknown(this.localizedMessage)
    }
}

sealed class UiError {
    data object NetworkConnection : UiError()
    data object Timeout : UiError()
    data class ServerError(val message: String) : UiError()
    data class Unknown(val message: String?) : UiError()
}