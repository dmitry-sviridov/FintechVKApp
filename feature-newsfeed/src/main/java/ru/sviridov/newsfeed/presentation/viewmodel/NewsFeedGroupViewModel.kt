package ru.sviridov.newsfeed.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import ru.sviridov.newsfeed.domain.implementation.FavouriteNewsRepositoryImpl

class NewsFeedGroupViewModel(application: Application) : AndroidViewModel(application) {

    private val feedRepository = FavouriteNewsRepositoryImpl(application)
    val favouriteTabEnabled: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var likedCountDisposable: Disposable

    init {
        listenLikedNewsListNotEmpty()
    }

    private fun listenLikedNewsListNotEmpty() {
        likedCountDisposable = feedRepository
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

class NewsFeedGroupViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NewsFeedGroupViewModel(application) as T
}
