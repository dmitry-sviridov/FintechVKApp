package ru.sviridov.network.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import ru.sviridov.network.model.Group
import ru.sviridov.network.model.NewsFeed
import ru.sviridov.network.model.Profile

@JsonRootName(value = "response")
data class NewsResponse(
    val items: List<NewsFeed> = emptyList(),
    val profiles: List<Profile> = emptyList(),
    val groups: List<Group> = emptyList(),
    @JsonProperty("next_from") val nextFrom: String?
)