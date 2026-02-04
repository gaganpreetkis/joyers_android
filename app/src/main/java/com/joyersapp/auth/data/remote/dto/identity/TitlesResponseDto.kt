package com.joyersapp.auth.data.remote.dto.identity

import com.google.gson.annotations.SerializedName
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData


data class TitlesResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("data"       ) var data       : ArrayList<ProfileTitlesData> = arrayListOf(),
    @SerializedName("message"    ) var message    : String?         = null

)

data class Title (

    @SerializedName("id"              ) var id             : String?               = null,
    @SerializedName("name"            ) var name           : String?               = null,
    @SerializedName("description"     ) var description    : String?               = null,
    @SerializedName("selections"      ) var subTitles     : ArrayList<SubTitle> = arrayListOf(),
    @SerializedName("selection_count" ) var selectionCount : Int?                  = null

)

data class SubTitle (

    @SerializedName("id"          ) var id          : String? = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("description" ) var description : String? = null

)