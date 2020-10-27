package ru.sviridov.newsfeed.presentation.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.newsfeed.domain.FeedItemsDirection
import ru.sviridov.newsfeed.domain.implementation.NewsFeedRepositoryImpl
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

class FeedViewModel(assetManager: AssetManager) : ViewModel() {

    private val feedRepository = NewsFeedRepositoryImpl()
//    private val feedRepository = NewsFeedRepositoryFakeImpl(assetManager = assetManager)

    val newsItems = MutableLiveData<List<NewsItem>>()
    val likedItems = MutableLiveData<List<NewsItem>>()
    val isErrorState = MutableLiveData<Boolean>()
    val recyclerScrollUp = MutableLiveData<Boolean>()

    val newsDisposable = feedRepository.fetchNews()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(onNext = { list ->
            likedItems.value = list.filter { it.isLiked == true }.toList()
            newsItems.value = list
            isErrorState.value = false
        }, onError = {
            isErrorState.value = true
        })

    fun uploadFeedItems(direction: FeedItemsDirection) {
        feedRepository.updateNews(direction)
        if (direction == FeedItemsDirection.FRESH) {
            recyclerScrollUp.value = true
        }
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
        newsDisposable.dispose()
    }
}

class FeedViewModelFactory(private val assetManager: AssetManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(assetManager) as T
}
