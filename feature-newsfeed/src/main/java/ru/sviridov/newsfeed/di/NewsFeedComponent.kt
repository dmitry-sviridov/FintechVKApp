package ru.sviridov.newsfeed.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.sviridov.network.service.NewsFeedService
import ru.sviridov.network.service.PostLikesService
import ru.sviridov.newsfeed.presentation.FeedFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NewsFeedDbModule::class,
//        NewsConverterModule::class,
//        NewsFeedRepositoryModule::class
    ]
)
interface NewsFeedComponent {

    fun inject(target: FeedFragment)

    fun getLikedService(): PostLikesService

    fun getApiService(): NewsFeedService

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance newsFeedService: NewsFeedService,
            @BindsInstance likesService: PostLikesService
        ): NewsFeedComponent
    }
}