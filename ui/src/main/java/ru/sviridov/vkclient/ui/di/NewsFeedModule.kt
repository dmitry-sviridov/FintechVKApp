package ru.sviridov.vkclient.ui.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.sviridov.vkclient.ui.data.NewsConverterImpl
import ru.sviridov.vkclient.ui.data.db.NewsDatabase
import ru.sviridov.vkclient.ui.data.db.dao.LikedNewsItemDao
import ru.sviridov.vkclient.ui.domain.NewsConverter
import ru.sviridov.vkclient.ui.domain.NewsFeedRepository
import ru.sviridov.vkclient.ui.domain.implementation.NewsFeedRepositoryImpl
import javax.inject.Singleton

@Module
class NewsFeedDbModule {

    @Provides
    @Singleton
    fun provideDb(context: Context): NewsDatabase {
        return NewsDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideLikedDao(db: NewsDatabase): LikedNewsItemDao {
        return db.likedDao()
    }
}

@Module
class NewsConverterModule {

    @Provides
    @Singleton
    fun provideNewsConverter(): NewsConverter {
        return NewsConverterImpl()
    }
}

@Module
abstract class NewsFeedRepositoryModule {

    @Binds
    @Singleton
    internal abstract fun newsFeedRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository
}
