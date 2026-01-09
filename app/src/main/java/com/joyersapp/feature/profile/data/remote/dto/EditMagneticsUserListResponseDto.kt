package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EditMagneticsUserListResponseDto(

    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("data") var data: List<EditMagneticsUserListData> = arrayListOf(),
    @SerializedName("message") var message: String? = null

)


data class EditMagneticsUserListData(
    @SerializedName("id") var id: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("first_name") var first_name: String? = null,
    @SerializedName("last_name") var last_name: String? = null,
    @SerializedName("profile_picture") var profile_picture: String? = null,
)