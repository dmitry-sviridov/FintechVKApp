package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Profile(
    val id: Int,
    @JsonProperty("first_name") val firstName: String,
    @JsonProperty("last_name") val lastName: String,
    @JsonProperty("photo_50") val photoWithSize50: String,
)