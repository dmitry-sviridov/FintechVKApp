package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Attachment(
    val link: Link?,
    val photo: PhotoAttachment?,
    val type: String
)