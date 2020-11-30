package ru.sviridov.vkclient.network.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.sviridov.vkclient.network.service.NewsFeedService
import ru.sviridov.vkclient.network.service.PostLikesService
import javax.inject.Singleton

@Module
class NetworkServicesModule {

    @Provides
    @Singleton
    fun provideNewsFeedService(retrofit: Retrofit): NewsFeedService {
        return retrofit.create(NewsFeedService::class.java)
    }

    @Provides
    @Singleton
    fun providePostLikesService(retrofit: Retrofit): PostLikesService {
        return retrofit.create(PostLikesService::class.java)
    }
}