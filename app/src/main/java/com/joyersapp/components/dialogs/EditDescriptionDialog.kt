package com.joyersapp.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.joyersapp.feature.profile.data.remote.dto.ProfileTitlesData

//@Preview
@Composable
fun EditDescriptionDialog(
    titlesData: List<ProfileTitlesData>,
    headers: List<String>,
    onDismiss: () -> Unit,
    onApply: (List<ProfileTitlesData>) -> Unit
) {

  /*  var searchQuery by remember { mutableStateOf("") }
    var itemsList by remember { mutableStateOf(titlesData) }
    var itemsList2 by remember { mutableStateOf(titlesData) }
    var clarificationList by remember { mutableStateOf(itemsList2.filter { !it.description.isNullOrEmpty() }) }
*/

    var searchQuery by remember { mutableStateOf("") }
    var currentList by remember { mutableStateOf(titlesData) }

// Derived states (calculated efficiently)
    val filteredTitles by remember(searchQuery, currentList) {
        derivedStateOf {
            currentList.filter {
                it.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    val reorderedTitles by remember(filteredTitles) {
        derivedStateOf {
            val selected = filteredTitles.filter { it.isSelected }
            val unselected = filteredTitles.filter { !it.isSelected }

            // selected first, then normal
            selected + unselected
        }
    }

    val clarificationTitles by remember(searchQuery) {
        derivedStateOf {
            titlesData.filter {
                !it.description.isNullOrEmpty() &&
                        it.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }
    ProfileViewDialog(
        onDismiss = onDismiss,
        onApply = { onApply(currentList.filter { it.isSelected }) },
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
        titlesData = reorderedTitles,
        clarificationData = clarificationTitles,
        onShowSubTitles = { list ->
            currentList = list
        },
        onTitleSelected = { titleId ->
            currentList = currentList.map { item ->
                if (item.id == titleId) item.copy(isSelected = !item.isSelected)
                else item
            }
        },
        onBack = {
            currentList = titlesData
        }
    )
}

