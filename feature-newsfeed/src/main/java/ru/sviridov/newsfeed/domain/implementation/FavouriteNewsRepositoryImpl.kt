package ru.sviridov.newsfeed.domain.implementation

import android.app.Application
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.sviridov.newsfeed.data.db.NewsDatabase
import ru.sviridov.newsfeed.domain.FavouriteNewsRepository

class FavouriteNewsRepositoryImpl(application: Application) : FavouriteNewsRepository {
    private val likedDao = NewsDatabase.getDatabase(application).likedDao()

    override fun fetchLikedNewsNotEmpty(): Observable<Boolean> {
        return likedDao
            .getAllLiked()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .map { list -> list.isNotEmpty() }
    }
}