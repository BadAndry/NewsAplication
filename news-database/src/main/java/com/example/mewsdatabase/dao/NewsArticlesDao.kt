package com.example.mewsdatabase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mewsdatabase.models.ArticleDBO
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsArticlesDao {


    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleDBO>

    @Query("SELECT * FROM articles")
    fun observeAllArticles(): Flow<List<ArticleDBO>>

    @Insert(onConflict = OnConflictStrategy. REPLACE)
    suspend fun insert(articles: List<ArticleDBO>)

    @Delete()
    suspend fun remove(articles: List<ArticleDBO>)

    @Query("DELETE FROM articles")
    suspend fun clean()
}