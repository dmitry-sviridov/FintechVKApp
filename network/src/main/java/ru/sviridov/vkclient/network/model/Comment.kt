package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Comment(
    val attachments: List<Attachment?>?,
    val date: Int,
    @JsonProperty("from_id") val fromId: Int,
    val id: Int,
    val likes: CommentLikes?,
    @JsonProperty("owner_id") val ownerId: Int,
    val parents_stack: List<Any>,
    @JsonProperty("post_id") val postId: Int,
    val text: String?,
    val thread: Thread
)