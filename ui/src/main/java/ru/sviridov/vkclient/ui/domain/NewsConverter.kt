package ru.sviridov.vkclient.ui.domain

import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.network.dto.NewsResponse
import ru.sviridov.vkclient.ui.data.db.entity.NewsItemEntity

interface NewsConverter {
    fun convertApiResponseToUi(dto: NewsResponse): List<NewsItem>
    fun convertDbToUi(entity: NewsItemEntity): NewsItem
    fun convertUiToDb(item: NewsItem): NewsItemEntity
}
