package ru.sviridov.vkclient.feature.newsfeed.domain.implementation

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature.newsfeed.FeedItemsDirection
import ru.sviridov.vkclient.feature.newsfeed.data.CurrentSessionDataSource
import ru.sviridov.vkclient.feature.newsfeed.data.db.dao.LikedNewsItemDao
import ru.sviridov.vkclient.feature.newsfeed.domain.NewsConverter
import ru.sviridov.vkclient.feature.newsfeed.domain.NewsFeedRepository
import ru.sviridov.vkclient.network.response.NewsResponse
import ru.sviridov.vkclient.network.service.NewsFeedService
import ru.sviridov.vkclient.network.service.PostLikesService
import javax.inject.Inject

internal class NewsFeedRepositoryImpl @Inject constructor(
    private val apiService: NewsFeedService,
    private val likesService: PostLikesService,
    private val converter: NewsConverter,
    private val likedDao: LikedNewsItemDao
) : NewsFeedRepository {

    private val dataSource = CurrentSessionDataSource
    private val compositeDisposable = CompositeDisposable()

    override fun updateNews(timeDirection: FeedItemsDirection) {
        val newsResponse: Single<NewsResponse> = if (timeDirection == FeedItemsDirection.PREVIOUS &&
            dataSource.nextFrom != null
        ) {
            Log.d(TAG, "updateNews from ${dataSource.nextFrom}")
            apiService.getNews(dataSource.nextFrom!!)
        } else {
            Log.d(TAG, "updateNews")
            apiService.getNews()
        }

        val responseDisposable = newsResponse.subscribeOn(Schedulers.io())
            .doOnSuccess { dataSource.nextFrom = it.nextFrom }
            .map { resp -> converter.convertApiResponseToUi(resp) }
            .doAfterSuccess { list ->
                list
                    .filter { item -> item.isLiked == true }
                    .forEach { item -> insertItemToDB(item) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                Log.d(TAG, "responseDisposable: onError")
            }, onSuccess = { responseItems ->
                Log.d(TAG, "responseDisposable: onSuccess")
                if (timeDirection == FeedItemsDirection.PREVIOUS) {
                    dataSource.insertNewsItemsToSubjectBefore(responseItems)
                } else {
                    dataSource.insertNewsItemsToSubjectAfter(responseItems)
                }
            })

        compositeDisposable.add(responseDisposable)
    }

    override fun fetchNews(): Observable<List<NewsItem>> {
        Log.d(TAG, "fetchNews")
        return dataSource
            .newsListSubject
    }

    override fun fetchLikedFromDB(): Observable<List<NewsItem>> {
        Log.d(TAG, "fetchLikedFromDB")
        return likedDao
            .getAllLiked()
            .subscribeOn(Schedulers.io())
            .flatMap {
                Observable.fromIterable(it)
                    .map { entity ->
                        converter.convertDbToUi(entity)
                    }
                    .toList()
                    .toObservable()
                    .onErrorReturnItem(emptyList())
            }
    }

    override fun setNewsItemLiked(item: NewsItem) {
        val likedDisposable = likesService.setItemAsLiked(item.sourceId, item.postId)
            .subscribeOn(Schedulers.io())
            .doFinally {
                item.isLiked = true
                item.likesCount++
                insertItemToDB(item)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                dataSource.newsListSubject.value?.let { list ->
                    val newList = list.toMutableList()
                    dataSource.newsListSubject.onNext(newList)
                    Log.d(TAG, "setNewsItemLiked: likedDisposable on Success")
                }
            }, onError = {
                Log.d(TAG, "setNewsItemLiked: likedDisposable on Error")
            })

        compositeDisposable.add(likedDisposable)
    }

    override fun setNewsItemDisliked(item: NewsItem) {
        val dislikedDisposable = likesService.setItemAsDisliked(item.sourceId, item.postId)
            .subscribeOn(Schedulers.io())
            .doFinally {
                removeItemFromDb(item)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d(TAG, "setNewsItemDisliked: dislikedDisposable onSuccess")
                dataSource.updateNewsItemWithDislike(item)
            }, onError = {
                Log.d(TAG, "setNewsItemDisliked: dislikedDisposable onError")
            })

        compositeDisposable.add(dislikedDisposable)
    }

    override fun setItemAsHidden(item: NewsItem) {
        //TODO: Api call to hide element from feed
        dataSource.removeItem(item)
        removeItemFromDb(item)
    }

    private fun insertItemToDB(item: NewsItem) {
        compositeDisposable.add(
            likedDao
                .insertNewsItem(converter.convertUiToDb(item))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    Log.d(TAG, "insertItemToDB: onSuccess")
                }, onError = {
                    Log.d(TAG, "insertItemToDB: onError")
                })
        )
    }

    private fun removeItemFromDb(item: NewsItem) {
        compositeDisposable.add(
            likedDao
                .deleteNewsItem(converter.convertUiToDb(item))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    Log.d(TAG, "removeItemFromDb: onSuccess")
                }, onError = {
                    Log.d(TAG, "removeItemFromDb: onError")
                })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

    companion object {
        private val TAG = "NewsFeedRepositoryImpl" + "@" + this.hashCode()
    }
}
