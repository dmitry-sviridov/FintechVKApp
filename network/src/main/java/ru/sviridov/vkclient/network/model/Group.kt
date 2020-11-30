package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Group(
    var description: String?,
    val id: Int,
    @JsonProperty("is_admin") val isAdmin: Int,
    @JsonProperty("admin_level") var adminLevel: Int? = 0,
    @JsonProperty("is_closed") val isClosed: Int,
    @JsonProperty("is_member") val isMember: Int,
    val name: String,
    @JsonProperty("photo_100") val photoWithSize100: String,
    @JsonProperty("photo_200") val photoWithSize200: String,
    @JsonProperty("photo_50") val photoWithSize50: String,
    @JsonProperty("screen_name") val screenName: String,
    val type: String
)