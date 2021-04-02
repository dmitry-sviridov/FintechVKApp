package ru.sviridov.vkclient.feature.newsfeed.domain

import io.reactivex.Single
import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.vkclient.network.response.CommentResponse

interface PostCommentsRepository {

    fun fetchComments(sourceId: Int, postId: Int): Single<Pair<List<PostCommentItem>, Boolean>>
    fun markCommentAsLiked()

    fun sendComment(sourceId: Int, postId: Int, text: String): Single<CommentResponse>
}