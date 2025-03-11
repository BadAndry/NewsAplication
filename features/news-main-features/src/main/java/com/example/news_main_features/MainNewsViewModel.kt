package com.example.news_main_features

import androidx.lifecycle.ViewModel
import com.example.news_main_features.models.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class MainNewsViewModel: ViewModel() {


    private var _state = MutableStateFlow<State>(State.None)
    val state: StateFlow<State>
        get() = _state.asStateFlow()

}

sealed class State {

    object None: State()
    class Loading: State()
    class Error: State()
    class Success(val articles: List<Article>): State()

}