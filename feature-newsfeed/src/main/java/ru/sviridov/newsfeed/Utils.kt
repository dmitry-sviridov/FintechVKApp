package ru.sviridov.newsfeed

import android.content.res.AssetManager
import ru.sviridov.newsfeed.domain.dto.NewsResponse
import ru.sviridov.newsfeed.presentation.adapter.NewsFeedViewType
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import java.util.*
import kotlin.math.abs

fun fromFile(fileName: String, assetManager: AssetManager): String =
    assetManager.open(fileName).bufferedReader().use { it.readText() }


// TODO: Denis, don't watch here till we don't have issue about real API calls
fun mapResponseToItem(response: NewsResponse): List<NewsItem> {
    val newsItems = response.items.asSequence().map {
        NewsItem(
            postId = it.postId,
            sourceTitle = it.sourceId.toString(),
            postedAt = it.date.toString(),
            sourceAvatar = "",
            imageUrl = "",
            textContent = it.text,
            likesCount = it.likes?.count ?: 0,
            shareCount = it.reposts?.count ?: 0,
            commentCount = it.comments?.count ?: 0,
            viewsCount = it.views?.count ?: 0
        )
    }.toMutableList()

    newsItems.forEach {
        it.sourceTitle =
            response.groups.first { group -> group.id == abs(it.sourceTitle.toInt()) }.name
        it.sourceAvatar =
            response.groups.first { group -> group.name == it.sourceTitle }.photoWithSize50
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

fun NewsItem.getPostedAtDate(): Date {
    var result: Date
    try {
        result = Date(postedAt.toLong() * 1000)
    } catch (e: Exception) {
        e.printStackTrace()
        result = Date()
    }
    return result
}
