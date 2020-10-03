package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Link(
    var caption: String?,
    var description: String?,
    var photo: Photo?,
    var title: String?,
    val url: String
)