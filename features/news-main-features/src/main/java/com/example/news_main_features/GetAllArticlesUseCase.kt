package com.example.news_main_features

import com.example.news_data.ArticlesRepository
import com.example.news_data.models.Article
import com.example.news_data.models.RequestResult
import com.example.news_data.models.map
import com.example.news_main_features.models.ArticleUi
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class GetAllArticlesUseCase @Inject constructor(private val repository: ArticlesRepository) {

    operator fun invoke(): Flow<RequestResult<List<ArticleUi>>> {
        return repository.getAll()
            .map { requestResult ->
                requestResult.map { articles ->
                    articles.map { it.toUiArticle() }
                }
            }
    }
}

private fun Article.toUiArticle(): ArticleUi {
    TODO("Not yet implemented")
}
