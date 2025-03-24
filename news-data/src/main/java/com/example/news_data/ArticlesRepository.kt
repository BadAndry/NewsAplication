package com.example.news_data

import com.example.mewsdatabase.NewDataBase
import com.example.mewsdatabase.models.ArticleDBO
import com.example.news_data.models.Article
import com.example.newsapi.NewsApi
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewDataBase,
    private val api: NewsApi,
) {
    fun getAll(): Flow <RequestResult<List<Article>>> {

        val cachedAllArticle: Flow<RequestResult.Success<List<ArticleDBO>>> = getAllFromDataBase()

        val remoteArticles: Flow<RequestResult<*>> = getAllFromServer()

    }

    private fun getAllFromServer(): Flow<RequestResult<List<ArticleDBO>>> {
        return flow {
            emit(api.everything())
        }
            .map { result ->
                if (result.isSuccess) {
                    val response: ResponseDTO<ArticleDTO> = result.getOrThrow()
                    RequestResult.Success(response.articles)
                } else {
                    RequestResult.Error(null)
                }
            }
            .filterIsInstance<RequestResult.Success<List<ArticleDTO>>>()
            .map { requestResult: RequestResult.Success<List<ArticleDTO>> ->
                requestResult.map { dtos -> dtos.map { articleDTO -> articleDTO.toArticleDBO() } }
            }
            .onEach { requestResult: RequestResult<List<ArticleDBO>> ->
                database.articlesDao.insert(requestResult.data) }
    }

    private fun getAllFromDataBase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAllArticles()
            .map { RequestResult.Success(it) }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO()
    }


    sealed class RequestResult<E>(internal val data: E) {

        class InProgress<E>(data: E) : RequestResult<E>(data)
        class Success<E>(data: E) : RequestResult<E>(data)
        class Error<E>(data: E) : RequestResult<E>(data)

    }

    internal fun <In, Out> RequestResult<In>.map(mapper: (In) -> Out): RequestResult<Out> {
        val outData = mapper(data)
        return when(this){
            is RequestResult.Error -> RequestResult.Error(outData)
            is RequestResult.InProgress -> RequestResult.InProgress(outData)
            is RequestResult.Success -> RequestResult.Success(outData)
        }
    }
}