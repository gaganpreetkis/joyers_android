package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EditProfileHeaderDialogDto(
    @SerializedName("profilePicturePath"    ) var profilePicturePath    : String?         = null,
    @SerializedName("backgroundPicturePath"    ) var backgroundPicturePath    : String?         = null,
    @SerializedName("bio"    ) var bio    : String?         = null,
    @SerializedName("websiteUrl"    ) var websiteUrl    : String?         = null,
)
