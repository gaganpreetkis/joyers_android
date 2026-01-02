package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UploadPictureServerResponse (

    @SerializedName("statusCode" ) var statusCode : Int?    = null,
    @SerializedName("message"    ) var message    : String? = null,
    @SerializedName("data"       ) var data       : Data?   = Data()

)

data class Data (

    @SerializedName("profile_picture"    ) var profilePicture    : String? = null,
    @SerializedName("background_picture" ) var backgroundPicture : String? = null

)