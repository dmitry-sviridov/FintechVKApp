package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Link(
    val caption: String?,
    val description: String?,
    val photo: Photo?,
    val title: String?,
    val url: String
)