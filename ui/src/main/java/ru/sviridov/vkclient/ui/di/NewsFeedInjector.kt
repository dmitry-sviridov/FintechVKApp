package ru.sviridov.vkclient.ui.di

import android.content.Context
import ru.sviridov.vkclient.network.di.NetworkComponent

object NewsFeedInjector {
    lateinit var context: Context
    lateinit var networkComponent: NetworkComponent
    lateinit var appId: String
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