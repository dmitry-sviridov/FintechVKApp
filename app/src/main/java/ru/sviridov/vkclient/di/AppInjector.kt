package ru.sviridov.vkclient.di

import android.content.Context
import ru.sviridov.network.di.NetworkInjector
import ru.sviridov.newsfeed.di.NewsFeedInjector

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
    }
}