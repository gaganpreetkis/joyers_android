package com.joyersapp.feature.profile.presentation.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.feature.profile.data.remote.dto.Languages
import com.joyersapp.feature.profile.data.remote.dto.ProfileLanguagesData
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LanguagesDialogUiState(

    val rootItems: List<ProfileLanguagesData> = emptyList(),
    val signLanguages: List<ProfileLanguagesData> = emptyList(),
    val currentItems: List<ProfileLanguagesData> = emptyList(),

    val searchQuery: String = "",

    val isMultiSelectEnabled: Boolean = true,
    val recentSelectedItemId: String = "",

    ) {

    val filteredItems: List<ProfileLanguagesData>
        get() =
            if (searchQuery.isBlank()) currentItems
            else currentItems.filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true
            }

    val reorderedItems: List<ProfileLanguagesData>
        get() {
            val selected = filteredItems.filter { it.isSelected }
            val unselected = filteredItems.filterNot { it.isSelected }
            return selected + unselected
        }

    val showBackButton: Boolean
        get() = currentItems != rootItems

    val isApplyEnabled: Boolean
        get() = rootItems.any { it.isSelected }

    val selectedLanguages: List<ProfileLanguagesData>
        get() = rootItems.filter { it.isSelected }
    val selectedSignLanguages: List<ProfileLanguagesData>
        get() = signLanguages.filter { it.isSelected }
}

enum class LanguageLevel(val label: String) {
    BASIC("Basic"),
    GOOD("Good"),
    VERY_GOOD("Very Good"),
    EXCELLENT("Excellent"),
}

sealed class LanguagesDialogEvent {

    data class InitData(val items: List<ProfileLanguagesData>,val selectedLanguages: List<Languages>?,val selectedSignLanguages: List<Languages>?) : LanguagesDialogEvent()

    data class OnSearchQueryChanged(val query: String) : LanguagesDialogEvent()

    data class OnTitleClicked(val item: ProfileLanguagesData) : LanguagesDialogEvent()

    data class OnLanguageLevelSelected(
        val id: String,
        val level: String
    ) : LanguagesDialogEvent()

    data object OnBack : LanguagesDialogEvent()

    data object OnApply : LanguagesDialogEvent()
    data object OnShowSignLanguages : LanguagesDialogEvent()
}
sealed class LanguagesDialogNavEvent {
    data class OnApply(val langs: List<ProfileLanguagesData>?, val signLangs: List<ProfileLanguagesData>?) : LanguagesDialogNavEvent()
}

