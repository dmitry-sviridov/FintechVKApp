package ru.sviridov.vkclient.feature_newsfeed.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = NewsItemEntity.tableName)
class NewsItemEntity(
    @PrimaryKey val postId: Int,
    val sourceId: Int,
    var sourceTitle: String,
    val postedAt: Int,
    var sourceAvatar: String,
    var imageUrl: String?,
    var textContent: String?,
    var likesCount: Int,
    val shareCount: Int,
    val commentCount: Int,
    val viewsCount: Int,
    var isLiked: Boolean? = null
) {
    companion object {
        const val tableName = "news_item"
    }
}