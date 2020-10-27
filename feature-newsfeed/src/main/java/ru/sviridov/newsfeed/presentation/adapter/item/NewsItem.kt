package ru.sviridov.newsfeed.presentation.adapter.item

class NewsItem(
    val postId: Int,
    val sourceId: Int,
    var sourceTitle: String,
    val postedAt: String,
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