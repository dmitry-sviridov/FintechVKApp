package ru.sviridov.newsfeed.domain.implementation

import io.reactivex.Observable
import ru.sviridov.newsfeed.data.LocalDataSource
import ru.sviridov.newsfeed.domain.FavouriteNewsRepository

class FavouriteNewsRepositoryImpl : FavouriteNewsRepository {
    private val dataSource = LocalDataSource

    override fun fetchLikedNewsNotEmpty(): Observable<Boolean> {
        return dataSource
            .newsListSubject
            .map { list -> list.any { item -> item.isLiked == true } }
    }
}