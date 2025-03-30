package com.example.news_data

import com.example.mewsdatabase.NewDataBase
import com.example.mewsdatabase.models.ArticleDBO
import com.example.news_core.Loggers
import com.example.news_data.models.Article
import com.example.news_data.models.RequestResult
import com.example.news_data.models.map
import com.example.news_data.models.toRequestResult
import com.example.newsapi.NewsApi
import com.example.newsapi.models.ArticleDTO
import com.example.newsapi.models.ResponseDTO
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository @Inject constructor(
    private val database: NewDataBase,
    private val api: NewsApi,
    private val logger: Loggers,
) {
    fun getAll(
        mergeStrategy: MergeStrategy <RequestResult<List<Article>>> = ResponseMergeStrategy()
    ): Flow <RequestResult<List<Article>>> {

        val cachedAllArticle: Flow<RequestResult<List<Article>>> = getAllFromDataBase()

        val remoteArticles = getAllFromServer()

        return cachedAllArticle.combine(remoteArticles) {
            dbos: RequestResult<List<Article>>, dtos: RequestResult<List<Article>> ->
            mergeStrategy.merge(dbos, dtos)
        }.flatMapLatest { result ->
            if(result is RequestResult.Success) {
                database.articlesDao.observeAllArticles()
                    .map { dbos -> dbos.map { it.toArticle() } }
                    .map { RequestResult.Success(it) }
            } else {
                flowOf(result)
            }
            }
    }

    private fun getAllFromServer(): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow { emit(api.everything())  }
            .onEach { result -> if(result.isSuccess){
                saveServerResponse(result.getOrThrow().articles) }
        }
            .onEach { result ->
                if (result.isFailure) {
                    logger.e(
                        LOG_TAG,
                        "Error getting data from server. Cause = ${result.exceptionOrNull()}"
                    )
                }
            }
            .map { it.toRequestResult() }
        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(apiRequest, start)
            .map { result: RequestResult<ResponseDTO<ArticleDTO>> ->
                result.map { responseDTO ->
                    responseDTO.articles.map { it.toArticle() } } }
    }

    private suspend fun saveServerResponse(data: List<ArticleDTO>) {
        val dbos = data.map { articleDTO -> articleDTO.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

        private fun getAllFromDataBase(): Flow<RequestResult<List<Article>>> {
            val dbResponse = flow { emit(database.articlesDao.getAllArticles()) }
                    .map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> { RequestResult.Success(it) }
                    .catch {
                        logger.e(LOG_TAG, "Error getting from database. Cause = $it")
                        emit(RequestResult.Error(error = it))
                    }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start,dbResponse)
            .map { result -> result.map { articleDBOS -> articleDBOS.map { it.toArticle() } } }

    }

    fun updateArticles(): Flow<RequestResult<List<Article>>> {
        return getAllFromServer()
    }
    private companion object {
        const val LOG_TAG = "ArticlesRepository"
    }
}