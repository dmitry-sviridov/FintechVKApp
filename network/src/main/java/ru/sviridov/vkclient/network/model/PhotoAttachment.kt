package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PhotoAttachment(
    @JsonProperty("access_key") val accessKey: String?,
    @JsonProperty("album_id") val albumId: Int,
    val date: Int,
    @JsonProperty("has_tags") val hasTags: Boolean,
    val id: Int,
    @JsonProperty("owner_id") val ownerId: Int,
    @JsonProperty("post_id") val postId: Int,
    val sizes: List<Size>,
    val text: String,
    val user_id: Int
)