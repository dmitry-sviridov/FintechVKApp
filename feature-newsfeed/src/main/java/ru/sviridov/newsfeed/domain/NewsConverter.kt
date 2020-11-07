package ru.sviridov.newsfeed.domain

import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.network.dto.NewsResponse
import ru.sviridov.newsfeed.data.db.entity.NewsItemEntity

interface NewsConverter {
    fun convertApiResponseToUi(dto: NewsResponse): List<NewsItem>
    fun convertDbToUi(entity: NewsItemEntity): NewsItem
    fun convertUiToDb(item: NewsItem): NewsItemEntity
}
