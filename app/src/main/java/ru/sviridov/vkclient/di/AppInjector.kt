package ru.sviridov.vkclient.di

import android.content.Context
import ru.sviridov.vkclient.BuildConfig
import ru.sviridov.vkclient.feature.newsfeed.di.NewsFeedInjector
import ru.sviridov.vkclient.feature.profile.di.ProfileInjector
import ru.sviridov.vkclient.network.di.NetworkInjector
import ru.sviridov.vkclient.ui.di.UiComponentInjector

object AppInjector {

    private lateinit var appComponent: AppComponent

    fun createAppComponent(context: Context) {
        appComponent = DaggerAppComponent.builder()
            .appContext(context)
            .build()

        initModulesManagers()
    }

    fun getAppComponent() = appComponent

    private fun initModulesManagers() {
        NetworkInjector.apply {
            context = appComponent.context
        }

        NewsFeedInjector.apply {
            context = appComponent.context
            networkComponent = NetworkInjector.getComponent()
        }

        ProfileInjector.apply {
            context = appComponent.context
            networkComponent = NetworkInjector.getComponent()
        }

        UiComponentInjector.apply {
            newsFeedComponent = NewsFeedInjector.getComponent()
            profileComponent = ProfileInjector.getComponent()
            appId = BuildConfig.APPLICATION_ID
        }
    }
}