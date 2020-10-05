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
)