package ru.sviridov.newsfeed.data.db.item

import androidx.room.Entity
import androidx.room.PrimaryKey

// Вообще сущности пользовательского интерфейса и БД должны отличаться, но в текущей парадигме одно эквивалентно другому.
@Entity(tableName = NewsItem.tableName)
class NewsItem(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsItem

        if (postId != other.postId) return false
        if (sourceTitle != other.sourceTitle) return false
        if (postedAt != other.postedAt) return false
        if (sourceAvatar != other.sourceAvatar) return false

        return true
    }

    override fun hashCode(): Int {
        var result = postId
        result = 31 * result + sourceTitle.hashCode()
        result = 31 * result + postedAt.hashCode()
        result = 31 * result + sourceAvatar.hashCode()
        return result
    }
}