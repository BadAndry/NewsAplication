package com.example.news_main_features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news_data.ArticlesRepository
import com.example.news_data.models.RequestResult
import com.example.news_main_features.models.ArticleUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider


@HiltViewModel
internal class MainNewsViewModel @Inject constructor(
    getAllArticlesUseCase: Provider<GetAllArticlesUseCase>,
    private val articlesRepository: ArticlesRepository,
): ViewModel() {


    val state: StateFlow<State> = getAllArticlesUseCase.get().invoke()
        .map { it.toState() }
        .stateIn(viewModelScope, SharingStarted.Lazily, State.None)

    fun updateArticle() {
        articlesRepository.updateArticles()
    }

}
fun RequestResult<List<ArticleUi>>.toState(): State {
    return when(this){
        is RequestResult.Error -> State.Error()
        is RequestResult.InProgress -> State.Loading(data)
        is RequestResult.Success -> State.Success(data)
    }
}
sealed class State {

    object None: State()

    class Loading(val articleUis: List<ArticleUi>?): State()

    class Error(val articleUis: List<ArticleUi>? = null): State()

    class Success(val articleUis: List<ArticleUi>): State()

}