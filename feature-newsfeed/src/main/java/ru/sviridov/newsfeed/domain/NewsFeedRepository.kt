package ru.sviridov.newsfeed.domain

import ru.sviridov.newsfeed.domain.dto.NewsResponse
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

interface NewsFeedRepository {

    // will be changed later after migrating to Rx
    fun fetchNews(filter: Any?): NewsResponse
}