@HiltViewModel
class LanguagesDialogViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LanguagesDialogUiState())
    val uiState: StateFlow<LanguagesDialogUiState> = _uiState.asStateFlow()

    private val _navigationEvents =
        Channel<LanguagesDialogNavEvent>(Channel.BUFFERED)

    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onEvent(event: LanguagesDialogEvent) {
        when (event) {

            is LanguagesDialogEvent.InitData -> {
                val rootListWithSelections =
                    if (event.selectedLanguages.isNullOrEmpty()) {
                        event.items
                    } else {
                        event.items.map { item ->
                            val isSelected =
                                item.id in event.selectedLanguages.map { it.language?.id }
                            item.copy(
                                isSelected = isSelected,
                                level = if (isSelected) event.selectedLanguages.find {
                                    it.language?.id.equals(
                                        item.id
                                    )
                                }?.language?.level else "Basic"
                            )
                        }
                    }

                val signLangList = event.items.firstOrNull { it.id.equals("72338abe-4687-487f-9515-c10d2a1be8ef") }?.selections?: emptyList()
                val signLanguagesWithSelections =
                    if (event.selectedSignLanguages.isNullOrEmpty()) {
                        signLangList
                    } else {
                        signLangList.map { item ->
                            val isSelected =
                                item.id in event.selectedSignLanguages.map { it.sublanguage?.id }
                            item.copy(
                                isSelected = isSelected,
                                level = if (isSelected) event.selectedSignLanguages.find {
                                    it.sublanguage?.id.equals(
                                        item.id
                                    )
                                }?.sublanguage?.level else "Basic"
                            )
                        }
                    }

                _uiState.update {
                    it.copy(
                        rootItems = rootListWithSelections,
                        currentItems = rootListWithSelections,
                        signLanguages = signLanguagesWithSelections,
                    )
                }
            }

            is LanguagesDialogEvent.OnShowSignLanguages -> {
                _uiState.update {
                    it.copy(
                        currentItems = it.signLanguages,
                        )
                }
            }

            is LanguagesDialogEvent.OnSearchQueryChanged -> {
                _uiState.update {
                    it.copy(searchQuery = event.query)
                }
            }

            is LanguagesDialogEvent.OnTitleClicked -> {

                val state = _uiState.value

                // Has sub languages
                if (!event.item.selections.isNullOrEmpty()) {
                    _uiState.update {
                        it.copy(
                            currentItems = event.item.selections!!,
                            searchQuery = ""
                        )
                    }
                    return
                }

                if (state.showBackButton) {
                    // sign languages
                    // Toggle selection
                    _uiState.update { state ->
                        val updated = state.signLanguages.map { item ->
                            if (item.id == event.item.id) {
//                            if (state.isMultiSelectEnabled) {
                                item.copy(isSelected = !item.isSelected, isSelectionMode = !item.isSelected)
//                            } else {
//                                item.copy(isSelected = true)
//                            }
                            } else {
                                item.copy(isSelectionMode = false)
//                            if (state.isMultiSelectEnabled) item
//                            else item.copy(isSelected = false)
                            }
                        }

                        state.copy(
                            signLanguages = updated,
                            currentItems = updated,
                            recentSelectedItemId = event.item.id ?: ""
                        )
                    }
                } else {
                    // all languages
                    // Toggle selection
                    _uiState.update { state ->
                        val updated = state.rootItems.map { item ->
                            if (item.id == event.item.id) {
//                            if (state.isMultiSelectEnabled) {
                                item.copy(isSelected = !item.isSelected, isSelectionMode = !item.isSelected)
//                            } else {
//                                item.copy(isSelected = true)
//                            }
                            } else {
                                item.copy(isSelectionMode = false)
//                            if (state.isMultiSelectEnabled) item
//                            else item.copy(isSelected = false)
                            }
                        }

                        state.copy(
                            rootItems = updated,
                            currentItems = updated,
                            recentSelectedItemId = event.item.id ?: ""
                        )
                    }
                }


            }

            is LanguagesDialogEvent.OnLanguageLevelSelected -> {
                _uiState.update { state ->

                    if (state.showBackButton) {
                        // sign languages
                        val updated = state.signLanguages.map { item ->
                            if (item.id?.contains(event.id) == true) {
                                item.copy(
                                    level = event.level,
                                    isSelectionMode = false
                                )
                            } else item
                        }

                        state.copy(
                            signLanguages = updated,
                            currentItems = updated
                        )
                    } else {
                        // all languages
                        val updated = state.rootItems.map { item ->
                            if (item.id?.contains(event.id) == true) {
                                item.copy(
                                    level = event.level,
                                    isSelectionMode = false
                                )
                            } else item
                        }

                        state.copy(
                            rootItems = updated,
                            currentItems = updated
                        )
                    }

                }
            }

            is LanguagesDialogEvent.OnBack -> {
                _uiState.update {
                    it.copy(
                        currentItems = it.rootItems,
                        searchQuery = ""
                    )
                }
            }

            is LanguagesDialogEvent.OnApply -> {
                val state = _uiState.value
                val langs = if (state.selectedLanguages.isNotEmpty()) state.selectedLanguages else null
                val signLangs = if (state.selectedSignLanguages.isNotEmpty()) state.selectedSignLanguages else null
                viewModelScope.launch {
                    _navigationEvents.send(
                        LanguagesDialogNavEvent.OnApply(
                            langs,
                            emptyList(),
                        )
                    )
                }
            }
        }
    }
}