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
    @SerializedName("birth_date"         ) var birthDate         : String?              = null,
    @SerializedName("background_picture" ) var backgroundPicture : String?              = null,
    @SerializedName("profile_picture"    ) var profilePicture    : String?              = null,
    @SerializedName("bio"                ) var bio               : String?              = null,
    @SerializedName("website_url"                ) var websiteUrl               : String?              = null,
    @SerializedName("joyer_status"       ) var joyerStatus       : String?              = null,
    @SerializedName("joy_since"          ) var joySince          : String?              = null,
    @SerializedName("joy_since_duration" ) var joySinceDuration  : String?              = null,
    @SerializedName("qr_code"            ) var qrCode            : String?              = null,
    @SerializedName("joyer_location"     ) var joyerLocation     : String?              = null,
    @SerializedName("title"              ) var title             : ProfileMeta?         = null,
    @SerializedName("location"           ) var location          : ProfileMeta?         = null,
    @SerializedName("sub_title"          ) var subTitle          : ProfileMeta?         = null,
    @SerializedName("ethnicity"          ) var ethnicity         : ProfileMeta?         = null,
    @SerializedName("faith"              ) var faith             : ProfileMeta?         = null,
    @SerializedName("education"          ) var education         : ProfileMeta?         = null,
    @SerializedName("relationship"       ) var relationship      : ProfileMeta?         = null,
    @SerializedName("likes_count"        ) var likesCount        : String?              = null,
    @SerializedName("following_count"    ) var followingCount    : String?              = null,
    @SerializedName("followers_count"    ) var followersCount    : String?              = null,
    @SerializedName("account_status"     ) var accountStatus     : String?              = null,
    @SerializedName("nationality"        ) var nationality       : ArrayList<Nationality>? = null,
    @SerializedName("political_ideology" ) var politicalIdeology : ArrayList<PoliticalIdeology>? = null,
    @SerializedName("interests"          ) var interests         : ArrayList<Interests>? = null,
    @SerializedName("languages"          ) var languages         : ArrayList<Languages>? = null,
    @SerializedName("sublanguages"          ) var sublanguages         : ArrayList<Languages>? = null,
//    @SerializedName("user_bios"          ) var userBios          : ArrayList<String> = arrayListOf()

)

data class ProfileMeta(

    @SerializedName("id"          )  var id          : String? = null,
    @SerializedName("name"        )  var name        : String? = null,
    @SerializedName("description" )  var description : String? = null
)

data class Languages (

    @SerializedName("language"    ) var language    : Language? = null,
    @SerializedName("sublanguages" ) var sublanguages : ArrayList<SubLanguageWrapper>? = null

)
data class SubLanguageWrapper(
    @SerializedName("sublanguage" ) val sublanguage: Language? = null
)

data class Language (

    @SerializedName("id"          ) var id          : String? = null,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("label"       ) var level       : String? = null,

    )
data class Interests (

    @SerializedName("dropdown_interests" ) var dropdownInterests : ProfileMeta? = null

)
data class PoliticalIdeology (

    @SerializedName("political_ideology" ) var politicalIdeology : ProfileMeta? = null,
    @SerializedName("subpolitical_ideology" ) var subpoliticalIdeology : String?            = null

)
data class Nationality (

    @SerializedName("dropdown_countries" ) var dropdownCountries : ProfileMeta? = null

)