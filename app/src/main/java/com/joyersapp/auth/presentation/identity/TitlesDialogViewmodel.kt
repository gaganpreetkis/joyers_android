package com.joyersapp.auth.presentation.identity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.data.remote.dto.ProfileLanguagesData
import com.joyersapp.feature.profile.data.remote.dto.ProfileMeta
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import com.joyersapp.feature.profile.presentation.dialogs.DialogMode
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


//class DescriptionViewModelFactory(
//    private val initialItems: List<ProfileTitlesData>,
//    private val selectedTitle: ProfileTitlesData?,
//    private val selectedSubTitle: ProfileTitlesData?,
//
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return DescriptionViewModel(initialItems, selectedTitle, selectedSubTitle) as T
//    }
//}

//@AssistedFactory
//interface CustomViewModelFactory {
//    fun create(
//        initialItems: List<ProfileTitlesData>,
//        selectedTitle: ProfileTitlesData?,
//        selectedSubTitle: ProfileTitlesData?
//        ): DescriptionViewModel
//}
sealed class TitlesDialogMode {
    //    data object JoyerStatus : DialogMode()
    data object Title : TitlesDialogMode()
    data object SubTitle : TitlesDialogMode()
}

data class TitlesUiState(

    val rootItems: List<ProfileTitlesData> = emptyList(),
    val currentItems: List<ProfileTitlesData> = emptyList(),
    val currentUiMode: TitlesDialogMode = TitlesDialogMode.Title,
    val preSelectedTitle: ProfileTitlesData? = null,
    val selectedTitle: ProfileTitlesData? = null,
    val localTitle: ProfileTitlesData? = null,
    val preSelectedSubTitle: ProfileTitlesData? = null,
    val selectedSubTitle: ProfileTitlesData? = null,
    val firstClick: Boolean = true,
    val searchQuery: String = ""

) {

    val filteredItems: List<ProfileTitlesData>
        get() =
            if (searchQuery.isBlank()) currentItems
            else currentItems.filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true
            }

    val reorderedItems: List<ProfileTitlesData>
        get() {
            val selected = filteredItems.filter { it.isSelected }
            val unselected = filteredItems.filterNot { it.isSelected }
            return selected + unselected
        }

    val clarificationItems: List<ProfileTitlesData>
        get() = filteredItems.filter { it.description != null }

    val showBackButton: Boolean
        get() = currentUiMode is TitlesDialogMode.SubTitle


    val headers: List<String>
        get() =  when (currentUiMode) {
            TitlesDialogMode.Title -> {
                arrayListOf("Description", "Joyer Status", "Classic")
            }

            TitlesDialogMode.SubTitle -> {
                arrayListOf("Description", "Joyer Status", "Classic", localTitle?.name)
            }
        }


    val isApplyEnabled: Boolean
        get() = when (currentUiMode) {
            TitlesDialogMode.Title -> {
                rootItems.firstOrNull{ it.isSelected }?.id.equals(preSelectedTitle?.id) == false
            }

            TitlesDialogMode.SubTitle -> {
                currentItems.any(){ it.isSelected } && currentItems.firstOrNull{ it.isSelected }?.id?.equals(preSelectedSubTitle?.id) == false
            }
        }
}


sealed class TitlesEvent {

    data class InitData(val items: List<ProfileTitlesData>, val selectedTitle: ProfileTitlesData?, val selectedSubTitle: ProfileTitlesData?) : TitlesEvent()
    data object OnBackButton : TitlesEvent()
    data class OnItemClicked(val item: ProfileTitlesData) : TitlesEvent()
    data class OnUserSelectionToggled(val item: ProfileTitlesData?) : TitlesEvent()
    data class OnSearchQueryChanged(val query: String) : TitlesEvent()

    data object OnApply : TitlesEvent()
    data object OnClearData : TitlesEvent()


}

sealed class TitlesNavEvent {
    class OnApply(val Title: ProfileTitlesData?, val SubTitle: ProfileTitlesData?) : TitlesNavEvent()

}

