package ru.sviridov.newsfeed.domain.implementation

import android.app.Application
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.network.ApiServicesProvider
import ru.sviridov.network.dto.NewsResponse
import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.data.NewsConverterImpl
import ru.sviridov.newsfeed.data.db.NewsDatabase
import ru.sviridov.newsfeed.data.db.item.NewsItem
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
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onSuccess = {
                    dataSource.newsItems.addAll(it)
                }, onError = {
                    Log.d("ROOm/RX", "init: error catched. ${it.message}")
                })
        )
    }

    override fun updateNews(timeDirection: FeedItemsDirection) {
        var response: Single<NewsResponse>? = null
        if (timeDirection == FeedItemsDirection.PREVIOUS) {
            if (dataSource.nextFrom != null) {
                response = apiService.getNews(dataSource.nextFrom!!)
            }
        } else {
            response = apiService.getNews()
        }

        response?.let {
            it.subscribeOn(Schedulers.io())
                .doOnSuccess { dataSource.nextFrom = it.nextFrom }
                .map { resp -> converter.convert(resp) }
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
                        dataSource.newsItems.addAll(responseItems)
                    } else {
                        (responseItems as MutableList<NewsItem>).addAll(dataSource.newsItems)
                        dataSource.newsItems = responseItems.distinct().toMutableList()
                    }
                    dataSource.newsListSubject.onNext(dataSource.newsItems.distinct() as MutableList<NewsItem>)
                })
        }
    }

    override fun fetchNews(): Observable<List<NewsItem>> {
        return dataSource.newsListSubject.map { mutable -> mutable.toList() }
    }

    override fun setNewsItemLiked(item: NewsItem) {
        val likedDisposable = likesService.setItemAsLiked(item.sourceId, item.postId)
            .subscribeOn(Schedulers.io())
            .doFinally { insertItemToDB(item) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d("RX", "setNewsItemDisliked: ${it.likes}")
            }, onError = {
                Log.d("RX", "setNewsItemDisliked: ${it.message}")
            })

        compositeDisposable.add(likedDisposable)

        val itemToLike =
            dataSource.newsItems.find { newsItem -> newsItem.postId == item.postId }
        itemToLike?.let { newsFeedItem ->
            with(newsFeedItem) {
                isLiked = true
                likesCount++
            }
            dataSource.newsListSubject.onNext(dataSource.newsItems)
        }
    }

    override fun setNewsItemDisliked(item: NewsItem) {
        val dislikedDisposable = likesService.setItemAsDisliked(item.sourceId, item.postId)
            .subscribeOn(Schedulers.io())
            .doFinally { removeItemFromDb(item) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d("RX", "setNewsItemDisliked: ${it.likes}")
            }, onError = {
                Log.d("RX", "setNewsItemDisliked: ${it.message}")
            })

        compositeDisposable.add(dislikedDisposable)

        val itemToDislike =
            dataSource.newsItems.find { newsItem -> newsItem.postId == item.postId }
        itemToDislike?.let { newsFeedItem ->
            with(newsFeedItem) {
                isLiked = false
                likesCount--
            }
            dataSource.newsListSubject.onNext(dataSource.newsItems)
        }
    }

    override fun setItemAsHidden(item: NewsItem) {
        dataSource.newsItems.remove(item)
        dataSource.newsListSubject.onNext(dataSource.newsItems)
    }

    fun insertItemToDB(item: NewsItem) {
        compositeDisposable.add(
            likedDao
                .insertNewsItem(item)
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

    fun removeItemFromDb(item: NewsItem) {
        compositeDisposable.add(
            likedDao
                .deleteNewsItem(item)
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
