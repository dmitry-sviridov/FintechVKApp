package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Image(
    val height: Int,
    val url: String,
    val width: Int,
    val with_padding: Int
)