package ru.sviridov.newsfeed.presentation

import ru.sviridov.component.feeditem.model.NewsItem

interface AdapterActionHandler: OnAdapterClickListener, AdapterCallback

interface AdapterCallback {
    fun onItemHided(item: NewsItem)
    fun onItemLiked(item: NewsItem, shouldBeLiked: Boolean)
}

interface OnAdapterClickListener {
    fun onImageViewClicked(url: String)
}
