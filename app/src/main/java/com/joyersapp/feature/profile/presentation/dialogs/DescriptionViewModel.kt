package com.joyersapp.feature.profile.presentation.dialogs

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
 sealed class DialogMode {
//    data object JoyerStatus : DialogMode()
    data object Title : DialogMode()
    data object SubTitle : DialogMode()
}

data class DescriptionUiState(

    val rootItems: List<ProfileTitlesData> = emptyList(),
    val currentItems: List<ProfileTitlesData> = emptyList(),
    val currentUiMode: DialogMode = DialogMode.Title,
    val preSelectedTitle: ProfileTitlesData? = null,
    val selectedTitle: ProfileTitlesData? = null,
    val localTitle: ProfileTitlesData? = null,
    val preSelectedSubTitle: ProfileTitlesData? = null,
    val selectedSubTitle: ProfileTitlesData? = null,
    val firstClick: Boolean = true,
    val searchQuery: String = "",

    val headers: MutableList<String> =  mutableListOf("Description", "Joyer Status", "Classic")

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
        get() = currentUiMode is DialogMode.SubTitle


//    val headers: MutableList<String>
//        get() =  when (currentUiMode) {
//            DialogMode.Title -> {
//                mutableListOf("Description", "Joyer Status", "Classic")
//            }
//
//            DialogMode.SubTitle -> {
//                mutableListOf("Description", "Joyer Status", "Classic", selectedTitle?.name?:"")
//            }
//        }

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
        get() = when (currentUiMode) {
            DialogMode.Title -> {
                rootItems.firstOrNull{ it.isSelected }?.id.equals(preSelectedTitle?.id) == false
            }

            DialogMode.SubTitle -> {
                currentItems.any(){ it.isSelected } && currentItems.firstOrNull{ it.isSelected }?.id?.equals(preSelectedSubTitle?.id) == false
            }
        }
}


sealed class DescriptionEvent {

    data class InitData(val items: List<ProfileTitlesData>, val selectedTitle: ProfileTitlesData?, val selectedSubTitle: ProfileTitlesData?) : DescriptionEvent()
    data object OnBackButton : DescriptionEvent()
    data class OnItemClicked(val item: ProfileTitlesData) : DescriptionEvent()
    data class OnUserSelectionToggled(val item: ProfileTitlesData?) : DescriptionEvent()
    data class OnSearchQueryChanged(val query: String) : DescriptionEvent()

    data object OnApply : DescriptionEvent()
    data object OnClearData : DescriptionEvent()


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

    override fun onCleared() {
        super.onCleared()
        _uiState.update { DescriptionUiState() }
    }


    fun onEvent(event: DescriptionEvent) {
        when (event) {
            is DescriptionEvent.OnApply -> {
                val state = uiState.value

                val selectedTitle = if (state.currentUiMode is DialogMode.SubTitle) {
                    state.localTitle
                } else state.rootItems.firstOrNull{ it.isSelected }
                val selectedSubTitle = if (state.currentUiMode is DialogMode.SubTitle) {
                    state.currentItems.firstOrNull{ it.isSelected }
                } else null
                viewModelScope.launch {
                    _navigationEvents.send(
                        DescriptionNavEvent.OnApply(
                            Title = selectedTitle,
                            SubTitle = selectedSubTitle,
                        )
                    )
                    onCleared()
                }
            }
            is DescriptionEvent.OnClearData -> {
                onCleared()
            }
            is DescriptionEvent.InitData -> {

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

                    uiState.value.headers.add(event.selectedTitle?.name?:"")

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
                            currentUiMode = DialogMode.SubTitle,
                        )
                    }
                }

            }
            is DescriptionEvent.OnBackButton -> {
               uiState.value.headers.removeLast()
                _uiState.update {
                    it.copy(
                        currentItems = it.rootItems,
                        currentUiMode = DialogMode.Title,
                        localTitle = null,
//                        selectedTitle = null,
//                        selectedSubTitle = null
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
                    uiState.value.headers.add(event.item.name?:"")
                    // 👉 Navigate to child list
                    _uiState.update {
                        it.copy(
                            firstClick = false,
                            currentItems = event.item.selections!!,
                            currentUiMode = DialogMode.SubTitle,
                            searchQuery = "",
                            localTitle = event.item
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
                        is DialogMode.SubTitle -> {
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