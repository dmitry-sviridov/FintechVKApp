package ru.sviridov.vkclient.network.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import ru.sviridov.vkclient.network.model.Comment
import ru.sviridov.vkclient.network.model.CommentProfile

@JsonRootName(value = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
data class PostCommentsResponse(
    @JsonProperty("can_post") val canPost: Boolean,
    @JsonProperty("count") val count: Int,
    @JsonProperty("current_level_count") val currentLevelCount: Int,
    @JsonProperty("groups") val groups: List<Any>?,
    @JsonProperty("groups_can_post") val groupsCanPost: Boolean,
    @JsonProperty("items")  val items: List<Comment>,
    @JsonProperty("profiles") val profiles: List<CommentProfile>,
    @JsonProperty("show_reply_button") val showReplyButton: Boolean
)