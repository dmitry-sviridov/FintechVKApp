package ru.sviridov.newsfeed.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.newsfeed.domain.FeedItemsDirection
import ru.sviridov.newsfeed.domain.implementation.NewsFeedRepositoryImpl
import ru.sviridov.newsfeed.presentation.FeedViewActions
import ru.sviridov.newsfeed.presentation.FeedViewState

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val feedRepository = NewsFeedRepositoryImpl(application)
    private var shouldRecyclerBeScrolled = false

    val viewState: MutableLiveData<FeedViewState> = MutableLiveData()

    private val newsDisposable = feedRepository.fetchNews()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(onNext = { list ->
            viewState.value = FeedViewState.ShowNewsFeed(
                newsList = list,
                scrollRecyclerUp = shouldRecyclerBeScrolled
            )
            Log.d(TAG, "newsDisposable :onNext, firstElement = ${list.first().postId}")

        }, onError = { throwable ->
            viewState.value = FeedViewState.ShowApiError(throwable)
            Log.d(TAG, "newsDisposable :onError => ${throwable.message} ")
        })

    fun handleAction(action: FeedViewActions) {
        Log.d(TAG, "handleAction: $action")
        when (action) {
            FeedViewActions.GetFreshNews -> fetchNewsUpdates(FeedItemsDirection.FRESH)
            FeedViewActions.GetPreviousNews -> fetchNewsUpdates(FeedItemsDirection.PREVIOUS)
            is FeedViewActions.SetCurrentItemAsLiked -> changeItemLikeStatus(action.item, true)
            is FeedViewActions.SetCurrentItemAsDisliked -> changeItemLikeStatus(action.item, false)
            is FeedViewActions.HideCurrentItem -> markItemAsHidden(action.item)
        }
    }

    fun fetchNewsUpdates(direction: FeedItemsDirection) {
        Log.d(TAG, "fetchNewsUpdates: $direction")
        viewState.value = FeedViewState.Loading
        shouldRecyclerBeScrolled = direction == FeedItemsDirection.FRESH
        feedRepository.updateNews(direction)
    }

    private fun markItemAsHidden(item: NewsItem) {
        Log.d(TAG, "markItemAsHidden: postId = ${item.postId}")
        shouldRecyclerBeScrolled = false
        feedRepository.setItemAsHidden(item)
    }

    private fun changeItemLikeStatus(newsItem: NewsItem, shouldBeLiked: Boolean) {
        Log.d(TAG, "changeItemLikeStatus: postId = ${newsItem.postId}, liked = $shouldBeLiked")
        shouldRecyclerBeScrolled = false
        if (shouldBeLiked) {
            feedRepository.setNewsItemLiked(newsItem)
        } else {
            feedRepository.setNewsItemDisliked(newsItem)
        }
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
        feedRepository.onCleared()
        newsDisposable.dispose()
    }

    companion object {
        private val TAG = FeedViewModel::class.simpleName
    }
}

class FeedViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(application) as T
}
