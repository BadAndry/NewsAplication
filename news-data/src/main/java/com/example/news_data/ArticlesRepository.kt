package com.example.news_data

import com.example.mewsdatabase.NewsDataBase
import com.example.news_data.models.Articles
import com.example.newsapi.NewsApi
import kotlinx.coroutines.flow.Flow

class ArticlesRepository(
    private val database: NewsDataBase,
    private val api: NewsApi,
) {
    fun request(): Flow<Articles> {
        TODO()
    }

}