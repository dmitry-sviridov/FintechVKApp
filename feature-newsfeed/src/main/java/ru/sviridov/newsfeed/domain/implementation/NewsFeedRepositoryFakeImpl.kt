package ru.sviridov.newsfeed.domain.implementation

import android.content.res.AssetManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.domain.NewsFeedRepository
import ru.sviridov.newsfeed.domain.dto.NewsResponse
import ru.sviridov.newsfeed.fromFile
import ru.sviridov.newsfeed.mapResponseToItem
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import java.util.concurrent.Callable

internal class NewsFeedRepositoryFakeImpl(private val assetManager: AssetManager) :
    NewsFeedRepository {

    private val dataSource = FakeDataSource
    private val likedNewsListSubject = BehaviorSubject.create<MutableList<NewsItem>>()

    override fun fetchNews(filter: Any?): Observable<List<NewsItem>> {
        if (dataSource.newsItems.isEmpty()) {
            run {
                Single
                    .fromCallable(readFromJson())
                    .subscribeOn(Schedulers.computation())
                    .map { response -> mapResponseToItem(response) as MutableList<NewsItem> }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        dataSource.newsItems = result
                        dataSource.newsListSubject.onNext(dataSource.newsItems)
                        likedNewsListSubject.onNext(result.filter { item -> item.isLiked == true }
                            .toMutableList())
                    }
            }
        }
        return dataSource.newsListSubject.map { mutable -> mutable.toList() }
    }

    override fun setNewsItemLiked(item: NewsItem) {
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

    private fun readFromJson(): Callable<NewsResponse> = Callable {

//        if (Random.nextInt(10) > 5) {
//            throw RuntimeException("Random network error imitation")
//        }

        val jsonString = fromFile("posts.json", assetManager = assetManager)
        val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
        return@Callable mapper.readValue<NewsResponse>(jsonString)
    }
}
