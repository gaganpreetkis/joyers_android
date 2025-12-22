package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GetUserProfileResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?    = null,
    @SerializedName("data"       ) var data       : UserProfile?   = UserProfile(),
    @SerializedName("message"    ) var message    : String? = null

)


data class UserProfile (

    @SerializedName("id"                 ) var id                : String? = null,
    @SerializedName("username"           ) var username          : String? = null,
    @SerializedName("mobile"             ) var mobile            : String? = null,
    @SerializedName("country_code"       ) var countryCode       : String? = null,
    @SerializedName("first_name"         ) var firstName         : String? = null,
    @SerializedName("last_name"          ) var lastName          : String? = null,
    @SerializedName("background_picture" ) var backgroundPicture : String? = null,
    @SerializedName("profile_picture"    ) var profilePicture    : String? = null,
    @SerializedName("bio"                ) var bio               : String? = null,
    @SerializedName("joyer_location"     ) var joyerLocation     : String? = null,
    @SerializedName("joyer_status"       ) var joyerStatus       : String? = null,
    @SerializedName("title_name"         ) var titleName         : String? = null,
    @SerializedName("sub_title_name"     ) var subTitleName      : String? = null,
    @SerializedName("joy_since"          ) var joySince          : String? = null,
    @SerializedName("joy_since_duration" ) var joySinceDuration  : String? = null,
    @SerializedName("qr_code"            ) var qrCode            : String? = null

)