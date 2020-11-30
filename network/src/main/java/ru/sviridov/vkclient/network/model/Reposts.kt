package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Reposts(
    val count: Int,
    @JsonProperty("user_reposted") val userReposted: Int
)