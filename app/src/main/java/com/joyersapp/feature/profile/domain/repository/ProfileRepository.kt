package com.joyersapp.feature.profile.domain.repository

import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfile
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto

interface ProfileRepository {

    suspend fun uploadUserProfile(requestDto: UserProfileGraphRequestDto): Result<UserProfile>
    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun getTitles(): Result<List<ProfileTitlesData>>
    suspend fun getSubTitles(): Result<List<ProfileTitlesData>>
    suspend fun getCountryList(): Result<List<ProfileTitlesData>>
    suspend fun getEducationList(): Result<List<ProfileTitlesData>>
    suspend fun getEthenicityList(): Result<List<ProfileTitlesData>>
    suspend fun getFaithReligionList(): Result<List<ProfileTitlesData>>
    suspend fun getInterestList(): Result<List<ProfileTitlesData>>
    suspend fun getPoliticalIdeologyList(): Result<List<ProfileTitlesData>>
    suspend fun getRelationShipList(): Result<List<ProfileTitlesData>>
    suspend fun getLanguageList(): Result<List<ProfileTitlesData>>


    suspend fun logout()

}