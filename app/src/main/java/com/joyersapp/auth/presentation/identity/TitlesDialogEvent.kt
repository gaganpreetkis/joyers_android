package com.joyersapp.auth.presentation.identity

import com.joyersapp.auth.data.remote.dto.identity.SubTitle
import com.joyersapp.auth.data.remote.dto.identity.Title

sealed class TitlesDialogEvent {
    data class SelectionConfirmed(
        val titleId: String?,
        val titleName: String?,
        val subTitleId: String?,
        val subTitleName: String?
    ) : TitlesDialogEvent()


}