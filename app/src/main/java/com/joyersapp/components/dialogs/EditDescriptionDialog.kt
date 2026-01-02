package com.joyersapp.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

//@Preview
@Composable
fun EditDescriptionDialog(
    titlesData: List<ProfileTitlesData>,
    headers: List<String>,
    onDismiss: () -> Unit,
    onApply: () -> Unit = {}
) {

    var searchQuery by remember { mutableStateOf("") }
    var itemsList by remember { mutableStateOf(titlesData) }
    var itemsList2 by remember { mutableStateOf(titlesData) }
    var clarificationList by remember { mutableStateOf(itemsList2.filter { !it.description.isNullOrEmpty() }) }

    ProfileViewDialog(
        onDismiss = onDismiss,
        onApply = onApply,
        showApplyButton = true,
        headers = headers,
        searchQuery = searchQuery,
        onSearchQueryChanged = { query ->
            searchQuery = query
//            CoroutineScope(Dispatchers.Default).launch {
//                itemsList =
//                    itemsList2.filter { it.name?.contains(query, ignoreCase = true) ?: false }
//            }

        },
        titlesData = itemsList2.filter { it.name?.contains(searchQuery, ignoreCase = true) ?: false },
        clarificationData = clarificationList,
        onShowSubTitles = {
            itemsList2 = it
        },
        onBack = { itemsList2 = titlesData }
    )
}

