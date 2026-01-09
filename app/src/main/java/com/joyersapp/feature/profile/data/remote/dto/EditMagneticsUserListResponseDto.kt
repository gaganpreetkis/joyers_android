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
    var isSelected: Boolean = false,
) {
//    val displayName: String = getDisplayName()

    fun getDisplayName(): String {
        val fullName = listOfNotNull(first_name, last_name)
            .joinToString(" ")
            .trim()?:""
        return if (fullName.trim().isEmpty()) username?:"" else fullName
    }
}