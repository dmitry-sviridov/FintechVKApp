package ru.sviridov.vkclient.network.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {

    val context: Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun appContext(appContext: Context): Builder

        fun build(): AppComponent
    }
}