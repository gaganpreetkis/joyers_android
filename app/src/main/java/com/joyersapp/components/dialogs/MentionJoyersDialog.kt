package com.joyersapp.components.dialogs

import androidx.compose.runtime.Composable
//@Preview
@Composable
fun MentionJoyersDialog(
    onDismiss: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    BaseDialog (
        onDismiss = { onDismiss() },
        titles = arrayListOf("Profile Header")

    ) { dialogModifier, dialogFocusManager, maxHeight ->

        // Mention Joyers List


    }
}