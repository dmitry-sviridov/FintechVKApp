package ru.sviridov.newsfeed

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sviridov.newsfeed.domain.implementation.NewsFeedRepositoryFakeImpl
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

class FeedViewModel(assetManager: AssetManager): ViewModel() {

    private val feedRepository = NewsFeedRepositoryFakeImpl(assetManager = assetManager)

    private var newsItems = MutableLiveData<List<NewsItem>>()

    fun getFeedItems() {
        val newsResponse = feedRepository.fetchNews(null)
        val items = mapResponseToItem(newsResponse)
        newsItems.value = items
    }

}

class FeedViewModelFactory(private val assetManager: AssetManager): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(assetManager) as T
}
