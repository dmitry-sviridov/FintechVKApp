package ru.sviridov.newsfeed.domain.implementation

import android.app.Application
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.network.ApiServicesProvider
import ru.sviridov.network.dto.NewsResponse
import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.data.NewsConverterImpl
import ru.sviridov.newsfeed.data.db.NewsDatabase
import ru.sviridov.newsfeed.domain.FeedItemsDirection
import ru.sviridov.newsfeed.domain.NewsFeedRepository

internal class NewsFeedRepositoryImpl(application: Application) : NewsFeedRepository {

    private val dataSource = FakeDataSource
    private val apiService = ApiServicesProvider.getNewsFeedApiService()
    private val likesService = ApiServicesProvider.getPostLikesApiService()
    private val converter = NewsConverterImpl()
    private val likedDao = NewsDatabase.getDatabase(application).likedDao()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            likedDao.getAllLiked()
                .subscribeOn(Schedulers.io())
                .toObservable()
                .flatMap { likedList -> Observable.fromIterable(likedList) }
                .map { entity -> converter.convertDbToUi(entity) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = { list ->
                    dataSource.newsListSubject.onNext(list)
                }, onError = { error ->
                    Log.d("RX", "init: error = ${error.message}")
                })
        )
    }

    override fun updateNews(timeDirection: FeedItemsDirection) {
        var response: Single<NewsResponse>? = null
        if (timeDirection == FeedItemsDirection.PREVIOUS) {
            dataSource.nextFrom?.let { nextFrom ->
                response = apiService.getNews(nextFrom)
            }
        } else {
            response = apiService.getNews()
        }

        response?.let {
            it.subscribeOn(Schedulers.io())
                .doOnSuccess { dataSource.nextFrom = it.nextFrom }
                .map { resp -> converter.convertApiResponseToUi(resp) }
                .doAfterSuccess { list ->
                    list
                        .filter { item -> item.isLiked == true }
                        .forEach { item -> insertItemToDB(item) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    Log.d("RX", "fetch: ${it.message}")
                }, onSuccess = { responseItems ->
                    if (timeDirection == FeedItemsDirection.PREVIOUS) {
                        dataSource.newsListSubject.value?.let { existingList ->
                            val newList = existingList.toMutableList()
                            newList.addAll(responseItems)
                            dataSource.newsListSubject.onNext(newList)
                        } ?: dataSource.newsListSubject.onNext(responseItems)
                    } else {
                        dataSource.newsListSubject.value?.let { existingList ->
                            responseItems.toMutableList().addAll(existingList)
                        }
                        dataSource.newsListSubject.onNext(responseItems.distinct())
                    }
                })
        }
    }

    override fun fetchNews(): Observable<List<NewsItem>> {
        return dataSource.newsListSubject
    }

    override fun setNewsItemLiked(item: NewsItem) {
        val likedDisposable = likesService.setItemAsLiked(item.sourceId, item.postId)
            .subscribeOn(Schedulers.io())
            .doFinally {
                item.isLiked = true
                item.likesCount ++
                insertItemToDB(item)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                dataSource.newsListSubject.value?.let { list ->
                    val newList = list.toMutableList()
                    dataSource.newsListSubject.onNext(newList)
                }
            }, onError = {
                Log.d("RX", "setNewsItemDisliked: ${it.message}")
            })

        compositeDisposable.add(likedDisposable)
    }

    override fun setNewsItemDisliked(item: NewsItem) {
        val dislikedDisposable = likesService.setItemAsDisliked(item.sourceId, item.postId)
            .subscribeOn(Schedulers.io())
            .doFinally {
                removeItemFromDb(item)
                item.isLiked = false
                item.likesCount --
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d("RX", "setNewsItemDisliked ${it.likes}")
                dataSource.newsListSubject.value?.let { list ->
                    val newList = list.toMutableList()
                    dataSource.newsListSubject.onNext(newList)
                }
            }, onError = {
                Log.d("RX", "setNewsItemDisliked: ${it.message}")
            })

        compositeDisposable.add(dislikedDisposable)
    }

    override fun setItemAsHidden(item: NewsItem) {
        //TODO: Api call to hide element from feed
        dataSource.newsListSubject.value?.let { list ->
            val newList = list.toMutableList()
            newList.remove(newList.find { newsItem -> newsItem.postId == item.postId })
            dataSource.newsListSubject.onNext(newList)
        }
    }

    private fun insertItemToDB(item: NewsItem) {
        compositeDisposable.add(
            likedDao
                .insertNewsItem(converter.convertUiToDb(item))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    Log.d(
                        "ROOM/RX",
                        "insertItemToDB: post with id=${item.postId} has been inserted"
                    )
                }, onError = {
                    Log.d(
                        "ROOM/RX",
                        "insertItemToDB: error during insert post with id=${item.postId} "
                    )
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
                    Log.d(
                        "ROOM/RX",
                        "removeItemFromDb: post with id=${item.postId} has been deleted"
                    )
                }, onError = {
                    Log.d(
                        "ROOM/RX",
                        "removeItemFromDb: error during deleting post with id=${item.postId} "
                    )
                })
        )
    }

    fun onCleared() {
        compositeDisposable.clear()
    }
}
