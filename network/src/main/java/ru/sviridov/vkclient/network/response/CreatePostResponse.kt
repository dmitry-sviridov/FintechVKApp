package ru.sviridov.vkclient.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
class CreatePostResponse(
    @JsonProperty(value = "post_id") val postId: Int
)