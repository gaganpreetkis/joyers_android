package com.joyersapp.components.dialogs

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData

//@Preview
@Composable
fun EditDescriptionDialog(
    titlesData: List<ProfileTitlesData>,
    onDismiss: () -> Unit,
    onApply: () -> Unit = {}
) {
    ProfileViewDialog(
        onDismiss = onDismiss,
        onApply = onApply,
        showApplyButton = true,
        titles = arrayListOf("Description"),
        searchQuery = "",
        FirstColumn = {
            itemsIndexed(titlesData) {
                index, item ->

            }
        },
    )

}
