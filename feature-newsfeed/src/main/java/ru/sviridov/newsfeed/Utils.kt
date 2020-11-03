package ru.sviridov.newsfeed

import android.content.res.AssetManager
import ru.sviridov.newsfeed.data.db.item.NewsItem
import ru.sviridov.newsfeed.presentation.adapter.NewsFeedViewType
import java.util.*

fun fromFile(fileName: String, assetManager: AssetManager): String =
    assetManager.open(fileName).bufferedReader().use { it.readText() }

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
