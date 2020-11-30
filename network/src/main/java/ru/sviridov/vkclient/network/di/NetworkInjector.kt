package ru.sviridov.vkclient.network.di

import android.content.Context

object NetworkInjector {

    lateinit var context: Context
    private var component: NetworkComponent? = null

    @Synchronized
    fun getComponent(): NetworkComponent {
        return component ?: DaggerNetworkComponent.create().also {
            component = it
        }
    }
}