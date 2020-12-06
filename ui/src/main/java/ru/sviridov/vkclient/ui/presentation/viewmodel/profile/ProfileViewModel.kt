package ru.sviridov.vkclient.ui.presentation.viewmodel.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.sviridov.vkclient.feature.profile.domain.ProfileScreenRepository
import ru.sviridov.vkclient.ui.presentation.mvi.newsfeed.FeedViewState
import ru.sviridov.vkclient.ui.presentation.mvi.profile.ProfileViewActions
import ru.sviridov.vkclient.ui.presentation.mvi.profile.ProfileViewState
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileScreenRepository: ProfileScreenRepository
) : ViewModel() {

    val viewState: MutableLiveData<ProfileViewState> = MutableLiveData()

    fun handleAction(action: ProfileViewActions) {
        when (action) {
            is ProfileViewActions.FetchProfileInfo -> fetchProfileInfo()
        }
    }

    @SuppressLint("CheckResult")
    private fun fetchProfileInfo() {
        Log.e(TAG, "fetchProfileInfo", )
        profileScreenRepository
            .getProfileInfo()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.e(TAG, "fetchProfileInfo ${it.message}",)
                    viewState.value = ProfileViewState.RenderError
                },
                onSuccess = {
                    Log.e(TAG, "fetchProfileInfo about user ${it.userName}",)
                    viewState.value = ProfileViewState.RenderProfileInfo(it)
                }
            )
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}