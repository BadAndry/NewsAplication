package com.example.mewsdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mewsdatabase.dao.NewsArticlesDao
import com.example.mewsdatabase.models.ArticleDBO
import com.example.mewsdatabase.utils.TypeConvector


@Database(entities = [ArticleDBO::class], version = 1)

@TypeConverters(TypeConvector::class)

abstract class NewsDataBase: RoomDatabase() {

    abstract fun articles(): NewsArticlesDao

    companion object {


        private var INSTANCE: NewsDataBase? = null

        fun getDatabase(context: Context): NewsDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDataBase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
