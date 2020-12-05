package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Likes(
    @JsonProperty("can_like") val canLike: Int,
    @JsonProperty("can_publish") val canPublish: Int,
    val count: Int,
    @JsonProperty("user_likes") val userLikes: Int
)

// Для новостей и для комментариев две идентичных модели,
// но в одной поле bool, а в другой - int
// Я в шоке
data class CommentLikes(
    @JsonProperty("can_like") val canLike: Int,
    @JsonProperty("can_publish") val canPublish: Boolean,
    val count: Int,
    @JsonProperty("user_likes") val userLikes: Int
)