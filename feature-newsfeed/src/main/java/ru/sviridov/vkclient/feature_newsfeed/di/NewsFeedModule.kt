package ru.sviridov.vkclient.feature_newsfeed.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.sviridov.vkclient.feature_newsfeed.data.db.NewsDatabase
import ru.sviridov.vkclient.feature_newsfeed.data.db.dao.LikedNewsItemDao
import ru.sviridov.vkclient.feature_newsfeed.domain.*
import ru.sviridov.vkclient.feature_newsfeed.domain.implementation.*
import ru.sviridov.vkclient.feature_newsfeed.domain.implementation.FavouriteNewsRepositoryImpl
import ru.sviridov.vkclient.feature_newsfeed.domain.implementation.NewsConverterImpl
import ru.sviridov.vkclient.feature_newsfeed.domain.implementation.NewsFeedRepositoryImpl
import ru.sviridov.vkclient.feature_newsfeed.domain.implementation.PostCommentRepositoryImpl
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
class ConvertersModule {

    @Provides
    @Singleton
    fun provideNewsConverter(): NewsConverter {
        return NewsConverterImpl()
    }

    @Provides
    @Singleton
    fun provideCommentsConverter(): CommentsConverter {
        return CommentsConverterImpl()
    }
}

@Module
abstract class NewsFeedRepositoryModule {

    @Binds
    @Singleton
    internal abstract fun newsFeedRepository(impl: NewsFeedRepositoryImpl): NewsFeedRepository

    @Binds
    @Singleton
    internal abstract fun favouritesNewsRepository(impl: FavouriteNewsRepositoryImpl): FavouriteNewsRepository
}

@Module
abstract class PostCommentRepositoryModule {

    @Binds
    @Singleton
    internal abstract fun postCommentRepository(impl: PostCommentRepositoryImpl): PostCommentsRepository
}
