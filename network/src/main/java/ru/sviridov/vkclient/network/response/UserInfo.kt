package ru.sviridov.vkclient.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import ru.sviridov.vkclient.network.model.City
import ru.sviridov.vkclient.network.model.Country
import ru.sviridov.vkclient.network.model.LastSeen

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfo(
    @JsonProperty(value = "bdate")              val birthdayDate: String?,
    @JsonProperty(value = "can_access_closed")  val canAccessClosed: Boolean,
    @JsonProperty(value = "domain")             val domain: String,
    @JsonProperty(value = "first_name")         val firstName: String,
    @JsonProperty(value = "followers_count")    val followersCount: Int,
    @JsonProperty(value = "id")                 val id: Int,
    @JsonProperty(value = "is_closed")          val isClosed: Boolean,
    @JsonProperty(value = "last_name")          val lastName: String,
    @JsonProperty(value = "last_seen")          val lastSeen: LastSeen,
    @JsonProperty(value = "photo_max_orig")     val photoMaxOrig: String,
    @JsonProperty(value = "city")               val city: City?,
    @JsonProperty(value = "country")            val country: Country?
)