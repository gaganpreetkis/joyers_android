package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProfileLanguagesResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("data"       ) var data       : List<ProfileLanguagesData> = arrayListOf(),
    @SerializedName("message"    ) var message    : String?         = null

)


data class ProfileLanguagesData (

    @SerializedName("id"            ) var id           : String? = null,
    @SerializedName("name"          ) var name         : String? = null,
    @SerializedName("description"   ) var description  : String? = null,
    @SerializedName("dropdown_sub_langauge_list"    ) var selections   : ArrayList<ProfileLanguagesData>? = null,

    @SerializedName("label"         ) var level        : String? = "Basic",
//    @SerializedName("selection_count") var selectionCount : String? = null,
//    @SerializedName("display_order" ) var displayOrder : Int?    = null,
    var isSelected : Boolean = false,
    var isSelectionMode : Boolean = false

)