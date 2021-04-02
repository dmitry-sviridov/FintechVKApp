package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class OnlineInfo(
    @JsonProperty("app_id") val appId: Int,
    @JsonProperty("is_mobile") val isMobile: Boolean,
    @JsonProperty("is_online") val isOnline: Boolean,
    @JsonProperty("last_seen") val lastSeen: Int,
    @JsonProperty("visible") val visible: Boolean
)