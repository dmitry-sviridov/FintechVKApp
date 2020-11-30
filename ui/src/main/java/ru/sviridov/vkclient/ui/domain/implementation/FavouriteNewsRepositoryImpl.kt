package ru.sviridov.vkclient.ui.domain.implementation

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.sviridov.vkclient.ui.data.db.dao.LikedNewsItemDao
import ru.sviridov.vkclient.ui.domain.FavouriteNewsRepository
import javax.inject.Inject

class FavouriteNewsRepositoryImpl @Inject constructor(
    private val likedDao: LikedNewsItemDao
) : FavouriteNewsRepository {

    override fun fetchLikedNewsNotEmpty(): Observable<Boolean> {
        return likedDao
            .getAllLiked()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .map { list -> list.isNotEmpty() }
    }
}