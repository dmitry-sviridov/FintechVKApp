package ru.sviridov.vkclient.feature.profile.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.sviridov.vkclient.feature.profile.domain.ProfileScreenRepository
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallRepository
import ru.sviridov.vkclient.network.service.ProfileInfoService
import ru.sviridov.vkclient.network.service.WallService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ConverterModule::class,
        RepositoryModule::class
    ]
)
interface ProfileComponent {

    fun getProfileInfoRepositoryImpl(): ProfileScreenRepository
    fun getProfileWallRepositoryImpl(): ProfileWallRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance profileInfoService: ProfileInfoService,
            @BindsInstance wallService: WallService,
        ): ProfileComponent
    }

}

