package com.joyersapp.auth.presentation.identity

import com.joyersapp.auth.data.remote.dto.identity.SubTitle
import com.joyersapp.auth.data.remote.dto.identity.Title

sealed class TitleEvent {
    data object Load : TitleEvent()
    data object Retry : TitleEvent()
    data class SelectionClicked(val titleId: String, val selectionId: String) : TitleEvent()
    data object ConfirmSelection : TitleEvent()
    data object NavigateToAllTitles : TitleEvent()
    data object ClearError : TitleEvent()

    data class SearchQueryChanged(val query: String) : TitleEvent()

    data class ExpandClassifications(val isExpanded: Boolean) : TitleEvent()
    data class TitleClicked(val title : Title?) : TitleEvent()
    data class SubtitleClicked(val subtitle: SubTitle?) : TitleEvent()
    data class InitTitleSelection(val title : Title?) : TitleEvent()

    data class ShowTitles(val items: List<Title>) : TitleEvent()
    data class ShowSubtitles(val items: List<SubTitle>) : TitleEvent()

    data object ApplyClicked : TitleEvent()
}