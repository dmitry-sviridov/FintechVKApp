package ru.sviridov.vkclient.feature_newsfeed.di

import android.content.Context
import ru.sviridov.vkclient.network.di.NetworkComponent

object NewsFeedInjector {
    lateinit var context: Context
    lateinit var networkComponent: NetworkComponent
    var newsFeedComponent: NewsFeedComponent? = null

    @Synchronized
    fun getComponent(): NewsFeedComponent {
        return newsFeedComponent ?: DaggerNewsFeedComponent
            .factory()
            .create(
                context,
                networkComponent.getNewsFeedService(),
                networkComponent.getPostLikesService()
            )
            .also {
                newsFeedComponent = it
            }
    }

}