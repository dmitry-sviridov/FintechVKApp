package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ru.sviridov.vkclient.network.model.Size

@JsonIgnoreProperties(ignoreUnknown = true)
data class WallPhoto(
    val access_key: String,
    val album_id: Int,
    val date: Int,
    val has_tags: Boolean,
    val id: Int,
    val owner_id: Int,
    val post_id: Int,
    val sizes: List<Size>,
    val text: String,
    val user_id: Int
)