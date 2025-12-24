package com.joyersapp.components.layouts

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun TokenExpiredDialog(
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Prevent dismiss */ },
        title = { Text("Session Expired") },
        text = { Text("Your session has expired. Please log in again.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Login")
            }
        }
    )
}