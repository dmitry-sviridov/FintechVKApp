package ru.sviridov.newsfeed.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.sviridov.newsfeed.data.NewsConverterImpl
import ru.sviridov.newsfeed.data.db.NewsDatabase
import ru.sviridov.newsfeed.data.db.dao.LikedNewsItemDao
import ru.sviridov.newsfeed.domain.NewsConverter
import ru.sviridov.newsfeed.domain.NewsFeedRepository
import ru.sviridov.newsfeed.domain.implementation.NewsFeedRepositoryImpl
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

/*
    private val apiService: NewsFeedService,
    private val likesService: PostLikesService,
    private val converter: NewsConverter,
    private val likedDao: LikedNewsItemDao
 */