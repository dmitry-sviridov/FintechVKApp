package ru.sviridov.vkclient.ui.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.sviridov.vkclient.ui.data.db.dao.LikedNewsItemDao
import ru.sviridov.vkclient.ui.data.db.entity.NewsItemEntity

@Database(entities = [NewsItemEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun likedDao(): LikedNewsItemDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}