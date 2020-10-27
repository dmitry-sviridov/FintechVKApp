package ru.sviridov.network

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.sviridov.network.service.NewsFeedService
import ru.sviridov.network.service.PostLikesService


object ApiServicesProvider {

    private val newsFeedService: NewsFeedService by lazy { retrofit.create(NewsFeedService::class.java) }
    private val postLikesService: PostLikesService by lazy { retrofit.create(PostLikesService::class.java) }

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(QueryParameterInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    var objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(NetworkConstants.API_URL)
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun getNewsFeedApiService(): NewsFeedService {
        return newsFeedService
    }

    fun getPostLikesApiService(): PostLikesService {
        return postLikesService
    }
}