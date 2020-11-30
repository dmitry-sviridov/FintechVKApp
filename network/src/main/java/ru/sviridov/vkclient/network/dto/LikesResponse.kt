package ru.sviridov.vkclient.network.dto

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName(value = "response")
data class LikesResponse(
    val likes: Int
)