package com.joyersapp.feature.post.presentation

import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.components.dialogs.HighlightBullet
import com.joyersapp.components.dialogs.HighlightEvent
import com.joyersapp.feature.profile.domain.usecase.GetTitlesUseCase
import com.joyersapp.core.SessionManager
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Language
import com.joyersapp.feature.profile.data.remote.dto.LanguageReq
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.SubLanguageReq
import com.joyersapp.feature.profile.data.remote.dto.SubLanguageWrapper
import com.joyersapp.feature.profile.data.remote.dto.UserProfileGraphRequestDto
import com.joyersapp.feature.profile.domain.usecase.GetCountryListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetEducationListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetEthnicityListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetFaithReligionListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetInterstsListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetLanguageListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetPoliticalIdeoogyListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetRelationshipListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetSubTitlesUseCase
import com.joyersapp.feature.profile.domain.usecase.GetEditMagneticsUserListUseCase
import com.joyersapp.feature.profile.domain.usecase.GetUserProfileUseCase
import com.joyersapp.feature.profile.domain.usecase.UploadPictureServerUseCase
import com.joyersapp.feature.profile.domain.usecase.UploadUserProfileUseCase
import com.joyersapp.feature.profile.presentation.EditMagneticsUiState
import com.joyersapp.feature.profile.presentation.ProfileHeaderData
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.feature.profile.presentation.UserProfileNavigationEvent
import com.joyersapp.feature.profile.presentation.UserProfileUiState
import com.joyersapp.utils.UiText
import com.joyersapp.utils.graphemeCount
import com.joyersapp.utils.takeGraphemes
import com.joyersapp.utils.toHighlightBullets
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateJoyUiState(

    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,

)

sealed class CreateJoyNavigationEvent {


}

sealed class CreateJoyEvent {

}

@HiltViewModel
class CreateJoyViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateJoyUiState())
    val uiState: StateFlow<CreateJoyUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<CreateJoyNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()


    fun onEvent(event: CreateJoyEvent) {
//        when (event) {
//
//        }
    }

}