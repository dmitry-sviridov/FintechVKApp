package ru.sviridov.newsfeed.domain.model

data class Likes(
    val can_like: Int,
    val can_publish: Int,
    val count: Int,
    val user_likes: Int
)