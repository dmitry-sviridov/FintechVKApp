package ru.sviridov.newsfeed.domain.implementation

import io.reactivex.Observable
import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.domain.FavouriteNewsRepository

internal class FavouriteNewsRepositoryFakeImpl : FavouriteNewsRepository {

    private val dataSource = FakeDataSource

    override fun fetchLikedNewsNotEmpty(): Observable<Boolean> {
        return dataSource
            .newsListSubject
            .map { list -> list.any { item -> item.isLiked == true } }
    }
}
