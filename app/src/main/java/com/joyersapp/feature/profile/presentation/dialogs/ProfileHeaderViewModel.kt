package com.joyersapp.feature.profile.presentation.dialogs

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joyersapp.R
import com.joyersapp.feature.profile.data.remote.dto.EditMagneticsUserListData
import com.joyersapp.feature.profile.presentation.ProfileHeaderData
import com.joyersapp.feature.profile.presentation.UserProfileEvent
import com.joyersapp.utils.UiText
import com.joyersapp.utils.graphemeCount
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileHeaderUiState(

    val profilePicture: String = "",
    val backgroundPicture: String = "",
    val bio: String = "",
    val overviewFieldValue: TextFieldValue = TextFieldValue(""),
    val highlightFieldValue: TextFieldValue = TextFieldValue(text = "• ", selection = TextRange(2)),
    val overviewRemainingChars: Int = 150 - overviewFieldValue.text.graphemeCount(),
    val highlightsRemainingChars: Int = 25 - highlightFieldValue.text.graphemeCount(),
    val websiteUrl: String = "",
    val bioValidationError: UiText? = null,

    var selectedTab: String = "overview"

) {

}

sealed class ProfileHeaderEvent {

    data class ToggleBioEditor(val tab: String) : ProfileHeaderEvent()
    data class OnOverviewChanged(val tab: String) : ProfileHeaderEvent()
    data class OnHighlightChanged(val tab: String) : ProfileHeaderEvent()
    data class OnWebsiteUrlChanged(val tab: String) : ProfileHeaderEvent()

}

sealed class ProfileHeaderNavEvent {
    class OnApply(val selectedUsers: List<ProfileHeaderData>) : ProfileHeaderNavEvent()

}

@HiltViewModel
class ProfileHeaderViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileHeaderUiState() )
    private val _navigationEvents = MutableSharedFlow<MentionJoyersNavEvent>()

    val uiState: StateFlow<ProfileHeaderUiState> = _uiState.asStateFlow()
    val navigationEvents = _navigationEvents


    fun onEvent(event: ProfileHeaderEvent) {
        when (event) {
            is ProfileHeaderEvent.ToggleBioEditor -> {
//                _uiState.update {
//                    it.copy(
//                        profileHeaderData = _uiState.value.profileHeaderData.copy(
//                            selectedTab = event.tab
//                        ),
//                    )
//                }
            }

            is ProfileHeaderEvent.OnOverviewChanged -> {
//                val overviewRemainingChars = 150 - event.value.text.graphemeCount()
//                if (overviewRemainingChars >= -20) {
//                    _uiState.update {
//                        it.copy(
//                            profileHeaderData = it.profileHeaderData.copy(
////                            bio = event.value.text,
//                                overviewFieldValue = event.value.copy(selection = TextRange(event.value.text.length)),
//                                overviewRemainingChars = 150 - event.value.text.graphemeCount(),
//                                bioValidationError = if (overviewRemainingChars <= -1) UiText.StringResource(
//                                    R.string.bio_validation_error
//                                ) else null,
//                            ),
//                        )
//                    }
//                }
            }

            is ProfileHeaderEvent.OnHighlightChanged -> {

//                val websiteUrl = _uiState.value.profileHeaderData.websiteUrl
//                val old = _uiState.value.profileHeaderData.highlightFieldValue
//                val oldText = old.text
//                val newText = event.value.text
//
//                // Default bullet prefix
//                val bullet = "• "
//                val bulletLine = "\n$bullet"
//                val lastLine = newText.substringAfterLast(bulletLine)
//
//                // Helper function to apply result
//                fun update(newStr: String) {
//                    _uiState.update {
//                        it.copy(
//                            profileHeaderData = it.profileHeaderData.copy(
//                                highlightFieldValue = TextFieldValue(
//                                    text = newStr,
//                                    selection = TextRange(newStr.length)
//                                ),
//                                highlightsRemainingChars = 25 - lastLine.graphemeCount()
//                            ),
//                        )
//                    }
//                }
//
//
//
//
//                // ---------------------------------------------------------
//                // Enforce per-line limit (25 chars each bullet)
//                // ---------------------------------------------------------
//                if (lastLine.graphemeCount() > 25) return
//
//                // Restore single bullet when completely cleared
//                if (newText.isEmpty()) {
//                    update(bullet)
//                    return
//                }
//
//                // Guard: prevent state "•" (missing trailing space)
//                if (newText == "•" && !oldText.contains("\n")) {
//                    update(bullet)
//                    return
//                }
//
//                // Detect "Enter" → add a new bullet
//                if (newText.endsWith("\n")
////                    && !oldText.endsWith(bulletLine)
//                ) {
//                    val maxBullets = if (websiteUrl.isNotEmpty()) 4 else 5
//                    val count = oldText.count { it == '•' }
//                    if (count >= maxBullets) {
//                        update(oldText)   // restore previous, prevent bullet overflow
//                        return
//                    }
//
//                    update(newText + bullet)
//                    return
//                }
//
//                // delete empty bullet
//                if (
//                    oldText.endsWith(bulletLine) &&
//                    newText.length < oldText.length
//                ) {
//                    val trimmed = oldText.removeSuffix(bulletLine)
//                    update(trimmed)
//                    return
//                }
//
//                // Normal typing or deletion
//                update(newText)
            }

            is ProfileHeaderEvent.OnWebsiteUrlChanged -> {
//                _uiState.update {
//                    it.copy(
//                        profileHeaderData = _uiState.value.profileHeaderData.copy(
//                            websiteUrl = event.value
//                        )
//                    )
//                }
            }
        }

    }
}