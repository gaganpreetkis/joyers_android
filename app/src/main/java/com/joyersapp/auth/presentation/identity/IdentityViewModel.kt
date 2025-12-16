package com.joyersapp.auth.presentation.identity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.auth.data.remote.dto.identity.SubTitle
import com.joyersapp.auth.data.remote.dto.identity.Title
import com.joyersapp.auth.domain.usecase.FetchTitlesUseCase
import com.joyersapp.common_widgets.DialogState
import com.joyersapp.utils.UiText.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class IdentityViewModel @Inject constructor(
    private val fetchTitlesUseCase: FetchTitlesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TitlesDialogUiState())
    val uiState: StateFlow<TitlesDialogUiState> = _uiState
    private val _events = MutableSharedFlow<TitlesDialogEvent>()
    val events = _events

//    init {
//        loadTitles()
//    }

    fun onEvent(event: TitleEvent) {
        when (event) {
            TitleEvent.Load,
            TitleEvent.Retry -> {
                loadTitles()
            }

            is TitleEvent.InitTitleSelection -> {
                val title = event.title
                var selectedSubTitleId: String? = null
                var selectedSubTitleName: String? = null
                if (title?.subTitles != null && title.subTitles.isNotEmpty()) {
                    selectedSubTitleId = title.subTitles.first().id
                    selectedSubTitleName = title.subTitles.first().name
                }
                _uiState.update {
                    it.copy(
                        selectedTitleId = title?.id,
                        selectedTitleName = title?.name,
                        selectedSubTitleName = selectedSubTitleName,
                        selectedSubTitleId = selectedSubTitleId
                    )
                }
            }

            is TitleEvent.TitleClicked -> {
                val title = event.title!!
                if (title.subTitles.size > 0) {
                    _uiState.update {
                        it.copy(
                            selectedTitleId = title.id,
                            selectedTitleName = title.name,
                            selectedSubTitleName = null,
                            selectedSubTitleId = null,
                            dialogState = DialogState.Subtitles(title.name, title.subTitles)
                        )
                    }
                    onEvent(TitleEvent.ShowSubtitles(title.subTitles))
                } else {
                    if (title.id == _uiState.value.selectedTitleId) {
                        _uiState.update {
                            it.copy(
                                selectedTitleId = null,
                                selectedTitleName = null,
                                selectedSubTitleName = null,
                                selectedSubTitleId = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                selectedTitleId = title.id,
                                selectedTitleName = title.name,
                                selectedSubTitleName = null,
                                selectedSubTitleId = null
                            )
                        }
                    }
                    //recalcTitles(_uiState.value,_uiState.value.titles)
                }
                /*val title = event.title!!
                if (title.subTitles.size > 0) {
                    _uiState.update {
                        it.copy(
                            selectedTitleId = title.id,
                            selectedTitleName = title.name,
                            selectedSubTitleName = null,
                            selectedSubTitleId = null,
                            dialogState = DialogState.Subtitles(title.name, title.subTitles)
                        )
                    }
                } else {
                    if (title.id == _uiState.value.selectedTitleId) {
                        _uiState.update {
                            it.copy(
                                selectedTitleId = null,
                                selectedTitleName = null,
                                selectedSubTitleName = null,
                                selectedSubTitleId = null
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                selectedTitleId = title.id,
                                selectedTitleName = title.name,
                                selectedSubTitleName = null,
                                selectedSubTitleId = null
                            )
                        }
                    }
                }*/
            }

            is TitleEvent.SubtitleClicked -> {
                val subTitle = event.subtitle!!
                if (subTitle.id == _uiState.value.selectedSubTitleId) {
                    _uiState.update {
                        it.copy(
                            selectedSubTitleId = null,
                            selectedSubTitleName = null
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            selectedSubTitleId = subTitle.id,
                            selectedSubTitleName = subTitle.name,
                        )
                    }
                }
                /*_uiState.update { old ->
                    val newState = old.copy(
                        selectedTitleId = event.subtitleId, // or separate selectedSubtitleId if you prefer
//                        showApply = true
                    )
                    recalc(newState)
                }*/
            }

            is TitleEvent.SelectionClicked -> {
                // when user taps a sub-option within a title
                _uiState.update {
                    it.copy(
                        selectedTitleId = event.titleId,
                        selectedSubTitleId = event.selectionId
                    )
                }
            }

            TitleEvent.ConfirmSelection -> {
                confirmSelection()
            }

            TitleEvent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }

            TitleEvent.NavigateToAllTitles -> {
                _uiState.update { it.copy(
                    errorMessage = null,
                    dialogState = DialogState.Titles(_uiState.value.titles),
                    selectedSubTitleId = null,
                    selectedSubTitleName = null
                ) }
            }



            is TitleEvent.SearchQueryChanged -> {
                _uiState.update { old ->
                    val newState = old.copy(searchQuery = event.query)
                    recalc(newState)
                }
            }

            is TitleEvent.ExpandClassifications -> {
                _uiState.update { old ->
                    old.copy(isExpanded = !event.isExpanded)
                }
            }

            is TitleEvent.ShowTitles -> {
                _uiState.update {
                    recalc(
                        it.copy(
                            dialogState = DialogState.Titles(event.items),
                            selectedTitleId = null, // keep selection if needed
                        )
                    )
                }
            }

            is TitleEvent.ShowSubtitles -> {
                _uiState.update {
                    recalc(
                        it.copy(
                            dialogState = DialogState.Subtitles(parentTitle = _uiState.value.selectedTitleName ,items = event.items),
                            selectedTitleId = it.selectedTitleId,
                        )
                    )
                }
            }

            TitleEvent.ApplyClicked -> {
                confirmSelection()
            }
        }
    }

    private fun recalc(state: TitlesDialogUiState): TitlesDialogUiState {
        return when (val ds = state.dialogState) {
            is DialogState.Titles -> recalcTitles(state, ds.items)
            is DialogState.Subtitles -> recalcSubtitles(state, ds.items)
        }
    }

    private fun recalcTitles(
        state: TitlesDialogUiState,
        items: List<Title>
    ): TitlesDialogUiState {
        val query = state.searchQuery.trim()
        val filteredTitles =
            if (query.isEmpty()) {
                items
            } else {
                items.filter { it.name?.contains(query, ignoreCase = true) == true }
            }

        val classificationTitles =
            if (query.isEmpty()) {
                items.filter { !it.description.isNullOrEmpty() }
            } else {
                items.filter {
                    !it.description.isNullOrEmpty() &&
                            it.name?.contains(query, ignoreCase = true) == true
                }
            }

        val reordered =
            if (!state.selectedTitleId.isNullOrEmpty()) {
                val selected = filteredTitles.find { it.id == state.selectedTitleId }
                if (selected != null && (selected.subTitles.isEmpty())) {
                    listOf(selected) + filteredTitles.filter { it.id != state.selectedTitleId }
                } else {
                    filteredTitles
                }
            } else {
                filteredTitles
            }

        return state.copy(
            reorderedTitles = reordered,
            classificationTitles = classificationTitles,
            reorderedSubtitles = emptyList(),
            classificationSubtitles = emptyList(),
            showNoResults = reordered.isEmpty()
        )
    }

    private fun recalcSubtitles(
        state: TitlesDialogUiState,
        items: List<SubTitle>
    ): TitlesDialogUiState {
        val query = state.searchQuery.trim()
        val filteredSubtitles =
            if (query.isEmpty()) {
                items
            } else {
                items.filter { it.name?.contains(query, ignoreCase = true) == true }
            }

        val classificationSubtitles =
            if (query.isEmpty()) {
                items.filter { !it.description.isNullOrEmpty() }
            } else {
                items.filter {
                    !it.description.isNullOrEmpty() &&
                            it.name?.contains(query, ignoreCase = true) == true
                }
            }

        val reordered =
            if (!state.selectedSubTitleId.isNullOrEmpty()) {
                val selected = filteredSubtitles.find { it.id == state.selectedSubTitleId }
                if (selected != null/* && (selected.subtitles == null || selected.subtitles.isEmpty())*/) {
                    listOf(selected) + filteredSubtitles.filter { it.id != state.selectedSubTitleId }
                } else {
                    filteredSubtitles
                }
            } else {
                filteredSubtitles
            }

        return state.copy(
            reorderedSubtitles = reordered,
            classificationSubtitles = classificationSubtitles,
            reorderedTitles = emptyList(),
            classificationTitles = emptyList(),
            showNoResults = reordered.isEmpty()
        )
    }

    private fun loadTitles(){
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result =
                fetchTitlesUseCase()

            result.fold(
                onSuccess = { titles ->
                    _uiState.update { old ->
                        old.copy(
                            isLoading = false,
                            titles = titles,
                            dialogState = DialogState.Titles(titles),
                            errorMessage = null
                        )
                    }
                    recalcTitles(_uiState.value, titles)
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

    private fun confirmSelection() {
        val state = _uiState.value
        viewModelScope.launch {
            if (state.dialogState is DialogState.Subtitles) {
                _uiState.update {
                    it.copy(
                        subTitles = state.dialogState.items
                    )
                }
            }

            _events.emit(
                TitlesDialogEvent.SelectionConfirmed(
                    titleId = state.selectedTitleId,
                    titleName = state.selectedTitleName,
                    subTitleId = state.selectedSubTitleId,
                    subTitleName = state.selectedSubTitleName,
                )
            )
        }
    }
}
