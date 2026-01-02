package com.joyersapp.feature.profile.presentation

import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto

sealed class UserProfileNavigationEvent {
    object NavigateToUserProfile : UserProfileNavigationEvent()

}