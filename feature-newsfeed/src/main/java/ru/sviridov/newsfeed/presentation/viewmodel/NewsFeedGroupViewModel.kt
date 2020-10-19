package ru.sviridov.newsfeed.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import ru.sviridov.newsfeed.domain.implementation.FavouriteNewsRepositoryFakeImpl

class NewsFeedGroupViewModel : ViewModel() {

    private val feedRepository = FavouriteNewsRepositoryFakeImpl()
    val favouriteTabEnabled: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var likedCountDisposable: Disposable

    init {
        listenLikedNewsListNotEmpty()
    }

    private fun listenLikedNewsListNotEmpty() {
        likedCountDisposable = feedRepository.fetchLikedNewsNotEmpty()
            .subscribeBy { likedListNotEmpty ->
                favouriteTabEnabled.value = likedListNotEmpty
            }
    }

    override fun onCleared() {
        super.onCleared()
        likedCountDisposable.dispose()
    }
}
