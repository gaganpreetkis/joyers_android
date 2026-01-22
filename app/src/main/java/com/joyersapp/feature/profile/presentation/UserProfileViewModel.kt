package com.joyersapp.feature.profile.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
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
import com.joyersapp.utils.UiText
import com.joyersapp.utils.graphemeCount
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
    private val _navigationEvents = Channel<UserProfileNavigationEvent>(Channel.BUFFERED)
//    private val _navigationEvents = MutableSharedFlow<UserProfileNavigationEvent>(
//        extraBufferCapacity = 3
//    )

    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()
    val uiStateMagnetics: StateFlow<EditMagneticsUiState> = _uiStateMagnetics.asStateFlow()
    val navigationEvents = _navigationEvents.receiveAsFlow()


    fun onEvent(event: UserProfileEvent) {
        when (event) {

            is UserProfileEvent.Load -> {
                if (!uiState.value.isDataLoaded) {
                    // simulate fetch
                    viewModelScope.launch {

                        // show loader
                        _uiState.update { it.copy(isLoading = true) }

                        coroutineScope {
                            awaitAll(
                                async { getUserProfileData() },
                                async { getEditMagneticsUserListData() },
                                async { loadTitles() },
                                async { loadCountryList() },
                                async { loadEducationList() },
                                async { loadEthinicityList() },
                                async { loadFaithReligionList() },
                                async { loadInterestList() },
                                async { loadPoliticalIdeologyList() },
                                async { loadRelationShipList() },
                                async { loadLanguageList() }
                            )
                        }

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isDataLoaded = true
                            )
                        }
                    }
                }

            }

            is UserProfileEvent.InitMagneticsData -> {
                val state = _uiState.value

                var overviewText = ""
                var highlightsText = "• "
                var selectedTab = ""
                val prefix = "Highlights\n"
                if (state?.bio?.startsWith(prefix) == true) {
                    highlightsText = state.bio.removePrefix(prefix)
                    if (highlightsText.isEmpty()) highlightsText = "• "
                    selectedTab = "highlights"
                } else {
                    overviewText = state.bio?:""
                    selectedTab = "overview"
                }

                _uiState.update {
                    it.copy(
                        magneticsData = MagneticsData(
                            username = state.username,
                            profileHeaderData = ProfileHeaderData(
                                profilePicture = state.profilePicture,
                                backgroundPicture = state.backgroundPicture,
                                bio = state.bio,
                                selectedTab = selectedTab,
                                overviewFieldValue = TextFieldValue(
                                    text = overviewText,
                                    selection = TextRange(overviewText.length)
                                ),
                                highlightFieldValue = TextFieldValue(
                                    text = highlightsText,
                                    selection = TextRange(overviewText.length)
                                ),
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
                        ),
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
                        LanguageReq(item.language?.id ?: "", item.language?.level ?: "")
                    )
                }

                val requestDto = UserProfileGraphRequestDto(
                    profilePicture = magneticsData.profileHeaderData.profilePicture,
                    backgroundPicture = magneticsData.profileHeaderData.backgroundPicture,
                    bio = magneticsData.profileHeaderData.bio,
                    websiteUrl = magneticsData.profileHeaderData.websiteUrl,
                    joyerStatus = magneticsData.joyerStatus,
                    titleId = magneticsData.title?.id,
                    subTitleId = magneticsData.subTitle?.id,
                    firstName = magneticsData.identificationData?.name?.trim()?.split(" ")
                        ?.firstOrNull(),
                    lastName = magneticsData.identificationData?.name?.trim()?.split(" ")?.drop(1)
                        ?.joinToString(" "),
                    birthDate = magneticsData.identificationData?.birthday,
                    gender = magneticsData.identificationData?.gender,
                    languageId = languageList,
                    nationalityId = if (magneticsData.identificationData?.nationality.isNullOrEmpty()) null else magneticsData.identificationData.nationality?.map {
                        it.dropdownCountries?.id ?: ""
                    },
                    interestIds = if (magneticsData.interests.isNullOrEmpty()) null else magneticsData.interests?.map {
                        it.dropdownInterests?.id ?: ""
                    },
                    politicalIdeologyId = if (magneticsData.identificationData?.politicalIdeology.isNullOrEmpty()) null else magneticsData.identificationData.politicalIdeology?.map {
                        it.politicalIdeology?.id ?: ""
                    },
                    ethnicityId = magneticsData.identificationData?.ethnicity?.id,
                    faithId = magneticsData.identificationData?.faith?.id,
                    educationId = magneticsData.identificationData?.education?.id,
                    relationshipId = magneticsData.identificationData?.relationship?.id,
                    joyerLocationId = magneticsData.identificationData?.location?.id,
                )
                viewModelScope.launch(Dispatchers.IO) {
                    uploadUserProfileData(requestDto)
                }
            }

            is UserProfileEvent.OnApplyIdentification -> {
                val magneticsData =
                    _uiState.value.magneticsData.copy(identificationData = uiState.value.identificationData)

                _uiState.update {
                    it.copy(
                        magneticsData = magneticsData
                    )
                }
            }

            is UserProfileEvent.OnApplyProfileHeader -> {

                val prefix = "Highlights\n"
                val bio = if (event.value.selectedTab.equals("overview")) {
                    event.value.overviewFieldValue.text
                } else {
                    (prefix + event.value.highlightFieldValue.text)
                }
                val profileHeaderData = event.value.copy(bio = bio)
                val magneticsData =
                    _uiState.value.magneticsData.copy(profileHeaderData = profileHeaderData)

                _uiState.update {
                    it.copy(
                        magneticsData = magneticsData
                    )
                }
            }

            is UserProfileEvent.OnApplyMultipleSelections -> {
                when (event.key) {
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
                        _uiState.update {
                            it.copy(
                                magneticsData = _uiState.value.magneticsData.copy(
                                    interests = selectedMeta
                                )
                            )
                        }
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

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    nationality = selectedMeta
                                )
                            )
                        }
                    }

                    "Ethnicity" -> {
                        val selectedIds = event.value.map { it.id }.toSet()
                        val selected =
                            _uiState.value.ethenicityList.firstOrNull() { it.id in selectedIds }
                        val selectedMeta = ProfileMeta(
                            id = selected?.id,
                            name = selected?.name,
                            description = selected?.description,
                        )

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    ethnicity = selectedMeta
                                )
                            )
                        }

                    }

                    "Faith" -> {
                        val selectedIds = event.value.map { it.id }.toSet()
                        val selected =
                            _uiState.value.faithReligionList.firstOrNull() { it.id in selectedIds }
                        val selectedMeta = ProfileMeta(
                            id = selected?.id,
                            name = selected?.name,
                            description = selected?.description,
                        )

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    faith = selectedMeta
                                )
                            )
                        }

                    }

                    "Education" -> {
                        val selectedIds = event.value.map { it.id }.toSet()
                        val selected =
                            _uiState.value.educationList.firstOrNull() { it.id in selectedIds }
                        val selectedMeta = ProfileMeta(
                            id = selected?.id,
                            name = selected?.name,
                            description = selected?.description,
                        )

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    education = selectedMeta
                                )
                            )
                        }

                    }

                    "Relationship" -> {
                        val selectedIds = event.value.map { it.id }.toSet()
                        val selected =
                            _uiState.value.relationShipList.firstOrNull() { it.id in selectedIds }
                        val selectedMeta = ProfileMeta(
                            id = selected?.id,
                            name = selected?.name,
                            description = selected?.description,
                        )

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    relationship = selectedMeta
                                )
                            )
                        }
                    }

                    "Political Ideology" -> {
                        val selectedIds = event.value.map { it.id }.toSet()
                        val selected =
                            _uiState.value.politicalIdeologyList.filter() { it.id in selectedIds }

                        val selectedMeta = selected.map {
                            PoliticalIdeology(
                                politicalIdeology = ProfileMeta(
                                    id = it.id,
                                    name = it.name,
                                    description = it.description,
                                )
                            )
                        }

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    politicalIdeology = selectedMeta
                                )
                            )
                        }
                    }

                    "Joyer Location" -> {
                        val selectedIds = event.value.map { it.id }.toSet()
                        val selected =
                            _uiState.value.countryList.firstOrNull() { it.id in selectedIds }

                        val selectedMeta = ProfileMeta(
                            id = selected?.id,
                            name = selected?.name,
                            description = selected?.description,
                        )

                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    location = selectedMeta
                                )
                            )
                        }
                    }
                }
            }

            is UserProfileEvent.OnClearMultipleSelections -> {
                when (event.key) {
                    "Name" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    name = ""
                                )
                            )
                        }
                    }

                    "Birthday" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    birthday = ""
                                )
                            )
                        }
                    }

                    "Gender" -> {
//                        _uiState.update { it.copy(identificationData = _uiState.value.identificationData.copy(gender = "")) }
                    }

                    "Nationality" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    nationality = null
                                )
                            )
                        }
                    }

                    "Ethnicity" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    ethnicity = null
                                )
                            )
                        }
                    }

                    "Faith" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    faith = null
                                )
                            )
                        }
                    }

                    "Language" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    language = null
                                )
                            )
                        }
                    }

                    "Education" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    education = null
                                )
                            )
                        }
                    }

                    "Relationship" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    relationship = null
                                )
                            )
                        }
                    }

                    "Political Ideology" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    politicalIdeology = null
                                )
                            )
                        }
                    }

                    "Joyer Location" -> {
                        _uiState.update {
                            it.copy(
                                identificationData = _uiState.value.identificationData.copy(
                                    location = null
                                )
                            )
                        }
                    }
                }
            }

            is UserProfileEvent.OnGenderSelected -> {
                _uiState.update {
                    it.copy(
                        identificationData = _uiState.value.identificationData.copy(
                            gender = event.value
                        )
                    )
                }
            }

            is UserProfileEvent.OnToggleBioEditor -> {
                _uiState.update {
                    if (event.tab.equals("overview")) {
                        onEvent(UserProfileEvent.OnHighlightChanged(TextFieldValue(text = "")))
                    } else {
                        onEvent(UserProfileEvent.OnOverviewChanged(TextFieldValue(text = "")))
                    }
                    it.copy(
                        profileHeaderData = _uiState.value.profileHeaderData.copy(
                            selectedTab = event.tab
                        ),
                    )
                }
            }

            is UserProfileEvent.OnOverviewChanged -> {
                val overviewRemainingChars = 150 - event.value.text.graphemeCount()
                if (overviewRemainingChars >= -20) {
                    _uiState.update {
                        it.copy(
                            profileHeaderData = it.profileHeaderData.copy(
//                            bio = event.value.text,
                                overviewFieldValue = event.value.copy(selection = TextRange(event.value.text.length)),
                                overviewRemainingChars = 150 - event.value.text.graphemeCount(),
                                bioValidationError = if (overviewRemainingChars <= -1) UiText.StringResource(
                                    R.string.bio_validation_error
                                ) else null,
                            ),
                        )
                    }
                }
            }

            is UserProfileEvent.OnHighlightChanged -> {

                val websiteUrl = _uiState.value.profileHeaderData.websiteUrl
                val old = _uiState.value.profileHeaderData.highlightFieldValue
                val oldText = old.text
                val newText = event.value.text

                // Default bullet prefix
                val bullet = "• "
                val bulletLine = "\n$bullet"
                var lastLine = newText.substringAfterLast(bullet)

                // Helper function to apply result
                fun update(newStr: String) {
                    lastLine = newStr.substringAfterLast(bullet)
                    _uiState.update {
                        it.copy(
                            profileHeaderData = it.profileHeaderData.copy(
                                highlightFieldValue = TextFieldValue(
                                    text = newStr,
                                    selection = TextRange(newStr.length)
                                ),
                                highlightsRemainingChars = 25 - lastLine.graphemeCount()
                            ),
                        )
                    }
                }


                // ---------------------------------------------------------
                // Enforce per-line limit (25 chars each bullet)
                // ---------------------------------------------------------
                if (lastLine.graphemeCount() > 25 && !(newText.length < oldText.length) && !newText.endsWith(
                        "\n"
                    )
                ) return

                // Restore single bullet when completely cleared
                if (newText.isEmpty()) {
                    update(bullet)
                    return
                }

                // Guard: prevent state "•" (missing trailing space)
                if (newText == "•" && !oldText.contains("\n")) {
                    update(bullet)
                    return
                }

                // Detect "Enter" → add a new bullet
                if (newText.endsWith("\n")
//                    && !oldText.endsWith(bulletLine)
                ) {
                    val maxBullets = if (websiteUrl.isNullOrEmpty()) 5 else 4
                    val count = oldText.count { it == '•' }
                    if (count >= maxBullets) {
                        update(oldText)   // restore previous, prevent bullet overflow
                        return
                    }

                    update(newText + bullet)
                    return
                }

                // delete empty bullet
                if (
                    oldText.endsWith(bulletLine) &&
                    newText.length < oldText.length
                ) {
                    val trimmed = oldText.removeSuffix(bulletLine)
                    update(trimmed)
                    return
                }

                // Normal typing or deletion
                update(newText)
            }

            is UserProfileEvent.OnWebsiteUrlChanged -> {
                _uiState.update {
                    it.copy(
                        profileHeaderData = _uiState.value.profileHeaderData.copy(
                            websiteUrl = event.value
                        )
                    )
                }
            }

            is UserProfileEvent.OnApplyBirthday -> {
                _uiState.update {
                    it.copy(
                        identificationData = _uiState.value.identificationData.copy(
                            birthday = event.value
                        )
                    )
                }
            }

            is UserProfileEvent.OnNameChanged -> {

                _uiState.update {
                    it.copy(
                        identificationData = _uiState.value.identificationData.copy(
                            name = event.value
                        )
                    )
                }

            }

            is UserProfileEvent.ToggleProfileHeaderDialog -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                        showEditProfileHeaderDialog = event.show,
                            profileHeaderData = uiState.value.magneticsData.profileHeaderData
                        )
                    }
