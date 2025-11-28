package com.joyersapp.api.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckUserNameRequest(
    @SerializedName("username" ) var username : String? = null
) : Serializable