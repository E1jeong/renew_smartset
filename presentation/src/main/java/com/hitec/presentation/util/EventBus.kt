package com.hitec.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow

object EventBus {

    //MainViewModel -> SearchViewModel
    val subAreaListState = MutableStateFlow<List<String>>(emptyList())
}