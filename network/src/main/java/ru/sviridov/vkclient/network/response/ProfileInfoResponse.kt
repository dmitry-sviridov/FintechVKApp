package ru.sviridov.vkclient.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProfileInfoResponseContainer(
    val response: ProfileInfoResponse
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProfileInfoResponse(
    @JsonProperty(value = "bdate")             val bDate: String,
    @JsonProperty(value = "bdate_visibility")  val bDateVisibility: Int,
    @JsonProperty(value = "first_name")        val firstName: String,
    @JsonProperty(value = "home_town")         val homeTown: String,
    @JsonProperty(value = "id")                val id: Int,
    @JsonProperty(value = "last_name")         val lastName: String,
    @JsonProperty(value = "phone")             val phone: String,
    @JsonProperty(value = "relation")          val relation: Int,
    @JsonProperty(value = "sex")               val sex: Int,
    @JsonProperty(value = "status")            val status: String
)