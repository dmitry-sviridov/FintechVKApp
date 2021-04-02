package ru.sviridov.vkclient.ui.presentation.mvi.wall

import ru.sviridov.component.feeditem.model.NewsItem

sealed class WallViewState {
    object ShowError: WallViewState()
    object ShowEmptyState: WallViewState()
    class ShowWallItems(val items: List<NewsItem>): WallViewState()

}