package ru.sviridov.newsfeed.domain

import io.reactivex.Observable
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

interface NewsFeedRepository {

    fun fetchNews(filter: Any?): Observable<List<NewsItem>>

    fun setNewsItemLiked(item: NewsItem)

    fun setNewsItemDisliked(item: NewsItem)

    fun setItemAsHidden(item: NewsItem)
}