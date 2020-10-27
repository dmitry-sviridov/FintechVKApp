package ru.sviridov.newsfeed.domain.implementation.stub

import android.content.res.AssetManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.network.dto.NewsResponse
import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.data.NewsConverterImpl
import ru.sviridov.newsfeed.domain.FeedItemsDirection
import ru.sviridov.newsfeed.domain.NewsFeedRepository
import ru.sviridov.newsfeed.fromFile
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import java.util.concurrent.Callable

internal class NewsFeedRepositoryFakeImpl(private val assetManager: AssetManager) :
    NewsFeedRepository {

    private val dataSource = FakeDataSource
    private val likedNewsListSubject = BehaviorSubject.create<MutableList<NewsItem>>()
    private val mapper = jacksonObjectMapper()
    private val converter = NewsConverterImpl()

    override fun fetchNews(): Observable<List<NewsItem>> {
        return dataSource.newsListSubject.map { mutable -> mutable.toList() }
    }

    override fun updateNews(timeDirection: FeedItemsDirection) {
        if (dataSource.newsItems.isEmpty()) {
            run {
                Single
                    .fromCallable(readFromJson())
                    .subscribeOn(Schedulers.computation())
                    .map { response -> converter.convert(response) as MutableList<NewsItem> }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result ->
                        dataSource.newsItems = result
                        dataSource.newsListSubject.onNext(dataSource.newsItems)
                        likedNewsListSubject.onNext(result.filter { item -> item.isLiked == true }
                            .toMutableList())
                    }
            }
        }
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
        val jsonString = fromFile("posts2.json", assetManager = assetManager)
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
        return@Callable mapper.readValue<NewsResponse>(jsonString)
    }
}
