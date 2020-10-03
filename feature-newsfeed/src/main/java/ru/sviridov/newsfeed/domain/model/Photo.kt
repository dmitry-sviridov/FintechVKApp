package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Photo(
    val id: Int,
    val album_id: Int,
    val date: Int,
    val height: Int,
    val width: Int,
    val owner_id: Int,
    var photo_75: String?,
    var photo_130: String?,
    var photo_604: String?,
    var photo_807: String?,
    var photo_1280: String?,
    var photo_2560: String?,
    val text: String
)
