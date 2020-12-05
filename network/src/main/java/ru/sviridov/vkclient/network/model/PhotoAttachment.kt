package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PhotoAttachment(
    @JsonProperty("access_key")  val accessKey: String?,
    @JsonProperty("album_id")  val albumId: Int,
    @JsonProperty("date")  val date: Int,
    @JsonProperty("has_tags")  val hasTags: Boolean,
    @JsonProperty("id")  val id: Int,
    @JsonProperty("owner_id")  val ownerId: Int,
    @JsonProperty("sizes")  val sizes: List<Size>,
    @JsonProperty("text")  val text: String,
    @JsonProperty("user_id")  val userId: Int
)