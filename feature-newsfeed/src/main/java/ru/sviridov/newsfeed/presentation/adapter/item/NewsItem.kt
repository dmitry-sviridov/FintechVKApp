package ru.sviridov.newsfeed.presentation.adapter.item

data class NewsItem(
    val sourceTitle: String,
    val postedAt: String,
    val sourceAvatar: String,
    val imageUrl: String?,
    val textContent: String?,
    val likesCount: Int,
    val shareCount: Int,
    val commentCount: Int,
    val viewsCount: Int
)