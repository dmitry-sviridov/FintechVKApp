package ru.sviridov.vkclient.ui.di

import ru.sviridov.vkclient.feature_newsfeed.di.NewsFeedComponent

object UiComponentInjector {
    lateinit var newsFeedComponent: NewsFeedComponent
    lateinit var appId: String
    var uiComponent: UiComponent? = null

    @Synchronized
    fun getComponent(): UiComponent {
        return uiComponent ?: DaggerUiComponent
            .factory()
            .create(
                newsFeedComponent.getNewsFeedRepositoryImpl(),
                newsFeedComponent.getFavouritesNewsRepositoryImpl()
            )
            .also {
                uiComponent = it
            }
    }
}