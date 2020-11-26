package ru.sviridov.newsfeed.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.sviridov.newsfeed.data.db.NewsDatabase
import ru.sviridov.newsfeed.data.db.dao.LikedNewsItemDao
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

//@Module
//class NewsConverterModule {
//
//    @Provides
//    @Singleton
//    fun provideNewsConverter(): NewsConverter {
//        return NewsConverterImpl()
//    }
//}

//@Module
//class NewsFeedRepositoryModule {
//
//    @Provides
//    @Singleton
//    fun newsFeedRepository(
////        apiService: NewsFeedService,
////        likesService: PostLikesService,
////        converter: NewsConverter,
////        likedDao: LikedNewsItemDao
//    ): NewsFeedRepository {
//        return NewsFeedRepositoryImpl()
//        //apiService, likesService, converter, likedDao
//    }
//}

/*
    private val apiService: NewsFeedService,
    private val likesService: PostLikesService,
    private val converter: NewsConverter,
    private val likedDao: LikedNewsItemDao
 */