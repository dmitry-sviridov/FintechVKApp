package ru.sviridov.vkclient.ui.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import ru.sviridov.vkclient.ui.domain.implementation.FavouriteNewsRepositoryImpl
import javax.inject.Inject

class NewsFeedGroupViewModel @Inject constructor(
    private val favouriteNewsRepository: FavouriteNewsRepositoryImpl
) : ViewModel() {

    val favouriteTabEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var likedCountDisposable: Disposable

    init {
        listenLikedNewsListNotEmpty()
    }

    private fun listenLikedNewsListNotEmpty() {
        likedCountDisposable = favouriteNewsRepository
            .fetchLikedNewsNotEmpty()
            .distinctUntilChanged()
            .subscribeBy { likedListNotEmpty ->
                favouriteTabEnabled.value = likedListNotEmpty
            }
    }

    override fun onCleared() {
        super.onCleared()
        likedCountDisposable.dispose()
    }
}