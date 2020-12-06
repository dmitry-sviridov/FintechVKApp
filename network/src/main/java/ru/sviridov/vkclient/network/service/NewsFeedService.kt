package ru.sviridov.vkclient.network.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.sviridov.vkclient.network.NetworkConstants
import ru.sviridov.vkclient.network.response.NewsResponse

interface NewsFeedService {

    @GET("newsfeed.get?filters=post&max_photos=1&count=${NetworkConstants.FEED_ITEMS_COUNT_PER_REQUEST}")
    fun getNews(@Query("start_from") startFrom: String): Single<NewsResponse>

    @GET("newsfeed.get?filters=post,photo&max_photos=1&count=${NetworkConstants.FEED_ITEMS_COUNT_PER_REQUEST}")
    fun getNews(): Single<NewsResponse>

}
