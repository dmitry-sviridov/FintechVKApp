package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentProfile(
    @JsonProperty("can_access_closed")   val canAccessClosed: Boolean,
    @JsonProperty("first_name")          val firstName: String,
    @JsonProperty("id")                  val id: Int,
    @JsonProperty("is_closed")           val isClosed: Boolean,
    @JsonProperty("last_name")           val lastName: String,
    @JsonProperty("online")              val online: Int,
    @JsonProperty("online_app")          val onlineApp: Int,
    @JsonProperty("online_info")         val onlineInfo: OnlineInfo,
    @JsonProperty("online_mobile")       val onlineMobile: Int,
    @JsonProperty("photo_100")           val photo100: String,
    @JsonProperty("photo_50")            val photo50: String,
    @JsonProperty("screen_name")         val screenName: String?,
    @JsonProperty("sex")                 val sex: Int
)