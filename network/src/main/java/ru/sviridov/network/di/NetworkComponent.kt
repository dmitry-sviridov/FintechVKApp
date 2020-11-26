package ru.sviridov.network.di

import dagger.Component
import ru.sviridov.network.service.NewsFeedService
import ru.sviridov.network.service.PostLikesService
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
}