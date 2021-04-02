package ru.sviridov.vkclient.feature.profile.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.sviridov.vkclient.feature.profile.domain.ProfileInfoConverter
import ru.sviridov.vkclient.feature.profile.domain.ProfileScreenRepository
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallConverter
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallRepository
import ru.sviridov.vkclient.feature.profile.domain.impl.ProfileInfoConverterImpl
import ru.sviridov.vkclient.feature.profile.domain.impl.ProfileScreenRepositoryImpl
import ru.sviridov.vkclient.feature.profile.domain.impl.ProfileWallConverterImpl
import ru.sviridov.vkclient.feature.profile.domain.impl.ProfileWallRepositoryImpl
import javax.inject.Singleton

@Module
class ConverterModule {

    @Provides
    @Singleton
    fun provideProfileConverter(): ProfileInfoConverter {
        return ProfileInfoConverterImpl()
    }

    @Provides
    @Singleton
    fun provideWallConverter(): ProfileWallConverter {
        return ProfileWallConverterImpl()
    }
}

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun profileScreenRepository(impl: ProfileScreenRepositoryImpl): ProfileScreenRepository

    @Binds
    @Singleton
    internal abstract fun profileWallRepository(impl: ProfileWallRepositoryImpl): ProfileWallRepository

}