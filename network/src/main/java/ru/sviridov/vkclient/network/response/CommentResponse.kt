package ru.sviridov.vkclient.network.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName(value = "response")
class CommentResponse(
    @JsonProperty("comment_id") val commentId: Int,
    @JsonProperty("parents_stack") val parentStack: List<Any?>?
)
