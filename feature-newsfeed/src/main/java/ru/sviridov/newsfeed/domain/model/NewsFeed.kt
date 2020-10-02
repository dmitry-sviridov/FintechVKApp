package ru.sviridov.newsfeed.domain.model

data class NewsFeed(
    val attachments: List<Attachment>,
    val comments: Comments,
    val date: Int,
    val likes: Likes,
    val marked_as_ads: Int,
    val post_id: Int,
    val post_source: PostSource,
    val post_type: String,
    val reposts: Reposts,
    val source_id: Int,
    val text: String,
    val type: String,
    val views: Views
)