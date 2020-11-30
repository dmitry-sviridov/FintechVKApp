package ru.sviridov.vkclient.feature_newsfeed.domain

import io.reactivex.Observable
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature_newsfeed.FeedItemsDirection

interface NewsFeedRepository {

    fun fetchNews(): Observable<List<NewsItem>>

    fun fetchLikedFromDB(): Observable<List<NewsItem>>

    fun updateNews(timeDirection: FeedItemsDirection)

    fun setNewsItemLiked(item: NewsItem)

    fun setNewsItemDisliked(item: NewsItem)

    fun setItemAsHidden(item: NewsItem)

    fun onCleared()
}