package com.joyersapp.feature.profile.presentation

import androidx.compose.runtime.key
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.presentation.identity.IdentityEvent
import com.joyersapp.common_widgets.Gender
import com.joyersapp.feature.profile.domain.usecase.GetTitlesUseCase
import com.joyersapp.core.SessionManager
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
import com.joyersapp.feature.profile.domain.usecase.GetUserProfileUseCase
import com.joyersapp.feature.profile.domain.usecase.UploadPictureServerUseCase
import com.joyersapp.feature.profile.domain.usecase.UploadUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val uploadUserProfileUseCase: UploadUserProfileUseCase,
    private val uploadPictureServerUseCase: UploadPictureServerUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getTitlesUseCase: GetTitlesUseCase,
    private val getSubTitlesUseCase: GetSubTitlesUseCase,
    private val getCountryListUseCase: GetCountryListUseCase,
    private val getEducationListUseCase: GetEducationListUseCase,
    private val getEthenicityListUseCase: GetEthnicityListUseCase,
    private val getFaithReligionListUseCase: GetFaithReligionListUseCase,
    private val getInterestListUseCase: GetInterstsListUseCase,
    private val getPoliticalIdeologyListCase: GetPoliticalIdeoogyListUseCase,
    private val getRelationShipListUseCase: GetRelationshipListUseCase,
    private val getLanguageListUseCase: GetLanguageListUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        UserProfileUiState()
    )
    private val _uiStateMagnetics = MutableStateFlow(
        EditMagneticsUiState()
    )
    private val _navigationEvents = MutableSharedFlow<UserProfileNavigationEvent>()

    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()
    val uiStateMagnetics: StateFlow<EditMagneticsUiState> = _uiStateMagnetics.asStateFlow()
    val navigationEvents = _navigationEvents

    val req = UserProfileGraphRequestDto()

    init {
        // simulate fetch
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val getProfileJob = async { getUserProfileData() }
            val getTitlesJob = async { loadTitles() }
            val getCountryJob = async { loadCountryList() }
            val getEducationJob = async { loadEducationList() }
            val getEthinicityJob = async { loadEthinicityList() }
            val getFaithReligionJob = async { loadFaithReligionList() }
            val getInterestJob = async { loadInterestList() }
            val getPoliticalIdeologyJob = async { loadPoliticalIdeologyList() }
            val getRelationShipJob = async { loadRelationShipList() }
            val getLanguageJob = async { loadLanguageList() }

            getProfileJob.join()
            getTitlesJob.join()
            getCountryJob.join()
            getEducationJob.join()
            getEthinicityJob.join()
            getFaithReligionJob.join()
            getInterestJob.join()
            getPoliticalIdeologyJob.join()
            getRelationShipJob.join()
            getLanguageJob.join()

            delay(10)


            _uiState.update { it.copy(isLoading = false) }
        }
    }

     fun initSelections() {
        val state = _uiState.value
        _uiState.value.educationList.forEach { if (it.id.equals(state.education?.id)) { it.isSelected = true } }
        _uiState.value.relationShipList.map { if (it.id.equals(state.relationship?.id)) it.isSelected = true }

         val selectedIds = state.politicalIdeology?.map { it.dropdownPoliticalIdeology?.id }?.toSet()
         val merged = _uiState.value.politicalIdeologyList.map { item ->
             item.copy(isSelected = selectedIds?.contains(item.id) == true )
         }
         _uiState.update { it.copy(educationList = merged) }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {

            is UserProfileEvent.Load -> {
                getUserProfileData()
            }

            is UserProfileEvent.TabSelected -> {
                _uiState.update {
                    it.copy(
                        selectedTab = event.tab,
                    )
                }
            }

            is UserProfileEvent.Logout -> {
                viewModelScope.launch {
                    sessionManager.logout()
                }
            }

            is UserProfileEvent.UpdateUserData -> {
                uploadUserProfileData(req)
            }

            is UserProfileEvent.OnApplyIdentification -> {
                _uiStateMagnetics.update {
                    it.copy(
                        username = event.value.name,
                        birthday = event.value.birthday,
//                        gender = event.value.gender,
                        nationality = event.value.nationality,
                        ethnicity = event.value.ethnicity,
                        faith = event.value.faith,
                        languages = event.value.language,
                        education = event.value.education,
                        relationship = event.value.relationship,
                        politicalIdeology = event.value.politicalIdeology,
                        location = event.value.joyerLocation,
                ) }
            }

            is UserProfileEvent.OnApplyDescription -> {
                    when(event.key) {
                        "Education" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val merged = _uiState.value.educationList.map { item ->
                                item.copy(isSelected = item.id in selectedIds)
                            }
                            _uiState.update { it.copy(educationList = merged) }
                        }
                        "Political Ideology" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val merged = _uiState.value.politicalIdeologyList.map { item ->
                                item.copy(isSelected = item.id in selectedIds)
                            }
                            _uiState.update { it.copy(educationList = merged) }
                        }
                        "Relationship" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val merged = _uiState.value.relationShipList.map { item ->
                                item.copy(isSelected = item.id in selectedIds)
                            }
                            _uiState.update { it.copy(educationList = merged) }
                        }
                    }
            }

            is UserProfileEvent.ToggleProfileHeaderDialog -> {
                if (event.updateProfileHeaderData) {
                    _uiState.update {
                        it.copy(
                            showEditProfileHeaderDialog = event.show,
                            profilePicture = uiState.value.profileHeaderData.profilePicturePath ?: "",
                            backgroundPicture = uiState.value.profileHeaderData.backgroundPicturePath ?: ""
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            showEditProfileHeaderDialog = event.show,
                        )
                    }
                }
            }

            is UserProfileEvent.ToggleDescriptionDialog -> {
                initSelections()
                _uiState.update {
                    it.copy(
//                        showIdentificationDialog = false,
                        showEditDescriptionDialog = event.show,
                        dialogHeader = event.headers,
                        titlesData = event.titlesData,
                        selectedItems = event.selectedItems
                    )
                }
            }

            is UserProfileEvent.ToggleIdentificationDialog -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = event.show,
                    )
                }
            }

            is UserProfileEvent.ToggleMentionJoyersDialog -> {
                _uiState.update {
                    it.copy(
                        showMentionJoyersDialog = event.show,
                    )
                }
            }

            is UserProfileEvent.BackgroundPicturePathChanged -> {
                /*_uiState.update {
                    it.copy(
                        backgroundPicture = event.value
                    )
                }*/
                uploadPictureServer(2, event.value)
            }

            is UserProfileEvent.ProfilePicturePathChanged -> {
                /*_uiState.update {
                    it.copy(
                        profilePicture = event.value
                    )
                }*/
                uploadPictureServer(1, event.value)
            }

            is UserProfileEvent.UpdateProfileHeaderData -> {
                _uiState.update {
                    it.copy(
                        profileHeaderData = event.profileHeaderData
                    )
                }
            }
        }
    }


    private fun uploadUserProfileData(requestDto: UserProfileGraphRequestDto) {
        viewModelScope.launch {

            val result = uploadUserProfileUseCase(requestDto)
            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            error = null,
                            errorMessage = null,
                            username = response.username ?: "",
                            fullname = (response.firstName ?: "") + " " + (response.lastName ?: ""),
                            location = response.joyerLocation ?: "",
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            likes = response.likesCount ?: "",
                            following = response.followingCount ?: "",
                            followers = response.followersCount ?: "",
                            joyerStatus = response.joyerStatus ?: "",
//                            birthday = response.b ?: "",
                            gender = response.gender ?: "",
                            relationship = response.relationship,
                            education = response.education,
//                            children = response.ch?.name?: "",
                            politicalIdeology = response.politicalIdeology,
                            titleName = response.title?.name ?: "",
                            subTitleName = response.subTitle?.name ?: "",
                            title = response.title,
                            subTitle = response.subTitle,
                            areaOfInterest = response.interests,
//                            languages = response.languages,
                            joySince = response.joySince ?: "",
                            joySinceDuration = response.joySinceDuration ?: "",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality?.name ?: "",
                            ethnicity = response.ethnicity?.name ?: "",
                            faith = response.faith?.name ?: "",
                            educationName = response.education?.name ?: "",
                        )
                    }
                    _uiStateMagnetics.update {
                        it.copy(
                            username = response.username ?: "",
                            fullname = (response.firstName ?: "") + " " + (response.lastName ?: ""),
                            location = response.joyerLocation ?: "",
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            joyerStatus = response.joyerStatus ?: "",
//                            birthday = response.b ?: "",
                            gender = response.gender ?: "",
                            relationship = response.relationship,
//                            children = response.ch?.name?: "",
                            politicalIdeology = response.politicalIdeology,
                            title = response.title,
                            subTitle = response.subTitle,
                            areaOfInterest = response.interests,
                            languages = response.languages,
                            nationality = response.nationality,
                            ethnicity = response.ethnicity,
                            faith = response.faith,
                            education = response.education,
                        )
                    }
                    _navigationEvents.emit(UserProfileNavigationEvent.NavigateToUserProfile)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    private fun uploadPictureServer(imageId: Int, imagePath: String) {
        viewModelScope.launch {
            val result = uploadPictureServerUseCase(imageId, imagePath)
            result.fold(
                onSuccess = { response ->
                    if (imageId == 1) {
                        /*_uiState.update {
                            it.copy(
                                error = null,
                                errorMessage = null,
                                profilePicture = response.data?.profilePicture ?: "",
                            )
                        }*/
                        _uiState.update {
                            it.copy(
                                profileHeaderData = uiState.value.profileHeaderData.copy(
                                    profilePicturePath = response.data?.profilePicture ?: "",
                                )
                            )
                        }
                    } else {
                        /*_uiState.update {
                            it.copy(
                                error = null,
                                errorMessage = null,
                                backgroundPicture = response.data?.backgroundPicture ?: "",
                            )
                        }*/
                        _uiState.update {
                            it.copy(
                                profileHeaderData = uiState.value.profileHeaderData.copy(
                                    backgroundPicturePath = response.data?.backgroundPicture ?: "",
                                )
                            )
                        }
                    }
                    //_navigationEvents.emit(UserProfileNavigationEvent.NavigateToUserProfile)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    private fun getUserProfileData() {
        val state = _uiState.value
        viewModelScope.launch {

            val result = getUserProfileUseCase()
            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            error = null,
                            errorMessage = null,
                            username = response.username ?: "",
                            fullname = (response.firstName ?: "") + " " + (response.lastName ?: ""),
                            location = response.joyerLocation ?: "",
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            likes = response.likesCount ?: "",
                            following = response.followingCount ?: "",
                            followers = response.followersCount ?: "",
                            joyerStatus = response.joyerStatus ?: "",
//                            birthday = response.b ?: "",
                            gender = response.gender ?: "",
                            relationship = response.relationship,
                            education = response.education,
//                            children = response.ch?.name?: "",
                            politicalIdeology = response.politicalIdeology,
                            titleName = response.title?.name ?: "",
                            subTitleName = response.subTitle?.name ?: "",
                            title = response.title,
                            subTitle = response.subTitle,
                            areaOfInterest = response.interests,
//                            languages = response.languages,
                            joySince = response.joySince ?: "",
                            joySinceDuration = response.joySinceDuration ?: "",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality?.name ?: "",
                            ethnicity = response.ethnicity?.name ?: "",
                            faith = response.faith?.name ?: "",
                            educationName = response.education?.name ?: "",
                        )
                    }

                    _uiStateMagnetics.update {
                        it.copy(
                            username = response.username ?: "",
                            fullname = (response.firstName ?: "") + " " + (response.lastName ?: ""),
                            location = response.joyerLocation ?: "",
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            joyerStatus = response.joyerStatus ?: "",
//                            birthday = response.b ?: "",
                            gender = response.gender ?: "",
                            relationship = response.relationship,
//                            children = response.ch?.name?: "",
                            politicalIdeology = response.politicalIdeology,
                            title = response.title,
                            subTitle = response.subTitle,
                            areaOfInterest = response.interests,
                            languages = response.languages,
                            nationality = response.nationality,
                            ethnicity = response.ethnicity,
                            faith = response.faith,
                            education = response.education,
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadTitles() {
        val state = _uiState.value
        viewModelScope.launch {

            val result =
                getTitlesUseCase()

            result.fold(
                onSuccess = { titles ->
                    _uiState.update { old ->
                        old.copy(
                            titles = titles,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadSubTitles() {
        val state = _uiState.value
        viewModelScope.launch {

            val result =
                getSubTitlesUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
//                            subTitles = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadCountryList() {
        viewModelScope.launch {

            val result = getCountryListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            countryList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadEducationList() {
        viewModelScope.launch {

            val result = getEducationListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            educationList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadEthinicityList() {
        viewModelScope.launch {

            val result = getEthenicityListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            ethenicityList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadFaithReligionList() {
        viewModelScope.launch {

            val result = getFaithReligionListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            faithReligionList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadInterestList() {
        viewModelScope.launch {

            val result = getInterestListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            interestList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadPoliticalIdeologyList() {
        viewModelScope.launch {

            val result = getPoliticalIdeologyListCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            politicalIdeologyList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadRelationShipList() {
        viewModelScope.launch {

            val result = getRelationShipListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            relationShipList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadLanguageList() {
        viewModelScope.launch {

            val result = getLanguageListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            languageList = list,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
//                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

}