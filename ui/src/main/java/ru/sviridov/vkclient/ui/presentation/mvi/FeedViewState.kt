package ru.sviridov.vkclient.ui.presentation.mvi

import ru.sviridov.component.feeditem.model.NewsItem

sealed class FeedViewState {
    object Loading: FeedViewState()
    class ShowApiError(val apiError: Throwable): FeedViewState()
    class ShowNewsFeed(
        val newsList: List<NewsItem>,
        val scrollRecyclerUp: Boolean
    ): FeedViewState()
}