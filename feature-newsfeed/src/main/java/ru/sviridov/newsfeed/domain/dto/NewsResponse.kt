package ru.sviridov.newsfeed.domain.dto

import com.fasterxml.jackson.annotation.JsonRootName
import ru.sviridov.newsfeed.domain.model.Group
import ru.sviridov.newsfeed.domain.model.NewsFeed

@JsonRootName(value = "response")
data class NewsResponse(
    var items: List<NewsFeed> = emptyList(),
    var profiles: List<Any> = emptyList(),
    var groups: List<Group> = emptyList(),
    var next_from: String?
)
