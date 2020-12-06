package ru.sviridov.vkclient.ui.presentation.viewmodel.newsfeed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature.newsfeed.FeedItemsDirection
import ru.sviridov.vkclient.feature.newsfeed.domain.NewsFeedRepository
import ru.sviridov.vkclient.ui.presentation.mvi.newsfeed.FeedViewActions
import ru.sviridov.vkclient.ui.presentation.mvi.newsfeed.FeedViewState
import javax.inject.Inject

class FeedViewModel @Inject constructor(private val feedRepository: NewsFeedRepository) :
    ViewModel() {

    private val TAG = FeedViewModel::class.simpleName + "@" + this.hashCode()

    private var shouldRecyclerBeScrolled = false
    private val compositeDisposable = CompositeDisposable()

    private var isRequestIsFirst = true

    val viewState: MutableLiveData<FeedViewState> = MutableLiveData()

    fun handleAction(action: FeedViewActions) {
        Log.d(TAG, "handleAction: $action")
        when (action) {
            FeedViewActions.GetFreshNews -> fetchNewsUpdates(FeedItemsDirection.FRESH)
            FeedViewActions.GetPreviousNews -> fetchNewsUpdates(FeedItemsDirection.PREVIOUS)
            FeedViewActions.GetLikedNews -> startObservingLikedNewsFromDBToUIState()
            is FeedViewActions.SetCurrentItemAsLiked -> changeItemLikeStatus(action.item, true)
            is FeedViewActions.SetCurrentItemAsDisliked -> changeItemLikeStatus(action.item, false)
            is FeedViewActions.HideCurrentItem -> markItemAsHidden(action.item)
        }
    }

    private fun startObservingCurrentSessionDataSourceToUIState() {
        Log.d(TAG, "startObservingCurrentSessionDataSourceToUIState")
        val newsDisposable = feedRepository.fetchNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { list ->
                viewState.value = FeedViewState.ShowNewsFeed(
                    newsList = list,
                    scrollRecyclerUp = shouldRecyclerBeScrolled
                )
                Log.d(TAG, "newsDisposable :onNext, firstElement = ${list.first().postId}")

            }, onError = { throwable ->
                viewState.value = FeedViewState.StopLoading
                Log.e(TAG, "newsDisposable :onError => ${throwable.message} ")
            })
        compositeDisposable.add(newsDisposable)
    }

    private fun startObservingLikedNewsFromDBToUIState() {
        val likedNewsDisposable = feedRepository
            .fetchLikedFromDB()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { list ->
                Log.d(TAG, "likesDisposable :onNext, firstElement = ${list.first().postId}")
                viewState.value = FeedViewState.ShowNewsFeed(
                    newsList = list,
                    scrollRecyclerUp = false
                )
            }, onError = { throwable ->
                viewState.value = FeedViewState.StopLoading
                Log.d(TAG, "likesDisposable :onError => ${throwable.message} ")
            })
        compositeDisposable.add(likedNewsDisposable)
    }

    private fun fetchNewsUpdates(direction: FeedItemsDirection) {
        Log.d(TAG, "fetchNewsUpdates: $direction")
        viewState.value = FeedViewState.Loading
        if (isRequestIsFirst) {
            startObservingCurrentSessionDataSourceToUIState()
            isRequestIsFirst = false // Для предотвращения создания лишних подписок
        }
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
        compositeDisposable.dispose()
        super.onCleared()
    }

}
