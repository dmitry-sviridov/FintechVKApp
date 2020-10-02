package ru.sviridov.newsfeed.domain.model

data class Link(
    val caption: String,
    val description: String,
    val photo: Photo,
    val title: String,
    val url: String
)