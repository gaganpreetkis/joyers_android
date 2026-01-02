package com.joyersapp.feature.profile.data.repository

import com.joyersapp.auth.data.local.SessionLocalDataSource
import com.joyersapp.feature.profile.data.remote.ProfileApi
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfile
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto
import com.joyersapp.feature.profile.domain.repository.ProfileRepository
import com.joyersapp.utils.ApiErrorException
import com.joyersapp.utils.parseNetworkError
import jakarta.inject.Inject
import retrofit2.HttpException
import retrofit2.Response

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val sessionLocalDataSource: SessionLocalDataSource
) : ProfileRepository {

    override suspend fun uploadUserProfile(
        requestDto: UserProfileGraphRequestDto
    ): Result<UserProfile> =
        try {
            val response = api.uploadUserProfile(requestDto)
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data!!)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getUserProfile(): Result<UserProfile> =
        try {
            val response = api.getUserProfile()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data!!)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getTitles(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getTitles()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getSubTitles(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getSubTitles()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getCountryList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getCountryList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getEducationList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getEducationList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getEthenicityList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getEthenicityList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getFaithReligionList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getFaithReligionList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getInterestList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getInterestList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getPoliticalIdeologyList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getPoliticalIdeologyList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getRelationShipList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getRelationShipList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getLanguageList(): Result<List<ProfileTitlesData>> =
        try {
            val response = api.getLanguageList()
            when (response.statusCode) {
                200 -> {
                    Result.success(response.data)
                }

                400 -> {
                    Result.failure(
                        ApiErrorException(
                            message = response.message ?: "Something went wrong"
                        )
                    )
                }

                else -> Result.failure(
                    ApiErrorException(
                        message = response.message ?: "Something went wrong"
                    )
                )
            }
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun logout() {
        sessionLocalDataSource.clearUserSession()
    }


    private inline fun <T> safeApi(block: () -> Response<T>): Result<T> {
        return try {
            val res = block()
            if (res.isSuccessful && res.body() != null) Result.success(res.body()!!)
            else Result.failure(Exception(res.errorBody()?.string() ?: "API Error"))
        } catch (e: HttpException) {
            val errorMsg = parseNetworkError(e)
            Result.failure(IllegalArgumentException(errorMsg, e))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}