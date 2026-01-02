package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileResponseDto (

    @SerializedName("statusCode" ) var statusCode : Int?    = null,
    @SerializedName("data"       ) var data       : UserProfile?   = UserProfile(),
    @SerializedName("message"    ) var message    : String? = null

)


data class UserProfile (

    @SerializedName("id"                 ) var id                : String?              = null,
    @SerializedName("username"           ) var username          : String?              = null,
    @SerializedName("mobile"             ) var mobile            : String?              = null,
    @SerializedName("country_code"       ) var countryCode       : String?              = null,
    @SerializedName("first_name"         ) var firstName         : String?              = null,
    @SerializedName("last_name"          ) var lastName          : String?              = null,
    @SerializedName("gender"             ) var gender            : String?              = null,
    @SerializedName("background_picture" ) var backgroundPicture : String?              = null,
    @SerializedName("profile_picture"    ) var profilePicture    : String?              = null,
    @SerializedName("bio"                ) var bio               : String?              = null,
    @SerializedName("joyer_location"     ) var joyerLocation     : String?              = null,
    @SerializedName("joyer_status"       ) var joyerStatus       : String?              = null,
    @SerializedName("joy_since"          ) var joySince          : String?              = null,
    @SerializedName("joy_since_duration" ) var joySinceDuration  : String?              = null,
    @SerializedName("qr_code"            ) var qrCode            : String?              = null,
    @SerializedName("title"              ) var title             : ProfileMeta?         = ProfileMeta(),
    @SerializedName("sub_title"          ) var subTitle          : ProfileMeta?         = ProfileMeta(),
    @SerializedName("nationality"        ) var nationality       : ProfileMeta?         = ProfileMeta(),
    @SerializedName("ethnicity"          ) var ethnicity         : ProfileMeta?         = ProfileMeta(),
    @SerializedName("faith"              ) var faith             : ProfileMeta?         = ProfileMeta(),
    @SerializedName("education"          ) var education         : ProfileMeta?         = ProfileMeta(),
    @SerializedName("relationship"       ) var relationship      : ProfileMeta?         = ProfileMeta(),
    @SerializedName("political_ideology" ) var politicalIdeology : ProfileMeta?         = ProfileMeta(),
    @SerializedName("likes_count"        ) var likesCount        : String?              = null,
    @SerializedName("following_count"    ) var followingCount    : String?              = null,
    @SerializedName("followers_count"    ) var followersCount    : String?              = null,
    @SerializedName("account_status"     ) var accountStatus     : String?              = null,
    @SerializedName("interests"          ) var interests         : ArrayList<Interests> = arrayListOf(),
    @SerializedName("languages"          ) var languages         : ArrayList<Languages> = arrayListOf()
)

data class ProfileMeta (

    @SerializedName("id"          ) var id          : String? = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("description" ) var description : String? = null

)

data class Languages (

    @SerializedName("language"    ) var language    : ProfileMeta? = ProfileMeta(),
    @SerializedName("sublanguage" ) var sublanguage : Any?         = null

)

data class Interests (

    @SerializedName("dropdown_interests" ) var dropdownInterests : ProfileMeta? = ProfileMeta()

)