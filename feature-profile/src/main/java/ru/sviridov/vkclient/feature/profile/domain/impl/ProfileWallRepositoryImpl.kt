package ru.sviridov.vkclient.feature.profile.domain.impl

import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallConverter
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallRepository
import ru.sviridov.vkclient.network.response.CreatePostResponse
import ru.sviridov.vkclient.network.service.WallService
import javax.inject.Inject

internal class ProfileWallRepositoryImpl @Inject constructor(
    private val wallService: WallService,
    private val profileWallConverter: ProfileWallConverter
) : ProfileWallRepository {

    override fun getUsersWall(ownerId: Int): Single<List<NewsItem>> {
        Log.d(TAG, "getUsersWall")
        return wallService
            .getWallItems(ownerId)
            .subscribeOn(Schedulers.io())
            .map { response -> profileWallConverter.convertApiResponseToUi(response) }
    }

    override fun sendTextWallPost(ownerId: Int, message: String): Single<CreatePostResponse> {
        Log.d(TAG, "sendTextWallPost")
        return wallService
            .createWallPost(ownerId, message)
            .subscribeOn(Schedulers.io())
    }

    companion object {
        private const val TAG = "ProfileWallRepository"
    }
}