package com.joyersapp.feature.post.presentation.create_post

import android.content.Context
import android.media.browse.MediaBrowser
import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.core.SessionManager
import com.joyersapp.feature.post.domain.model.Joyer
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.utils.graphemeCount
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class CreatePostUiState(

    val isLoading: Boolean = false,
    val isDataLoaded: Boolean = false,
    val showMentionJoyersDialog: Boolean = false,
    val showMediaPickerDialog: Boolean = false,
    val showSaveDraftsPrompt: Boolean = false,
    val textState: TextFieldState = TextFieldState(),
    val joyer: Joyer = Joyer(
        id = "1",
        name = "James Spiegel James Spie Jame Spiegel James Spie James Spie",
        username = "james",
        profilePicture = "",
        tag = "Engineer",
        starsCount = 1,
        isLockVisible = true
    ),
    val mediaList: List<MediaItem> = emptyList(),

    ) {
    val remainingCharacters get() =  (300 - textState.text.toString().graphemeCount())
    val isPostingEnabled get() =  (mediaList.isNotEmpty() || textState.text.toString().isNotEmpty())
    }

sealed class CreatePostNavigationEvent {


}

sealed class CreatePostEvent {

    data class ToggleMediaPickerDialog(val show: Boolean) : CreatePostEvent()
    data class ToggleMentionJoyersDialog(val show: Boolean) : CreatePostEvent()
    data class ToggleSavedDraftsPrompt(val show: Boolean) : CreatePostEvent()
    data class ApplyMentionedJoyers(val selectedUserList: List<EditMagneticsUserListData>) : CreatePostEvent()


}

enum class MediaType {
    IMAGE,
    VIDEO
}
@Immutable
data class MediaItem(
//    val id: String = UUID.randomUUID().toString(),
    val uri: Uri,
    val type: MediaType,
//    val thumbnailUri: Uri? = null
)


@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState: StateFlow<CreatePostUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<CreatePostNavigationEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    override fun onCleared() {
        super.onCleared()
        _uiState.update { CreatePostUiState() }
    }

    fun clearData() {
       onCleared()
    }

    fun addMedia(uris: List<Uri>, context: Context) {
        viewModelScope.launch {
            val items = uris.map {
                val type = if (isVideo(context, it)) MediaType.VIDEO else MediaType.IMAGE
                MediaItem(it, type)
            }

            _uiState.update {
                it.copy(
                    mediaList = (it.mediaList + items).take(5),
                    showMediaPickerDialog = false,
                )
            }
        }
    }

    fun removeMedia(item: MediaItem) {   _uiState.update {
        it.copy(
            mediaList = (it.mediaList - item)
        )
    }
    }

    private fun isVideo(context: Context, uri: Uri): Boolean {
        val type = context.contentResolver.getType(uri)
        return type?.startsWith("video") == true
    }


    fun onEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.ApplyMentionedJoyers -> {
                val selectedUsers =
                    event.selectedUserList.joinToString(
                        separator = " @",
                        postfix = " "
                    ) { it.username ?: "" }

                _uiState.value.textState.edit {
                    append(selectedUsers)
                }
                _uiState.value = _uiState.value.copy(showMentionJoyersDialog = false)
            }
            is CreatePostEvent.ToggleMentionJoyersDialog -> {
                _uiState.value = _uiState.value.copy(showMentionJoyersDialog = event.show)
            }

            is CreatePostEvent.ToggleMediaPickerDialog -> {
                _uiState.value = _uiState.value.copy(showMediaPickerDialog = event.show)
            }

            is CreatePostEvent.ToggleSavedDraftsPrompt -> {
                _uiState.value = _uiState.value.copy(showSaveDraftsPrompt = event.show)
            }
        }
    }

}