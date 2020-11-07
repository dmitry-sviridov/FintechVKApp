package ru.sviridov.component.feeditem.model

class NewsItem(
    val postId: Int,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsItem

        if (postId != other.postId) return false
        return true
    }

    override fun hashCode(): Int {
        var result = postId
        result = 31 * result + postedAt.hashCode()
        return result
    }
}