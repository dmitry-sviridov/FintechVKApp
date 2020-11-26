package ru.sviridov.newsfeed.data.db.dao

import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single
import ru.sviridov.newsfeed.data.db.entity.NewsItemEntity
import javax.inject.Singleton

@Dao
interface LikedNewsItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsItem(newsItem: NewsItemEntity): Single<Long>

    @Delete
    fun deleteNewsItem(newsItem: NewsItemEntity): Single<Int>

    @Query("SELECT * FROM ${NewsItemEntity.tableName} ORDER BY postedAt DESC")
    fun getAllLiked(): Observable<List<NewsItemEntity>>
}