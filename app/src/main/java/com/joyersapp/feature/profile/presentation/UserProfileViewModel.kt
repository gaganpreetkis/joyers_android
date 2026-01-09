package com.joyersapp.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.common_widgets.IdentificationData
import com.joyersapp.feature.profile.domain.usecase.GetTitlesUseCase
import com.joyersapp.core.SessionManager
import com.joyersapp.feature.profile.data.remote.dto.Interests
import com.joyersapp.feature.profile.data.remote.dto.Language
import com.joyersapp.feature.profile.data.remote.dto.LanguageReq
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.Nationality
import com.joyersapp.feature.profile.data.remote.dto.PoliticalIdeology
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
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
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
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
    private val getEditMagneticsUserListUseCase: GetEditMagneticsUserListUseCase,
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



    init {
        // simulate fetch
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val getProfileJob = async { getUserProfileData() }
            val getEditMagneticsUserListJob = async { getEditMagneticsUserListData() }
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
            getEditMagneticsUserListJob.join()
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

            is UserProfileEvent.InitMagneticsData -> {
                val state = _uiState.value
                _uiState.update {
                    it.copy(
                        magneticsData = MagneticsData(
                            username = state.username,
                            profileHeaderData = ProfileHeaderData(
                                profilePicture = state.profilePicture,
                                backgroundPicture = state.backgroundPicture,
                                bio = state.bio,
                                websiteUrl = state.websiteUrl,
                            ),
                            joyerStatus = state.joyerStatus,
                            title = state.title,
                            subTitle = state.subTitle,
                            interests = state.areaOfInterest,
                            identificationData = IdentificationData(
                                name = state.fullname,
                                birthday = state.birthday,
                                gender = state.gender,
                                nationality = state.nationality,
                                ethnicity = state.ethnicity,
                                faith = state.faith,
                                language = state.languages,
                                education = state.education,
                                relationship = state.relationship,
                                politicalIdeology = state.politicalIdeology,
                                location = state.location,
                            ),
                        )
                    )
                }
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
                val magneticsData = _uiState.value.magneticsData

                val languageList = arrayListOf<LanguageReq>()
                magneticsData.identificationData?.language?.forEach { item ->
                    languageList.add(
                        LanguageReq(item.language?.id?: "", item.language?.level?: "")
                    )
                }

                val requestDto = UserProfileGraphRequestDto(
                    profilePicture = magneticsData.profileHeaderData?.profilePicture,
                    backgroundPicture = magneticsData.profileHeaderData?.backgroundPicture,
                    bio = magneticsData.profileHeaderData?.bio,
                    websiteUrl = magneticsData.profileHeaderData?.websiteUrl,
                    joyerStatus = magneticsData.joyerStatus,
                    titleId = magneticsData.title?.id,
                    subTitleId = magneticsData.subTitle?.id,
                    firstName = magneticsData.identificationData?.name?.trim()?.split(" ")?.firstOrNull(),
                    lastName = magneticsData.identificationData?.name?.trim()?.split(" ")?.drop(1)?.joinToString(" "),
                    birthDate = magneticsData.identificationData?.birthday,
                    gender = magneticsData.identificationData?.gender,
                    languageId = languageList,
                    nationalityId = if (magneticsData.identificationData?.nationality.isNullOrEmpty()) null else magneticsData.identificationData.nationality?.map { it.dropdownCountries?.id?: "" },
                    interestIds = if (magneticsData.interests.isNullOrEmpty()) null else magneticsData.interests?.map { it.dropdownInterests?.id?: "" },
                    politicalIdeologyId = if (magneticsData.identificationData?.politicalIdeology.isNullOrEmpty()) null else magneticsData.identificationData.politicalIdeology?.map { it.dropdownPoliticalIdeology?.id?: "" },
                    ethnicityId = magneticsData.identificationData?.ethnicity?.id,
                    faithId = magneticsData.identificationData?.faith?.id,
                    educationId = magneticsData.identificationData?.education?.id,
                    relationshipId = magneticsData.identificationData?.relationship?.id,
                    joyerLocationId = magneticsData.identificationData?.location?.id,
                )
                uploadUserProfileData(requestDto)
            }

            is UserProfileEvent.OnApplyIdentification -> {
                val magneticsData = _uiState.value.magneticsData.copy(identificationData = uiState.value.identificationData)

                _uiState.update {
                    it.copy(
                        magneticsData = magneticsData
                ) }
            }

            is UserProfileEvent.OnApplyProfileHeader -> {
                val magneticsData = _uiState.value.magneticsData.copy(profileHeaderData = event.value)

                _uiState.update {
                    it.copy(
                        magneticsData = magneticsData
                ) }
            }

            is UserProfileEvent.OnApplyMultipleSelections -> {
                    when(event.key) {
                        "Interests" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.interestList.filter() { it.id in selectedIds }

                            val selectedMeta = selected.map {
                                Interests(
                                    dropdownInterests = ProfileMeta(
                                        id = it.id,
                                        name = it.name,
                                        description = it.description,
                                    )
                                )
                            }
                            _uiState.update { it.copy(magneticsData = _uiState.value.magneticsData.copy(interests = selectedMeta)) }
                        }
                        "Nationality" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.countryList.filter() { it.id in selectedIds }

                            val selectedMeta = selected.map {
                                Nationality(
                                    dropdownCountries = ProfileMeta(
                                        id = it.id,
                                        name = it.name,
                                        description = it.description,
                                    )
                                )
                            }

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(nationality = selectedMeta)) }
                        }
                        "Ethnicity" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.ethenicityList.firstOrNull() { it.id in selectedIds }
                            val selectedMeta = ProfileMeta(
                                id = selected?.id,
                                name = selected?.name,
                                description = selected?.description,
                            )

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(ethnicity = selectedMeta)) }

                        }
                        "Faith" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.faithReligionList.firstOrNull() { it.id in selectedIds }
                            val selectedMeta = ProfileMeta(
                                id = selected?.id,
                                name = selected?.name,
                                description = selected?.description,
                            )

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(faith = selectedMeta)) }

                        }
                        "Education" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.educationList.firstOrNull() { it.id in selectedIds }
                            val selectedMeta = ProfileMeta(
                                id = selected?.id,
                                name = selected?.name,
                                description = selected?.description,
                            )

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(education = selectedMeta)) }

                        }
                        "Relationship" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.relationShipList.firstOrNull() { it.id in selectedIds }
                            val selectedMeta = ProfileMeta(
                                id = selected?.id,
                                name = selected?.name,
                                description = selected?.description,
                            )

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(relationship = selectedMeta)) }
                        }
                        "Political Ideology" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.politicalIdeologyList.filter() { it.id in selectedIds }

                            val selectedMeta = selected.map {
                                PoliticalIdeology(
                                    dropdownPoliticalIdeology = ProfileMeta(
                                        id = it.id,
                                        name = it.name,
                                        description = it.description,
                                    )
                                )
                            }

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(politicalIdeology = selectedMeta)) }
                        }
                        "Joyer Location" -> {
                            val selectedIds = event.value.map { it.id }.toSet()
                            val selected = _uiState.value.countryList.firstOrNull() { it.id in selectedIds }

                            val selectedMeta = ProfileMeta(
                                id = selected?.id,
                                name = selected?.name,
                                description = selected?.description,
                            )

                            _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(location = selectedMeta)) }
                        }
                    }
            }

            is UserProfileEvent.OnClearMultipleSelections -> {
                when(event.key) {
                    "Name" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(name = "")) }
                    }
                    "Birthday" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(birthday = "")) }
                    }
                    "Gender" -> {
//                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(gender = "")) }
                    }
                    "Nationality" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(nationality = null)) }
                    }
                    "Ethnicity" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(ethnicity = null)) }
                    }
                    "Faith" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(faith = null)) }
                    }
                    "Language" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(language = null)) }
                    }
                    "Education" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(education = null)) }
                    }
                    "Relationship" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(relationship = null)) }
                    }
                    "Political Ideology" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(politicalIdeology = null)) }
                    }
                    "Joyer Location" -> {
                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(location = null)) }
                    }
                }
            }

            is UserProfileEvent.OnGenderSelected -> {
                _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(gender = event.value)) }
            }

            is UserProfileEvent.OnBioChanged -> {
                _uiState.update { it.copy(
                    profileHeaderData = _uiState.value.profileHeaderData.copy(
                        bio = event.value,
                        overviewRemainingChars = 150 - event.value.graphemeCount()
                    ),
                ) }
            }

            is UserProfileEvent.OnHighlightChanged -> {
                _uiState.update { it.copy(
                    profileHeaderData = _uiState.value.profileHeaderData.copy(
                        highlightText = event.value,
                        highlightsRemainingChars = 25 - event.value.graphemeCount()
                    ),
                ) }
            }

            is UserProfileEvent.OnWebsiteUrlChanged -> {
                _uiState.update { it.copy(profileHeaderData = _uiState.value.profileHeaderData.copy(websiteUrl = event.value)) }
            }

            is UserProfileEvent.OnApplyBirthday -> {
                _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(birthday = event.value)) }
            }

            is UserProfileEvent.OnNameChanged -> {
                _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(name = event.value)) }
            }

            is UserProfileEvent.ToggleProfileHeaderDialog -> {
                _uiState.update {
                    it.copy(
                        showEditProfileHeaderDialog = event.show,
                        profileHeaderData = uiState.value.magneticsData.profileHeaderData?: ProfileHeaderData()
                    )
                }
            }

            is UserProfileEvent.ToggleMultipleSelectionsDialog -> {
                val selectedIds = event.selectedIds
                val merged = event.titlesData.map { item ->
                    item.copy(isSelected = selectedIds.contains(item.id) == true )
                }

                _uiState.update {
                    it.copy(
                        showMultipleSelectionsDialog = event.show,
                        dialogHeader = event.headers,
                        titlesData = merged,
                    )
                }
                _uiStateMagnetics.update {
                    it.copy(
                        key = event.key,
                        selectedIds = event.selectedIds,
                        isMultiselectEnabled = event.isMultiSelectEnabled,
                    )
                }
//                viewModelScope.launch {
//                    _navigationEvents.emit(UserProfileNavigationEvent.NavigateToDescriptionDialog(""))
//                }

            }

            is UserProfileEvent.ToggleDescriptionDialog -> {
                val state = uiState.value.magneticsData
                val dialogHeader = arrayListOf("Description", "Joyer Status", state.joyerStatus, )
                if (state.subTitle?.id.isNullOrEmpty()) {
                    dialogHeader.add(state.title?.name?: "")
                } else {
                    dialogHeader.add(state.title?.name?: "")
                    dialogHeader.add(state.subTitle.name?: "")
                }

                _uiState.update {
                    it.copy(
                        isSubTitleMode = false,
                        showDescriptionDialog = event.show,
                        dialogHeader = dialogHeader,
                        titlesData = uiState.value.titles,
                    )
                }

            }

            is UserProfileEvent.ToggleIdentificationDialog -> {
                val state = _uiState.value
                val uiStateMagnetics = uiStateMagnetics.value
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = event.show,
                        identificationData = state.magneticsData.identificationData?: IdentificationData()
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

            is UserProfileEvent.ToggleDatePickerDialog -> {
                _uiState.update {
                    it.copy(
                        showDatePickerDialog = event.show,
                    )
                }
            }

            is UserProfileEvent.ToggleLanguageDialog -> {
                val selectedIds = uiState.value.identificationData.language?.associateBy( { it.language?.id?: "" }, { it.language?.level?: "" })
                val merged = uiState.value.languageList.map { item ->
                    val newLevel = selectedIds?.get(item.id)
                    item.copy(
                        isSelected = selectedIds?.contains(item.id) == true,
                        level = newLevel?: ""
                        )
                }

                _uiState.update {
                    it.copy(
                        showLanguagesDialog = event.show,
                        titlesData = merged,
//                        selectedIds = selectedIds
                    )
                }
            }

            is UserProfileEvent.OnApplyLanguage -> {
                val selectedIds = event.value.associateBy( { it.id },{ it.level } )
                val selected = _uiState.value.languageList.filter() { it.id in selectedIds }

                val selectedMeta = selected.map { item ->
                    val newLevel = selectedIds?.get(item.id)
                    Languages(
                        language = Language(
                            id = item.id,
                            name = item.name,
                            description = item.description,
                            level = newLevel,
                        )
                    )
                }

                _uiState.update { it.copy(
                    showLanguagesDialog = false,
                    identificationData = _uiState.value.identificationData.copy(language = selectedMeta)
                ) }

            }

            is UserProfileEvent.BackgroundPicturePathChanged -> {

                uploadPictureServer(2, event.value)
            }

            is UserProfileEvent.ProfilePicturePathChanged -> {

                uploadPictureServer(1, event.value)
            }

            is UserProfileEvent.UpdateProfileHeaderData -> {
                _uiState.update {
                    it.copy(
                        profileHeaderData = event.profileHeaderData
                    )
                }
            }

            is UserProfileEvent.OnApplyDescription -> {
                _uiState.update {
                    it.copy(
                        showDescriptionDialog = false,
                        magneticsData = uiState.value.magneticsData.copy(
                            title = event.selectedTitle,
                            subTitle = event.selectedSubTitle,
                        )
                    )
                }
            }
        }
    }


    private fun uploadUserProfileData(requestDto: UserProfileGraphRequestDto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = uploadUserProfileUseCase(requestDto)
            result.fold(
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            username = response.username ?: "",
                            fullname = ((response.firstName ?: "") + " " + (response.lastName ?: "")).trim(),
                            location = response.location,
                            joyerLocation = response.joyerLocation,
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            bio = response.bio ?: "",
                            websiteUrl = response.websiteUrl ?: "",
                            likes = response.likesCount ?: "",
                            following = response.followingCount ?: "",
                            followers = response.followersCount ?: "",
                            joyerStatus = response.joyerStatus ?: "",
                            birthday = response.birthDate ?: "",
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
                            languages = response.languages,
                            joySince = response.joySince ?: "",
                            joySinceDuration = response.joySinceDuration ?: "",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality,
                            ethnicity = response.ethnicity,
                            faith = response.faith,
                            educationName = response.education?.name ?: "",
                        )
                    }
                    _navigationEvents.emit(UserProfileNavigationEvent.NavigateToUserProfile)
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
                                    profilePicture = response.data?.profilePicture ?: "",
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
                                    backgroundPicture = response.data?.backgroundPicture ?: "",
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
                            errorMessage = error.message
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
                            errorMessage = null,
                            username = response.username ?: "",
                            fullname = ((response.firstName ?: "") + " " + (response.lastName ?: "")).trim(),
                            location = response.location,
                            joyerLocation = response.joyerLocation,
                            profilePicture = response.profilePicture ?: "",
                            backgroundPicture = response.backgroundPicture ?: "",
                            bio = response.bio ?: "",
                            websiteUrl = response.websiteUrl ?: "",
                            likes = response.likesCount ?: "",
                            following = response.followingCount ?: "",
                            followers = response.followersCount ?: "",
                            joyerStatus = response.joyerStatus ?: "",
                            birthday = response.birthDate ?: "",
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
                            languages = response.languages,
                            joySince = response.joySince ?: "",
                            joySinceDuration = response.joySinceDuration ?: "",
                            qrCode = response.qrCode ?: "",
                            nationality = response.nationality,
                            ethnicity = response.ethnicity,
                            faith = response.faith,
                            educationName = response.education?.name ?: "",
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

    private fun getEditMagneticsUserListData() {
        viewModelScope.launch {

            val result = getEditMagneticsUserListUseCase()

            result.fold(
                onSuccess = { list ->
                    _uiState.update { old ->
                        old.copy(
                            editMagneticsUserList = list,
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