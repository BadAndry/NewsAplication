package com.example.mewsdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mewsdatabase.dao.NewsArticlesDao
import com.example.mewsdatabase.models.ArticleDBO
import com.example.mewsdatabase.utils.TypeConvector


class NewDataBase internal constructor(private val dataBase: NewsDataBaseRoom) {
    val articlesDao: NewsArticlesDao
        get() = dataBase.articlesDao()
}

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(TypeConvector::class)

internal abstract class NewsDataBaseRoom: RoomDatabase() {

    abstract fun articlesDao(): NewsArticlesDao
}

        fun GetDatabase(context: Context): NewDataBase {
                val newsDataBaseRoom = Room.databaseBuilder(
                    checkNotNull(context.applicationContext),
                    NewsDataBaseRoom::class.java,
                    "app_database"
                ).build()
                return NewDataBase(newsDataBaseRoom)
            }

