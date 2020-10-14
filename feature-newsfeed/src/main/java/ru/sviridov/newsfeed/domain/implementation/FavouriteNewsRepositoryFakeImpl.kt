package ru.sviridov.newsfeed.domain.implementation

import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.domain.FavouriteNewsRepository

internal class FavouriteNewsRepositoryFakeImpl : FavouriteNewsRepository {

    private val dataSource = FakeDataSource

    var likedItems = dataSource.likedItems
        private set
}
