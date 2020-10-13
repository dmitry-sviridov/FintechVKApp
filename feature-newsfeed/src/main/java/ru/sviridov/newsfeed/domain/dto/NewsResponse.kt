package ru.sviridov.newsfeed.domain.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import ru.sviridov.newsfeed.domain.model.Group
import ru.sviridov.newsfeed.domain.model.NewsFeed

@JsonRootName(value = "response")
data class NewsResponse(
    val items: List<NewsFeed> = emptyList(),
    val profiles: List<Any> = emptyList(),
    val groups: List<Group> = emptyList(),
    @JsonProperty("next_from") val nextFrom: String?
)