//@HiltViewModel(assistedFactory = CustomViewModelFactory::class)
@HiltViewModel
class TitlesDialogViewmodel @Inject constructor(
//    @Assisted private val initialItems: List<ProfileTitlesData>,
//    @Assisted private val selectedTitle: ProfileTitlesData?,
//    @Assisted private val selectedSubTitle: ProfileTitlesData?,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TitlesUiState() )
    private val _navigationEvents = Channel<TitlesNavEvent>(Channel.BUFFERED)

    val uiState: StateFlow<TitlesUiState> = _uiState.asStateFlow()
    val navigationEvents = _navigationEvents.receiveAsFlow()

//    init {
//        _uiState.update {
//            it.copy(
//                rootItems = initialItems,
//                currentItems = initialItems,
//                selectedTitle = selectedTitle,
//                selectedSubTitle = selectedSubTitle
//            )
//        }

//        savedStateHandle.keys()
//        savedStateHandle
//            .getStateFlow(
//                key = "description_titles_list",
//                initialValue = emptyList<ProfileTitlesData>()
//            )
//            .onEach { list ->
//                _uiState.update {
//                    it.copy(
//                        rootItems = list,
//                        currentItems = list
//                    )
//                }
//            }
//            .launchIn(viewModelScope)
//    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { TitlesUiState() }
    }


    fun onEvent(event: TitlesEvent) {
        when (event) {
            is TitlesEvent.OnApply -> {
                val state = uiState.value

                val selectedTitle = if (state.currentUiMode is TitlesDialogMode.SubTitle) {
                    state.localTitle
                } else state.rootItems.firstOrNull{ it.isSelected }
                val selectedSubTitle = if (state.currentUiMode is TitlesDialogMode.SubTitle) {
                    state.currentItems.firstOrNull{ it.isSelected }
                } else null
                viewModelScope.launch {
                    _navigationEvents.send(
                        TitlesNavEvent.OnApply(
                            Title = selectedTitle,
                            SubTitle = selectedSubTitle,
                        )
                    )
                    onCleared()
                }
            }
            is TitlesEvent.OnClearData -> {
                onCleared()
            }
            is TitlesEvent.InitData -> {
                if (event.selectedSubTitle?.id.isNullOrEmpty()) {
                    _uiState.update {
                        val items = event.items.map { title ->
                            if (title.id?.equals(event.selectedTitle?.id) == true) {
                                title.copy(
                                    isSelected = true,
                                )
                            } else title.copy(isSelected = false)
                        }
                        it.copy(
                            rootItems = items,
                            currentItems =  items,
                            preSelectedTitle = event.selectedTitle,
                            selectedTitle = event.selectedTitle,
                            preSelectedSubTitle = event.selectedSubTitle,
                            selectedSubTitle = event.selectedSubTitle,
                        )
                    }
                } else {
                    _uiState.update {
                        var subItems = emptyList<ProfileTitlesData>()
                        val items = event.items.map { title ->
                            if (title.id?.equals(event.selectedTitle?.id) == true) {
                                subItems = title.selections?.map { subtitle ->
                                    subtitle.copy(
                                        isSelected = (event.selectedSubTitle.id.equals(subtitle.id) == true)
                                    )
                                }?:emptyList()
                                title.copy(
                                    isSelected = true,
                                    selections = subItems
                                )
                            } else title.copy(isSelected = false)
                        }
                        it.copy(
                            rootItems = items,
                            currentItems = subItems,
                            searchQuery = "",
                            preSelectedTitle = event.selectedTitle,
                            selectedTitle = event.selectedTitle,
                            preSelectedSubTitle = event.selectedSubTitle,
                            selectedSubTitle = event.selectedSubTitle,
                            currentUiMode = TitlesDialogMode.SubTitle,
                        )
                    }
                }

            }
            is TitlesEvent.OnBackButton -> {
                _uiState.update {
                    it.copy(
                        currentItems = it.rootItems,
                        currentUiMode = TitlesDialogMode.Title,
                        localTitle = null,
//                        selectedTitle = null,
//                        selectedSubTitle = null
                    )
                }
            }
            is TitlesEvent.OnSearchQueryChanged -> {
                _uiState.update {
                    it.copy(searchQuery = event.query)
                }
            }
            is TitlesEvent.OnItemClicked -> {
                if (!event.item.selections.isNullOrEmpty()) {
                    // 👉 Navigate to child list
                    _uiState.update {
                        it.copy(
                            firstClick = false,
                            currentItems = event.item.selections?:emptyList(),
                            currentUiMode = TitlesDialogMode.SubTitle,
                            searchQuery = "",
                            localTitle = event.item
                        )
                    }
                } else {
                    onEvent(TitlesEvent.OnUserSelectionToggled(event.item))
                }
            }
            is TitlesEvent.OnUserSelectionToggled -> {
                _uiState.update { state ->
                    when (state.currentUiMode) {
                        is TitlesDialogMode.Title -> {
                            val updated = state.rootItems.map {
                                it.copy(
                                    isSelected = if (event.item?.id?.equals(it.id) == true) !it.isSelected else false
                                )
                            }
                            state.copy(
                                firstClick = false,
                                searchQuery = "",
                                rootItems = updated,
                                currentItems = updated,
                            )
                        }
                        is TitlesDialogMode.SubTitle -> {
                            val updated = state.currentItems.map { item ->
                                item.copy(
                                    isSelected = if (item.id == event.item?.id) !item.isSelected else false
                                )
                            }
                            state.copy(
                                firstClick = false,
                                searchQuery = "",
                                currentItems = updated
                            )
                        }
                    }
                }
            }
        }
    }
}