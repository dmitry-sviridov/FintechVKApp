package ru.sviridov.vkclient.ui.presentation.mvi.newsfeed

import ru.sviridov.component.feeditem.model.NewsItem

sealed class FeedViewState {
    object Loading : FeedViewState()
    object StopLoading : FeedViewState()
    class ShowError(val error: Throwable) : FeedViewState()
    class ShowNewsFeed(
        val newsList: List<NewsItem>,
        val scrollRecyclerUp: Boolean
    ) : FeedViewState()
}