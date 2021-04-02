package ru.sviridov.vkclient.ui.presentation.viewmodel.wall

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallRepository
import ru.sviridov.vkclient.ui.presentation.mvi.profile.ProfileViewActions
import ru.sviridov.vkclient.ui.presentation.mvi.wall.WallViewAction
import ru.sviridov.vkclient.ui.presentation.mvi.wall.WallViewState
import javax.inject.Inject

class WallViewModel @Inject constructor(
    private val profileWallRepository: ProfileWallRepository
) : ViewModel() {

    val viewState: MutableLiveData<WallViewState> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun handleAction(action: WallViewAction) {
        when (action) {
            is WallViewAction.FetchWallPosts -> fetchWallPosts(action.userId)
            is WallViewAction.CreateNewTextWallPost -> sendNewTextPost(action.userId, action.message)
        }
    }

    private fun sendNewTextPost(userId: Int, message: String) {
        val sendPostDisposable = profileWallRepository
            .sendTextWallPost(userId, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.e(TAG, "sendNewTextPost: ${it.message}",)
                },
                onSuccess = {
                    Log.d(TAG, "sendNewTextPost: post id = ${it.postId}")
                    fetchWallPosts(userId)
                }
            )

        compositeDisposable.add(sendPostDisposable)
    }

    private fun fetchWallPosts(userId: Int) {
        val fetchedWallDisposable = profileWallRepository
            .getUsersWall(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    Log.d(TAG, "List with items fetched! size=${it.size}")
                    if (it.isNotEmpty()) {
                        viewState.value = WallViewState.ShowWallItems(it)
                    } else {
                        viewState.value = WallViewState.ShowEmptyState
                    }
                },
                onError = {
                    Log.d(TAG, "fetch failure! ${it.message}")
                    viewState.value = WallViewState.ShowError
                }
            )

        compositeDisposable.add(fetchedWallDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val TAG = "WallViewModel"
    }
}