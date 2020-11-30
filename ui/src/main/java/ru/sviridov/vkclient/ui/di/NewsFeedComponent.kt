package ru.sviridov.vkclient.ui.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.sviridov.vkclient.network.service.NewsFeedService
import ru.sviridov.vkclient.network.service.PostLikesService
import ru.sviridov.vkclient.ui.presentation.fragments.FeedFragment
import ru.sviridov.vkclient.ui.presentation.fragments.NewsFeedGroupFragment
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