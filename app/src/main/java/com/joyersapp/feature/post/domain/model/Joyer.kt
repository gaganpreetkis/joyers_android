package com.joyersapp.feature.post.domain.model

data class Joyer(
    val id: String,
    val name: String,
    val username: String,
    val profilePicture: String,
    val tag: String,
    val starsCount: Int,
    val isLockVisible: Boolean,
)