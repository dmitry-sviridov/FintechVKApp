package ru.sviridov.newsfeed.domain.model

data class Photo(
    val id: Int,
    val album_id: Int,
    val date: Int,
    val height: Int,
    val width: Int,
    val owner_id: Int,
    val photo_75: String?,
    val photo_130: String?,
    val photo_604: String?,
    val photo_807: String?,
    val photo_1280: String?,
    val photo_2560: String?,
    val text: String
)
