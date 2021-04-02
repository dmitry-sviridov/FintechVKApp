package ru.sviridov.vkclient.network.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Thread(
    @JsonProperty("can_post")  val canPost: Boolean,
    @JsonProperty("count")  val count: Int,
    @JsonProperty("groups_can_post")  val groupsCanPost: Boolean,
    @JsonProperty("items")  val items: List<Any>,
    @JsonProperty("show_reply_button")  val showReplyButton: Boolean
)