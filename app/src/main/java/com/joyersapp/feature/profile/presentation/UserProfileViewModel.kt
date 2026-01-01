package com.joyersapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.feature.profile.domain.usecase.GetTitlesUseCase
import com.joyersapp.core.SessionManager
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
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserProfileViewModel @Inject constructor(
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
        UserProfileUiState(
            bannerUrl = null,
            avatarUrl = null
        )
    )
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

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

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: UserProfileEvent) {
        when (event) {

            is UserProfileEvent.Load -> { getUserProfileData() }

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

            UserProfileEvent.SubmitClicked -> TODO()
            is UserProfileEvent.OnDialogClosed -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = false,
                        showTitlesDialog = false,
                        showEditProfileHeaderDialog = false,
                        showEditDescriptionDialog = false,
                    )
                }
            }
            is UserProfileEvent.OnEditDescriptionClicked -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = true,
                    )
                }
            }
            is UserProfileEvent.OnEditTitleClicked -> {
                _uiState.update {
                    it.copy(
                        showTitlesDialog = true,
                    )
                }
            }
            is UserProfileEvent.OnEditProfileHeader -> {
                _uiState.update {
                    it.copy(
                        showEditProfileHeaderDialog = true,
                    )
                }
            }
            is UserProfileEvent.OnEditDescription -> {
                _uiState.update {
                    it.copy(
                        showEditDescriptionDialog = true,
                        showIdentificationDialog = false,
                        dialogHeader = event.headers
                    )
                }
            }
            is UserProfileEvent.OnEditIdentification -> {
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = true,
                    )
                }
            }
        }
    }


    private fun getUserProfileData(){
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
                            gender = response.gender?: "",
                            relationship = response.relationship?.name?: "",
//                            children = response.ch?.name?: "",
                            politicalIdeology = response.politicalIdeology?.name ?: "",
                            titleName = response.title?.name ?: "",
                            subTitleName = response.subTitle?.name ?: "",
                            title = response.title,
                            subTitle = response.subTitle,
                            areaOfInterest = response.interests,
                            languages = response.languages,
                            joySince = response.joySince ?: "",
                            joySinceDuration = response.joySinceDuration ?: "",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality?.name ?: "",
                            ethnicity = response.ethnicity?.name ?: "",
                            faith = response.faith?.name ?: "",
                            educationName = response.education?.name ?: "",
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadTitles(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadSubTitles(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadCountryList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadEducationList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadEthinicityList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadFaithReligionList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadInterestList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadPoliticalIdeologyList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadRelationShipList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

    private fun loadLanguageList(){
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
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
                }
            )
        }
    }

}