//                    _navigationEvents.send(UserProfileNavigationEvent.NavigateToProfileHeaderDialog)
                }
            }

            is UserProfileEvent.ToggleMultipleSelectionsDialog -> {
                val selectedIds = event.selectedIds
                val merged = event.titlesData.map { item ->
                    item.copy(isSelected = selectedIds.contains(item.id) == true)
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
                val dialogHeader = arrayListOf("Description", "Joyer Status", state.joyerStatus?:"")
                if (state.subTitle?.id.isNullOrEmpty()) {
                    dialogHeader.add(state.title?.name ?: "")
                } else {
                    dialogHeader.add(state.title?.name ?: "")
                    dialogHeader.add(state.subTitle.name ?: "")
                }

                _uiState.update {
                    it.copy(
                        isSubTitleMode = false,
                        showDescriptionDialog = event.show,
                        dialogHeader = dialogHeader,
                        titlesData = uiState.value.titles,
                    )
                }
//                viewModelScope.launch {
//                    _navigationEvents.send(UserProfileNavigationEvent.NavigateToDescriptionDialog(uiState.value.titles))
//                }
            }

            is UserProfileEvent.ToggleIdentificationDialog -> {
                val state = _uiState.value
                val uiStateMagnetics = uiStateMagnetics.value
                _uiState.update {
                    it.copy(
                        showIdentificationDialog = event.show,
                        identificationData = state.magneticsData.identificationData
                            ?: IdentificationData()
                    )
                }
            }

            is UserProfileEvent.ToggleMentionJoyersDialog -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                        showMentionJoyersDialog = event.show,
                        )
                    }
