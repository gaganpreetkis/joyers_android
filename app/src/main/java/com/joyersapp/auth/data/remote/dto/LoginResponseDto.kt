package com.joyersapp.auth.data.remote.dto

data class LoginResponseDto(
    var statusCode: Int,
    var message: String = "",
    var token: String = "",
    var user: User?
)

data class User(
    var id: Int,
    var username: String?,
    var email: String?,
    var mobile: String?,
    var country_code: String?,
    var firstName: String?,
    var lastName: String?,
    var dateOfBirth: String?,
    var role: String?,
    var location: String?,
    var status: String?,
    var is_oath_verified: Boolean?,
    var is_skipped: Boolean?,
    var is_identity_verified: Boolean?,
    var is_status_verified: Boolean?,
    var admin_role: String?,
    var profilePicture: String?,
    var backgroundPicture: String?,
    var stats: Stats?
)

data class Stats(
    var followers: Int?,
    var following: Int?
)