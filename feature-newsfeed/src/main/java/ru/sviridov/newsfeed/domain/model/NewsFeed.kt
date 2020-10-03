package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NewsFeed(
    var attachments: List<Attachment>? = emptyList(),
    var comments: Comments?,
    val date: Int,
    var likes: Likes?,
    val marked_as_ads: Int,
    val post_id: Int,
    var post_source: PostSource?,
    var post_type: String?,
    var reposts: Reposts?,
    var source_id: Int?,
    var text: String?,
    var type: String?,
    var views: Views?
)