//                    _navigationEvents.send(UserProfileNavigationEvent.NavigateToMentionJoyersDialog)
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
                val selectedIds = uiState.value.identificationData.language?.associateBy({
                    it.language?.id ?: ""
                }, { it.language?.level ?: "" })
                val merged = uiState.value.languageList.map { item ->
                    val newLevel = selectedIds?.get(item.id)
                    item.copy(
                        isSelected = selectedIds?.contains(item.id) == true,
                        level = newLevel ?: ""
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
                val selectedIds = event.value.associateBy({ it.id }, { it.level })
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

                _uiState.update {
                    it.copy(
                        showLanguagesDialog = false,
                        identificationData = _uiState.value.identificationData.copy(language = selectedMeta)
                    )
                }

            }

            is UserProfileEvent.BackgroundPicturePathChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uploadPictureServer(2, event.value)
                }
            }

            is UserProfileEvent.ProfilePicturePathChanged -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uploadPictureServer(1, event.value)
                }
            }

            is UserProfileEvent.UpdateProfileHeaderData -> {
                _uiState.update { state ->

                    state.copy(
                        profileHeaderData = event.profileHeaderData
                    )

                    /*    var overviewText = ""
                        var highlightsText = "• "
                        var selectedTab = ""
                        val prefix = "Highlights\n"
                        if (state.magneticsData.profileHeaderData?.bio?.startsWith(prefix) == true) {
                            highlightsText = state.magneticsData.profileHeaderData.bio.removePrefix(prefix)
                            if (highlightsText.isEmpty()) highlightsText = "• "
                            selectedTab = "highlights"
                        } else {
                            overviewText = state.bio
                            selectedTab = "overview"
                        }

                        state.copy(
                            profileHeaderData = event.profileHeaderData.copy(
                                selectedTab = selectedTab,
                                overviewFieldValue = TextFieldValue(
                                    text = overviewText,
                                    selection = TextRange(overviewText.length)
                                ),
                                highlightFieldValue = TextFieldValue(
                                    text = highlightsText,
                                    selection = TextRange(highlightsText.length)
                                ),
                            )
                        )*/
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

            is UserProfileEvent.OnApplyMentionedJoyers -> {
                viewModelScope.launch {
                    val profileHeaderData = uiState.value.profileHeaderData
                    val selectedUsers =
                        event.selectedUserList.joinToString(
                            separator = " @",
                            postfix = " "
                        ) { it.username ?: "" }
                    if (profileHeaderData.selectedTab.equals("overview")) {
                        val bio = profileHeaderData.overviewFieldValue.text + selectedUsers
                        onEvent(
                            UserProfileEvent.OnOverviewChanged(
                                TextFieldValue(
                                    text = bio,
                                    selection = TextRange(bio.length)
                                )
                            )
                        )
                    } else {
                        val bio = profileHeaderData.highlightFieldValue.text + selectedUsers
                        onEvent(
                            UserProfileEvent.OnHighlightChanged(
                                TextFieldValue(
                                    text = bio,
                                    selection = TextRange(bio.length)
                                )
                            )
                        )
                    }
                    _uiState.update { state ->
                        state.copy(
                            showMentionJoyersDialog = false
                        )
                    }
                }
            }
        }
    }


    private suspend fun uploadUserProfileData(requestDto: UserProfileGraphRequestDto) {
        _uiState.update { it.copy(isLoading = true) }
        val result = uploadUserProfileUseCase(requestDto)
        result.fold(
            onSuccess = { response ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        username = response.username ?: "",
                        fullname = ((response.firstName ?: "") + " " + (response.lastName
                            ?: "")).trim(),
                        location = response.location,
                        joyerLocation = response.joyerLocation,
                        profilePicture = response.profilePicture,
                        backgroundPicture = response.backgroundPicture,
                        bio = response.bio,
                        websiteUrl = response.websiteUrl,
                        likes = response.likesCount,
                        following = response.followingCount,
                        followers = response.followersCount,
                        joyerStatus = response.joyerStatus,
                        birthday = response.birthDate,
                        gender = response.gender,
                        relationship = response.relationship,
                        education = response.education,
//                            children = response.ch?.name?: "",
                        politicalIdeology = response.politicalIdeology,
                        titleName = response.title?.name,
                        subTitleName = response.subTitle?.name,
                        title = response.title,
                        subTitle = response.subTitle,
                        areaOfInterest = response.interests,
                        languages = response.languages,
                        joySince = response.joySince,
                        joySinceDuration = response.joySinceDuration,
                        qrCode = response.qrCode,
                        nationality = response.nationality,
                        ethnicity = response.ethnicity,
                        faith = response.faith,
                        educationName = response.education?.name,
                    )
                }
                _navigationEvents.send(UserProfileNavigationEvent.NavigateToUserProfile)
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

    private suspend fun uploadPictureServer(imageId: Int, imagePath: String) {
        val result = uploadPictureServerUseCase(imageId, imagePath)
        result.fold(
            onSuccess = { response ->
                if (imageId == 1) {
                    _uiState.update {
                        it.copy(
                            profileHeaderData = uiState.value.profileHeaderData.copy(
                                profilePicture = response.data?.profilePicture ?: "",
                            )
                        )
                    }
                } else {
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
                        errorMessage = error.message
                    )
                }
            }
        )
    }

    private suspend fun getUserProfileData() {
        val state = _uiState.value

        val result = getUserProfileUseCase()
        result.fold(
            onSuccess = { response ->
                _uiState.update {
                    it.copy(
                        errorMessage = null,
                        username = response.username ?: "",
                        fullname = ((response.firstName ?: "") + " " + (response.lastName
                            ?: "")).trim(),
                        location = response.location,
                        joyerLocation = response.joyerLocation,
                        profilePicture = response.profilePicture,
                        backgroundPicture = response.backgroundPicture,
                        bio = response.bio,
                        websiteUrl = response.websiteUrl,
                        likes = response.likesCount,
                        following = response.followingCount,
                        followers = response.followersCount,
                        joyerStatus = response.joyerStatus,
                        birthday = response.birthDate,
                        gender = response.gender,
                        relationship = response.relationship,
                        education = response.education,
//                            children = response.ch?.name?: "",
                        politicalIdeology = response.politicalIdeology,
                        titleName = response.title?.name,
                        subTitleName = response.subTitle?.name,
                        title = response.title,
                        subTitle = response.subTitle,
                        areaOfInterest = response.interests,
                        languages = response.languages,
                        joySince = response.joySince,
                        joySinceDuration = response.joySinceDuration,
                        qrCode = response.qrCode,
                        nationality = response.nationality,
                        ethnicity = response.ethnicity,
                        faith = response.faith,
                        educationName = response.education?.name,
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        errorMessage = error.message
                    )
                }
            }
        )
    }

    private suspend fun getEditMagneticsUserListData() {

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
                        errorMessage = error.message
                    )
                }
            }
        )
    }

    private suspend fun loadTitles() {

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
                        errorMessage = error.message
                    )
                }
            }
        )
    }

    private suspend fun loadCountryList() {

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

    private suspend fun loadEducationList() {
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

    private suspend fun loadEthinicityList() {

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

    private suspend fun loadFaithReligionList() {

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

    private suspend fun loadInterestList() {

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

    private suspend fun loadPoliticalIdeologyList() {

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

    private suspend fun loadRelationShipList() {

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

    private suspend fun loadLanguageList() {

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