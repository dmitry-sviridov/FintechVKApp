package ru.sviridov.newsfeed.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.sviridov.network.service.NewsFeedService
import ru.sviridov.network.service.PostLikesService
import ru.sviridov.newsfeed.presentation.FeedFragment
import ru.sviridov.newsfeed.presentation.NewsFeedGroupFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NewsFeedDbModule::class,
        NewsConverterModule::class,
        NewsFeedRepositoryModule::class
    ]
)
interface NewsFeedComponent {

    fun inject(target: FeedFragment)
    fun inject(target: NewsFeedGroupFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance newsFeedService: NewsFeedService,
            @BindsInstance likesService: PostLikesService
        ): NewsFeedComponent
    }
}