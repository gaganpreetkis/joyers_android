package com.joyersapp.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    val previousIndex = remember { mutableStateOf(firstVisibleItemIndex) }
    val previousScrollOffset = remember { mutableStateOf(firstVisibleItemScrollOffset) }

    return remember {
        derivedStateOf {
            if (previousIndex.value != firstVisibleItemIndex) {
                previousIndex.value > firstVisibleItemIndex
            } else {
                previousScrollOffset.value >= firstVisibleItemScrollOffset
            }.also {
                previousIndex.value = firstVisibleItemIndex
                previousScrollOffset.value = firstVisibleItemScrollOffset
            }
        }
    }.value
}