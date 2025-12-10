package com.joyersapp.auth.presentation.identity

import com.joyersapp.auth.data.remote.dto.identity.Title
import com.joyersapp.auth.data.remote.dto.identity.SubTitle
import com.joyersapp.common_widgets.DialogState

data class TitlesDialogUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val dialogState: DialogState = DialogState.Titles(emptyList()),
    val titles: List<Title> = emptyList(),

    val selectedTitleId: String? = null,
    val selectedSubTitleId: String? = null,

    val selectedTitleName: String? = null,
    val selectedSubTitleName: String? = null,

    val searchQuery: String = "",
    val isExpanded: Boolean = false,

    val reorderedTitles: List<Title> = emptyList(),
    val classificationTitles: List<Title> = emptyList(),

    val reorderedSubtitles: List<SubTitle> = emptyList(),
    val classificationSubtitles: List<SubTitle> = emptyList(),


    val showNoResults: Boolean = false
) {
    val showApply = when(dialogState) {
        is DialogState.Titles -> { !selectedTitleId.isNullOrEmpty() && reorderedTitles.find { it.id == selectedTitleId } != null }
        is DialogState.Subtitles -> { !selectedSubTitleId.isNullOrEmpty() && reorderedSubtitles.find { it.id == selectedSubTitleId } != null }
    }
}