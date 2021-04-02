package ru.sviridov.vkclient.ui.di

import ru.sviridov.vkclient.feature.newsfeed.di.NewsFeedComponent
import ru.sviridov.vkclient.feature.profile.di.ProfileComponent

object UiComponentInjector {
    lateinit var newsFeedComponent: NewsFeedComponent
    lateinit var profileComponent: ProfileComponent
    lateinit var appId: String
    var uiComponent: UiComponent? = null

    @Synchronized
    fun getComponent(): UiComponent {
        return uiComponent ?: DaggerUiComponent
            .factory()
            .create(
                newsFeedComponent.getNewsFeedRepositoryImpl(),
                newsFeedComponent.getFavouritesNewsRepositoryImpl(),
                newsFeedComponent.getPostCommentRepositoryImpl(),
                profileComponent.getProfileInfoRepositoryImpl(),
                profileComponent.getProfileWallRepositoryImpl()
            )
            .also {
                uiComponent = it
            }
    }
}