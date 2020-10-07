package ru.sviridov.newsfeed.presentation.adapter.item

data class NewsItem(
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
    // Temporary solution: we need to compare two objects without likes count, isLiked and so on
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as NewsItem

        if (this.sourceTitle == other.sourceTitle &&
            this.postedAt == other.postedAt &&
            this.imageUrl == other.imageUrl &&
            this.textContent == other.textContent
        ) return true

        return false
    }

    override fun hashCode(): Int {
        var result = sourceTitle.hashCode()
        result = 31 * result + postedAt.hashCode()
        result = 31 * result + sourceAvatar.hashCode()
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (textContent?.hashCode() ?: 0)
        return result
    }
}