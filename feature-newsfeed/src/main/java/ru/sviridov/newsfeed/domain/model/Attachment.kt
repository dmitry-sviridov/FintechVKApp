package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Attachment(
    var link: Link?,
    var photo: PhotoAttachment?,
    val type: String
)