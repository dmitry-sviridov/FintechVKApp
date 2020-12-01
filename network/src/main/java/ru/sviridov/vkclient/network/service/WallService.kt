package ru.sviridov.vkclient.network.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WallService {

    @GET("wall.getComments?need_likes=1")
    fun setItemAsLiked(
        @Query("owner_id") sourceId: Int,
        @Query("item_id") postId: Int
    ): Single<Unit>
}