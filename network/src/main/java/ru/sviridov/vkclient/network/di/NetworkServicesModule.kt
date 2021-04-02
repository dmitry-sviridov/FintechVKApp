package ru.sviridov.vkclient.network.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.sviridov.vkclient.network.service.NewsFeedService
import ru.sviridov.vkclient.network.service.PostLikesService
import ru.sviridov.vkclient.network.service.ProfileInfoService
import ru.sviridov.vkclient.network.service.WallService
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkServicesModule {

    @Provides
    @Singleton
    fun provideNewsFeedService(@Named("withUnwrap") retrofit: Retrofit): NewsFeedService {
        return retrofit.create(NewsFeedService::class.java)
    }

    @Provides
    @Singleton
    fun providePostLikesService(@Named("withUnwrap") retrofit: Retrofit): PostLikesService {
        return retrofit.create(PostLikesService::class.java)
    }

    @Provides
    @Singleton
    fun provideWallService(@Named("withUnwrap") retrofit: Retrofit): WallService {
        return retrofit.create(WallService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileInfoService(@Named("withOutUnwrap") retrofit: Retrofit): ProfileInfoService {
        return retrofit.create(ProfileInfoService::class.java)
    }
}