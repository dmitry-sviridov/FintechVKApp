package ru.sviridov.vkclient.network.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.sviridov.vkclient.network.response.LikesResponse

interface PostLikesService {

    @GET("likes.add?type=post")
    fun setItemAsLiked(
        @Query("owner_id") sourceId: Int,
        @Query("item_id") postId: Int
    ): Single<LikesResponse>

    @GET("likes.delete?type=post")
    fun setItemAsDisliked(
        @Query("owner_id") sourceId: Int,
        @Query("item_id") postId: Int
    ): Single<LikesResponse>

}
