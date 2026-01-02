package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProfileTitlesResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("data"       ) var data       : List<ProfileTitlesData> = arrayListOf(),
    @SerializedName("message"    ) var message    : String?         = null

)


data class ProfileTitlesData (

    @SerializedName("id"            ) var id           : String? = null,
    @SerializedName("name"          ) var name         : String? = null,
    @SerializedName("description"   ) var description  : String? = null,
    @SerializedName("selections"   ) var selections  : List<ProfileTitlesData>? = null,
    @SerializedName("selection_count"   ) var selectionCount  : String? = null,
    @SerializedName("display_order" ) var displayOrder : Int?    = null,
    var isSelected : Boolean = false

)