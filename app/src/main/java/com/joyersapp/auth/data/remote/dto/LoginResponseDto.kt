package com.joyersapp.auth.data.remote.dto

data class LoginResponseDto(
    var statusCode: Int,
    var message: String = "",
    var token: String = "",
    var user: User?
)

data class User(
    var id: String,
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
    var profile_picture: String?,
    var background_picture: String?,
    var stats: Stats?,
    var recentType: String?
)

data class Stats(
    var followers: Int?,
    var following: Int?
)