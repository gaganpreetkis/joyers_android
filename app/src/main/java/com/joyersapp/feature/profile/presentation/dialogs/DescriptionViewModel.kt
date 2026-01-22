package com.joyersapp.feature.profile.presentation.dialogs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
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
 sealed class DialogMode {
//    data object JoyerStatus : DialogMode()
    data object Title : DialogMode()
    data object SubTitle : DialogMode()
}

data class DescriptionUiState(

    val rootItems: List<ProfileTitlesData> = emptyList(),
    val currentItems: List<ProfileTitlesData> = emptyList(),
    val currentUiMode: DialogMode = DialogMode.Title,
    val selectedTitle: ProfileTitlesData? = null,
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
        get() =  when (currentUiMode) {
            DialogMode.Title -> {
                filteredItems.sortedByDescending { it.id.equals(selectedTitle?.id) }
            }

            DialogMode.SubTitle -> {
                filteredItems.sortedByDescending { it.id.equals(selectedSubTitle?.id) }
            }
        }

    val clarificationItems: List<ProfileTitlesData>
        get() = filteredItems.filter { it.description != null }

    val showBackButton: Boolean
        get() = currentUiMode is DialogMode.SubTitle


    val headers: List<String>
        get() =  when (currentUiMode) {
            DialogMode.Title -> {
                arrayListOf("Description", "Joyer Status", "Classic")
            }

            DialogMode.SubTitle -> {
                arrayListOf("Description", "Joyer Status", "Classic", selectedTitle?.name)
            }
        }

    val selectedId: String
        get() =  when (currentUiMode) {
            DialogMode.Title -> {
                selectedTitle?.id?:""
            }

            DialogMode.SubTitle -> {
                selectedSubTitle?.id?:""
            }
        }


    val isApplyEnabled: Boolean
        get() = (currentItems.any { it.id.equals(selectedId) } && !firstClick)
}


sealed class DescriptionEvent {

    data class InitData(val items: List<ProfileTitlesData>, val selectedTitle: ProfileTitlesData?, val selectedSubTitle: ProfileTitlesData?) : DescriptionEvent()
    data object OnBackButton : DescriptionEvent()
    data class OnItemClicked(val item: ProfileTitlesData) : DescriptionEvent()
    data class OnUserSelectionToggled(val item: ProfileTitlesData?) : DescriptionEvent()
    data class OnSearchQueryChanged(val query: String) : DescriptionEvent()

    data object OnApply : DescriptionEvent()


}

sealed class DescriptionNavEvent {
    class OnApply(val Title: ProfileTitlesData?, val SubTitle: ProfileTitlesData?) : DescriptionNavEvent()

}

//@HiltViewModel(assistedFactory = CustomViewModelFactory::class)
@HiltViewModel
class DescriptionViewModel @Inject constructor(
//    @Assisted private val initialItems: List<ProfileTitlesData>,
//    @Assisted private val selectedTitle: ProfileTitlesData?,
//    @Assisted private val selectedSubTitle: ProfileTitlesData?,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DescriptionUiState() )
    private val _navigationEvents = Channel<DescriptionNavEvent>(Channel.BUFFERED)

    val uiState: StateFlow<DescriptionUiState> = _uiState.asStateFlow()
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


    fun onEvent(event: DescriptionEvent) {
        when (event) {
            is DescriptionEvent.OnApply -> {
                viewModelScope.launch {
                    _navigationEvents.send(
                        DescriptionNavEvent.OnApply(
                            Title = uiState.value.selectedTitle,
                            SubTitle = uiState.value.selectedSubTitle,
                        )
                    )
                }
            }
            is DescriptionEvent.InitData -> {
                if (event.selectedSubTitle?.id.isNullOrEmpty()) {
                    _uiState.update {
                        it.copy(
                            rootItems = event.items,
                            currentItems =  event.items,
                            selectedTitle = event.selectedTitle,
                            selectedSubTitle = event.selectedSubTitle,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            rootItems = event.items,
                            currentItems = event.items.firstOrNull{ it.id?.equals(event.selectedTitle?.id) == true }?.selections?:emptyList(),
                            searchQuery = "",
                            selectedTitle = event.selectedTitle,
                            selectedSubTitle = event.selectedSubTitle,
                            currentUiMode = DialogMode.SubTitle,
                        )
                    }
                }

            }
            is DescriptionEvent.OnBackButton -> {
                _uiState.update {
                    it.copy(
                        currentItems = it.rootItems,
                        currentUiMode = DialogMode.Title,
                        selectedTitle = null
                    )
                }
            }
            is DescriptionEvent.OnSearchQueryChanged -> {
                _uiState.update {
                    it.copy(searchQuery = event.query)
                }
            }
            is DescriptionEvent.OnItemClicked -> {
                if (!event.item.selections.isNullOrEmpty()) {
                    // 👉 Navigate to child list
                    _uiState.update {
                        it.copy(
                            firstClick = false,
                            currentItems = event.item.selections!!,
                            currentUiMode = DialogMode.SubTitle,
                            searchQuery = "",
                            selectedTitle = event.item
                        )
                    }
                } else {
                    onEvent(DescriptionEvent.OnUserSelectionToggled(event.item))
                }
            }
            is DescriptionEvent.OnUserSelectionToggled -> {
                _uiState.update { state ->
                    when (state.currentUiMode) {
                        is DialogMode.Title -> {
                            val selectedTitle = if (state.selectedTitle?.id?.equals(event.item?.id?:"") == true) {
                                null
                            } else {event.item}
                            state.copy(
                                firstClick = false,
                                selectedTitle = selectedTitle,
                                selectedSubTitle = null
                            )
                        }
                        is DialogMode.SubTitle -> {
                            val selectedSubTitle = if (state.selectedTitle?.id?.equals(event.item?.id?:"") == true) {
                                null
                            } else {event.item}
                            state.copy(
                                firstClick = false,
                                selectedSubTitle = selectedSubTitle
                            )
                        }
                    }
                }
            }
        }
    }
}