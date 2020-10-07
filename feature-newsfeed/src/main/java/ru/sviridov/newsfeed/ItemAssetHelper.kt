package ru.sviridov.newsfeed

import android.content.res.AssetManager
import ru.sviridov.newsfeed.domain.dto.NewsResponse
import ru.sviridov.newsfeed.presentation.adapter.NewsFeedViewType
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import kotlin.math.abs

fun fromFile(fileName: String, assetManager: AssetManager): String =
    assetManager.open(fileName).bufferedReader().use { it.readText() }


// TODO: refactor this bullshit after migration to Rx
fun mapResponseToItem(response: NewsResponse): List<NewsItem> {
    val newsItems = response.items.asSequence().map {
        NewsItem(
            it.source_id.toString(),
            it.date.toString(),
            "",
            "",
            it.text,
            it.likes?.count ?: 0,
            it.reposts?.count ?: 0,
            it.comments?.count ?: 0,
            it.views?.count ?: 0
        )
    }.toMutableList()

    newsItems.forEach {
        it.sourceTitle =
            response.groups.first { group -> group.id == abs(it.sourceTitle.toInt()) }.name
        it.sourceAvatar = response.groups.first { group -> group.name == it.sourceTitle }.photo_50
        it.imageUrl = response.items.firstOrNull { item ->
            item.text == it.textContent
        }?.attachments?.firstOrNull { attachment ->
            attachment.type == "photo"
        }?.photo?.sizes?.maxByOrNull { by -> by.height }?.url
    }

    return newsItems
}

fun NewsItem.getItemType(): NewsFeedViewType {
    when {
        this.textContent.isNullOrEmpty() -> {
            if (!this.imageUrl.isNullOrBlank()) return NewsFeedViewType.VIEW_WITH_SINGLE_PICTURE_ONLY
        }
        else -> {
            return if (this.imageUrl.isNullOrEmpty()) {
                NewsFeedViewType.VIEW_WITH_TEXT_ONLY
            } else {
                NewsFeedViewType.VIEW_WITH_SINGLE_PICTURE_AND_TEXT
            }
        }
    }
    return NewsFeedViewType.UNKNOWN
}