package ru.sviridov.vkclient.network.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.sviridov.vkclient.network.response.*

interface WallService {

    @GET("wall.getComments?need_likes=1&count=100&extended=1")
    fun getComments(
        @Query("owner_id") sourceId: Int,
        @Query("post_id") postId: Int
    ): Single<PostCommentsResponse>

    @GET("wall.get?filter=all&count=100&extended=1")
    fun getWallItems(
        @Query("owner_id") ownerId: Int
    ): Single<GetWallResponse>

    @GET("wall.post")
    fun createWallPost(
        @Query("owner_id") ownerId: Int,
        @Query("message") message: String
    ): Single<CreatePostResponse>

    @GET("wall.createComment?")
    fun sendComment(
        @Query("owner_id") sourceId: Int,
        @Query("post_id") postId: Int,
        @Query("message") text: String
    ): Single<CommentResponse>

    @GET("likes.add?type=comment")
    fun setItemAsLiked(
        @Query("owner_id") sourceId: Int,
        @Query("item_id") postId: Int
    ): Single<LikesResponse>

    @GET("likes.delete?type=comment")
    fun setItemAsDisliked(
        @Query("owner_id") sourceId: Int,
        @Query("item_id") postId: Int
    ): Single<LikesResponse>

}