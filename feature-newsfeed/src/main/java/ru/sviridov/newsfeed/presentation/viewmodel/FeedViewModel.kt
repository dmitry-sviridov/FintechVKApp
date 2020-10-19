package ru.sviridov.newsfeed.presentation.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.newsfeed.domain.implementation.NewsFeedRepositoryFakeImpl
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

class FeedViewModel(assetManager: AssetManager) : ViewModel() {

    private val feedRepository = NewsFeedRepositoryFakeImpl(assetManager = assetManager)

    val newsItems = MutableLiveData<List<NewsItem>>()
    val likedItems = MutableLiveData<List<NewsItem>>()

    private val compositeDisposable = CompositeDisposable()

    fun updateFeed() {
        val newsDisposable = feedRepository.fetchNews(null)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { list ->
                likedItems.value = list.filter { it.isLiked == true }.toList()
                newsItems.value = list
            }
        compositeDisposable.add(newsDisposable)
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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

class FeedViewModelFactory(private val assetManager: AssetManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(assetManager) as T
}
