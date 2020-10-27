package ru.sviridov.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Likes(
    @JsonProperty("can_like") val canLike: Int,
    @JsonProperty("can_publish") val canPublish: Int,
    val count: Int,
    @JsonProperty("user_likes") val userLikes: Int
)