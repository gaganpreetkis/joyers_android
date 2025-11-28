package com.joyersapp.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckUserNameModel(
    @SerializedName("available") var available: Boolean? = null,
    @SerializedName("suggestions") var suggestions: ArrayList<String> = arrayListOf(),
    @SerializedName("message") var message: String? = null
) : Serializable