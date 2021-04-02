package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Photo(
    val id: Int,
    @JsonProperty("album_id") val albumId: Int,
    val date: Int,
    val height: Int,
    val width: Int,
    @JsonProperty("owner_id") val ownerId: Int,
    @JsonProperty("photo_75") var photoSize75: String?,
    @JsonProperty("photo_130") var photoSize130: String?,
    @JsonProperty("photo_604") var photoSize604: String?,
    @JsonProperty("photo_807") var photoSize807: String?,
    @JsonProperty("photo_1280") var photoSize1280: String?,
    @JsonProperty("photo_2560") var photoSize2560: String?,
    val text: String
)
