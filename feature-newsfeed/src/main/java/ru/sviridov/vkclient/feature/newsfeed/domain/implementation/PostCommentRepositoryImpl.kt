package ru.sviridov.vkclient.feature.newsfeed.domain.implementation

import android.util.Log
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.vkclient.feature.newsfeed.domain.CommentsConverter
import ru.sviridov.vkclient.feature.newsfeed.domain.PostCommentsRepository
import ru.sviridov.vkclient.network.response.CommentResponse
import ru.sviridov.vkclient.network.service.WallService
import javax.inject.Inject

internal class PostCommentRepositoryImpl @Inject constructor(
    private val wallService: WallService,
    val converter: CommentsConverter
) : PostCommentsRepository{

    override fun fetchComments(sourceId: Int, postId: Int): Single<Pair<List<PostCommentItem>, Boolean>> {
        Log.d(TAG, "fetchComments")
        return wallService
            .getComments(sourceId, postId)
            .subscribeOn(Schedulers.io())
            .map { response -> converter.convertApiResponseToUi(response) }
    }

    override fun markCommentAsLiked() {
        TODO("Not yet implemented")
    }

    override fun sendComment(sourceId: Int, postId: Int, text: String): Single<CommentResponse> {
        Log.d(TAG, "sendComment: $text")
        return wallService
            .sendComment(sourceId, postId, text)
            .subscribeOn(Schedulers.io())
    }

    companion object {
        private const val TAG = "PostCommentRepositoryIm"
    }
}