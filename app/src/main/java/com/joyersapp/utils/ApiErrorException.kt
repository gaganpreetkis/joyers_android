package com.joyersapp.utils

import com.joyersapp.auth.data.remote.dto.ApiErrorDto

class ApiErrorException(
    val errorBody: ApiErrorDto?,        // can be CheckUsernameErrorDto or any
    message: String
) : Exception(message)