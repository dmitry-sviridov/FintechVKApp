package ru.sviridov.newsfeed.data.db.dao

import androidx.room.*
import io.reactivex.Single
import ru.sviridov.newsfeed.data.db.entity.NewsItemEntity

@Dao
interface LikedNewsItemDao {

    // REPLACE триггерит перезапись после каждого запуска, что не оптимально.
    // В реальном проекте, думаю, было бы оптимально хранить изменяемую часть (комменты, лайки и пр. в отдельной таблице
    // со связью 1к1
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsItem(newsItem: NewsItemEntity): Single<Long>

    @Delete
    fun deleteNewsItem(newsItem: NewsItemEntity): Single<Int>

    @Query("SELECT * FROM ${NewsItemEntity.tableName} ORDER BY postedAt")
    fun getAllLiked(): Single<List<NewsItemEntity>>
}