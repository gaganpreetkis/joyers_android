package com.joyersapp.auth.data.remote.dto.signup

import com.google.gson.annotations.SerializedName


data class CompleteRegistrationResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?    = null,
    @SerializedName("token"      ) var token      : String? = null,
    @SerializedName("user"       ) var user       : User?   = User(),
    @SerializedName("message"    ) var message    : String? = null

)

data class Stats (

    @SerializedName("followers" ) var followers : Int? = null,
    @SerializedName("following" ) var following : Int? = null

)

data class User (

    @SerializedName("id"           ) var id          : String? = null,
    @SerializedName("username"     ) var username    : String? = null,
    @SerializedName("email"        ) var email       : String? = null,
    @SerializedName("mobile"       ) var mobile      : String? = null,
    @SerializedName("country_code" ) var countryCode : String? = null,
    @SerializedName("role"         ) var role        : String? = null,
    @SerializedName("stats"        ) var stats       : Stats?  = Stats()

)