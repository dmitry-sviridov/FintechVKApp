package ru.sviridov.newsfeed.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.sviridov.newsfeed.domain.implementation.FavouriteNewsRepositoryFakeImpl

class NewsFeedGroupViewModel : ViewModel() {

    private val feedRepository = FavouriteNewsRepositoryFakeImpl()

    val favouriteTabEnabled: LiveData<Boolean> =
        Transformations.map(feedRepository.likedItems) {
            it.isNotEmpty()
        }
}