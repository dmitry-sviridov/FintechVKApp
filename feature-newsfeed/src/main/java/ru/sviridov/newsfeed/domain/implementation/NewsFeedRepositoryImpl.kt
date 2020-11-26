package ru.sviridov.newsfeed.domain.implementation

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
import ru.sviridov.network.service.NewsFeedService
import ru.sviridov.newsfeed.data.CurrentSessionDataSource
import ru.sviridov.newsfeed.data.NewsConverterImpl
import ru.sviridov.newsfeed.data.db.dao.LikedNewsItemDao
import ru.sviridov.newsfeed.domain.FeedItemsDirection
import ru.sviridov.newsfeed.domain.NewsFeedRepository

internal class NewsFeedRepositoryImpl(
    private val apiService: NewsFeedService
)
//    private val likesService: PostLikesService,
//    private val converter: NewsConverter,
//    private val likedDao: LikedNewsItemDao
    : NewsFeedRepository {

    private val dataSource = CurrentSessionDataSource

    //    private val apiService = ApiServicesProvider.getNewsFeedApiService()
    private val likesService = ApiServicesProvider.getPostLikesApiService()
    private val converter = NewsConverterImpl()
    private lateinit var likedDao: LikedNewsItemDao

    private val compositeDisposable = CompositeDisposable()


    override fun updateNews(timeDirection: FeedItemsDirection) {
        val newsRequest: Single<NewsResponse> = if (timeDirection == FeedItemsDirection.PREVIOUS &&
            dataSource.nextFrom != null
        ) {
            apiService.getNews(dataSource.nextFrom!!)
        } else {
            apiService.getNews()
        }

        val responseDisposable = newsRequest.subscribeOn(Schedulers.io())
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
                    dataSource.insertNewsItemsToSubjectBefore(responseItems)
                } else {
                    dataSource.insertNewsItemsToSubjectAfter(responseItems)
                }
            })

        compositeDisposable.add(responseDisposable)
    }

    override fun fetchNews(): Observable<List<NewsItem>> {
        return dataSource
            .newsListSubject
            .subscribeOn(Schedulers.io())
    }

    override fun fetchLikedFromDB(): Observable<List<NewsItem>> {
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
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                Log.d("RX", "setNewsItemDisliked ${it.likes}")
                dataSource.updateNewsItemWithDislike(item)
            }, onError = {
                Log.d("RX", "setNewsItemDisliked: ${it.message}")
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

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
