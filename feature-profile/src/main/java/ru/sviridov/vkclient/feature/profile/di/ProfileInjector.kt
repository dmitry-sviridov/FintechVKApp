package ru.sviridov.vkclient.feature.profile.di

import android.content.Context
import ru.sviridov.vkclient.network.di.NetworkComponent

object ProfileInjector {
    lateinit var context: Context
    lateinit var networkComponent: NetworkComponent
    var profileComponent: ProfileComponent? = null

    @Synchronized
    fun getComponent(): ProfileComponent {
        return profileComponent ?: DaggerProfileComponent
            .factory()
            .create(
                context,
                networkComponent.getProfileInfoService(),
                networkComponent.getWallService()
            )
            .also {
                profileComponent = it
            }
    }

}