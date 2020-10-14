package ru.sviridov.newsfeed.presentation.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sviridov.newsfeed.domain.implementation.NewsFeedRepositoryFakeImpl
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

class FeedViewModel(assetManager: AssetManager) : ViewModel() {

    private val feedRepository = NewsFeedRepositoryFakeImpl(assetManager = assetManager)

    val newsItems = feedRepository.newsItems

    val likedItems = feedRepository.likedItems

    fun updateFeed() {
        feedRepository.fetchNews(null)
    }

    fun markItemAsHidden(item: NewsItem) {
        feedRepository.setItemAsHidden(item)
    }

    fun changeItemLikeStatus(newsItem: NewsItem, shouldBeLiked: Boolean) {
        if (shouldBeLiked) {
            feedRepository.setNewsItemLiked(newsItem)
        } else {
            feedRepository.setNewsItemDisliked(newsItem)
        }
    }
}

class FeedViewModelFactory(private val assetManager: AssetManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(assetManager) as T
}
