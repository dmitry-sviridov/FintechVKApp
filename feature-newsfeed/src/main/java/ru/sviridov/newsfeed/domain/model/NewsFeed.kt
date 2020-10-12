package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class NewsFeed(
    var attachments: List<Attachment>? = emptyList(),
    var comments: Comments?,
    val date: Int,
    var likes: Likes?,
    @JsonProperty("marked_as_ads") val markedAsAds: Int,
    @JsonProperty("post_id") val postId: Int,
    @JsonProperty("post_source") var postSource: PostSource?,
    @JsonProperty("post_type") var postType: String?,
    var reposts: Reposts?,
    @JsonProperty("source_id") var sourceId: Int?,
    var text: String?,
    var type: String?,
    var views: Views?
)