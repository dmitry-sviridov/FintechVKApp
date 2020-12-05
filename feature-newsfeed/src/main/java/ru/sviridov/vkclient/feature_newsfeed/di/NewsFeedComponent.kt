package ru.sviridov.vkclient.feature_newsfeed.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.sviridov.vkclient.feature_newsfeed.domain.FavouriteNewsRepository
import ru.sviridov.vkclient.feature_newsfeed.domain.NewsFeedRepository
import ru.sviridov.vkclient.feature_newsfeed.domain.PostCommentsRepository
import ru.sviridov.vkclient.feature_newsfeed.domain.implementation.PostCommentRepositoryImpl
import ru.sviridov.vkclient.network.service.NewsFeedService
import ru.sviridov.vkclient.network.service.PostLikesService
import ru.sviridov.vkclient.network.service.WallService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NewsFeedDbModule::class,
        ConvertersModule::class,
        NewsFeedRepositoryModule::class,
        PostCommentRepositoryModule::class
    ]
)
interface NewsFeedComponent {

    fun getNewsFeedRepositoryImpl(): NewsFeedRepository

    fun getFavouritesNewsRepositoryImpl(): FavouriteNewsRepository

    fun getPostCommentRepositoryImpl(): PostCommentsRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance newsFeedService: NewsFeedService,
            @BindsInstance likesService: PostLikesService,
            @BindsInstance wallService: WallService
        ): NewsFeedComponent
    }
}