package ru.sviridov.vkclient.feature.profile.domain

import io.reactivex.Single
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.network.response.CreatePostResponse

interface ProfileWallRepository {

    fun getUsersWall(ownerId: Int): Single<List<NewsItem>>

    fun sendTextWallPost(ownerId: Int, message: String): Single<CreatePostResponse>
}