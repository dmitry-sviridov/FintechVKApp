package ru.sviridov.newsfeed.domain

import ru.sviridov.network.dto.NewsResponse
import ru.sviridov.newsfeed.data.db.item.NewsItem

interface NewsConverter {
    fun convert(dto: NewsResponse): List<NewsItem>
}
