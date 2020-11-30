package ru.sviridov.vkclient.ui.presentation.mvi

import ru.sviridov.component.feeditem.model.NewsItem

sealed class FeedViewActions {
    object GetFreshNews: FeedViewActions()
    object GetPreviousNews : FeedViewActions()
    object GetLikedNews : FeedViewActions()
    class SetCurrentItemAsLiked(val item: NewsItem): FeedViewActions()
    class SetCurrentItemAsDisliked(val item: NewsItem): FeedViewActions()
    class HideCurrentItem(val item: NewsItem): FeedViewActions()
}