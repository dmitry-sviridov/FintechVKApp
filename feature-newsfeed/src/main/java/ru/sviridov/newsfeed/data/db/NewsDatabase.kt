package ru.sviridov.newsfeed.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.sviridov.newsfeed.data.db.dao.LikedNewsItemDao
import ru.sviridov.newsfeed.data.db.entity.NewsItemEntity

@Database(entities = [NewsItemEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun likedDao(): LikedNewsItemDao

    // Позаимствовано из https://developer.android.com/codelabs/android-room-with-a-view-kotlin?hl=pt-PT#7
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
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