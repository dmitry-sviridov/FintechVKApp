package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Attachment(
    val link: Link?,
    val photo: PhotoAttachment?,
    val type: String
)