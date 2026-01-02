package com.joyersapp.feature.profile.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserProfileGraphRequestDto (

    @SerializedName("background_picture"    ) var backgroundPicture   : String?           = null,
    @SerializedName("profile_picture"       ) var profilePicture      : String?           = null,
    @SerializedName("mobile"                ) var mobile              : String?           = null,
    @SerializedName("country_code"          ) var countryCode         : String?           = null,
    @SerializedName("first_name"            ) var firstName           : String?           = null,
    @SerializedName("last_name"             ) var lastName            : String?           = null,
    @SerializedName("joyer_location"        ) var joyerLocation       : String?           = null,
    @SerializedName("website_url"           ) var websiteUrl          : String?           = null,
    @SerializedName("birth_date"            ) var birthDate           : String?           = null,
    @SerializedName("gender"                ) var gender              : String?           = null,
    @SerializedName("title_id"              ) var titleId             : String?           = null,
    @SerializedName("sub_title_id"          ) var subTitleId          : String?           = null,
    @SerializedName("joyer_status"          ) var joyerStatus         : String?           = null,
    @SerializedName("bio"                   ) var bio                 : String?           = null,
    @SerializedName("user_bio_id"           ) var userBioId           : ArrayList<String> = arrayListOf(),
    @SerializedName("nationality_id"        ) var nationalityId       : String?           = null,
    @SerializedName("ethnicity_id"          ) var ethnicityId         : String?           = null,
    @SerializedName("faith_id"              ) var faithId             : String?           = null,
    @SerializedName("education_id"          ) var educationId         : String?           = null,
    @SerializedName("relationship_id"       ) var relationshipId      : String?           = null,
    @SerializedName("political_ideology_id" ) var politicalIdeologyId : String?           = null,
    @SerializedName("joyer_location_id"     ) var joyerLocationId     : String?           = null,
    @SerializedName("language_id"           ) var languageId          : ArrayList<String> = arrayListOf(),
    @SerializedName("sub_language_id"       ) var subLanguageId       : ArrayList<String> = arrayListOf(),
    @SerializedName("interest_ids"          ) var interestIds         : ArrayList<String> = arrayListOf()

)