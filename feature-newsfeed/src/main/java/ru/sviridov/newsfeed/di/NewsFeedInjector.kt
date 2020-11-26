package ru.sviridov.newsfeed.di

import android.content.Context
import ru.sviridov.network.di.NetworkComponent

object NewsFeedInjector {
    lateinit var context: Context
    lateinit var networkComponent: NetworkComponent
    var newsFeedComponent: NewsFeedComponent? = null

    @Synchronized
    fun getComponent(): NewsFeedComponent {
        return newsFeedComponent ?: DaggerNewsFeedComponent
            .factory()
            .create(
                context = context,
                newsFeedService = networkComponent.getNewsFeedService(),
                likesService = networkComponent.getPostLikesService()
            )
            .also {
                newsFeedComponent = it
            }
    }

}