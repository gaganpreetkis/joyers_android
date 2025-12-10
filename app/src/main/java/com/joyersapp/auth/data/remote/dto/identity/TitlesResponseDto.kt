package com.joyersapp.auth.data.remote.dto.identity

import com.google.gson.annotations.SerializedName


data class TitlesResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?            = null,
    @SerializedName("data"       ) var data       : ArrayList<Title> = arrayListOf(),
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