package ru.sviridov.vkclient.network.di

import dagger.Component
import ru.sviridov.vkclient.network.service.NewsFeedService
import ru.sviridov.vkclient.network.service.PostLikesService
import ru.sviridov.vkclient.network.service.WallService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkCommonModule::class,
        NetworkServicesModule::class
    ]
)
interface NetworkComponent {
    fun getNewsFeedService(): NewsFeedService
    fun getPostLikesService(): PostLikesService
    fun getWallService(): WallService
}