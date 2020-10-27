package ru.sviridov.newsfeed.domain

import io.reactivex.Observable
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

interface NewsFeedRepository {

    fun fetchNews(): Observable<List<NewsItem>>

    fun updateNews(timeDirection: FeedItemsDirection)

    fun setNewsItemLiked(item: NewsItem)

    fun setNewsItemDisliked(item: NewsItem)

    fun setItemAsHidden(item: NewsItem)
}