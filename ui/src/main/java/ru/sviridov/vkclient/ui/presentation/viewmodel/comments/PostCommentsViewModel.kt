package ru.sviridov.vkclient.ui.presentation.viewmodel.comments

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import ru.sviridov.vkclient.feature.newsfeed.domain.PostCommentsRepository
import ru.sviridov.vkclient.feature.newsfeed.model.CommentModel
import ru.sviridov.vkclient.ui.presentation.mvi.comments.CommentViewAction
import ru.sviridov.vkclient.ui.presentation.mvi.comments.CommentViewState
import javax.inject.Inject

class PostCommentsViewModel @Inject constructor(
    private val postCommentsRepository: PostCommentsRepository
) : ViewModel() {

    val viewState: MutableLiveData<CommentViewState> = MutableLiveData()

    fun handleAction(action: CommentViewAction) {
        when (action) {
            is CommentViewAction.FetchComments -> fetchCommentsForFeedItem(
                action.postId,
                action.ownerId
            )
            is CommentViewAction.SendComment -> sendNewTextComment(action.comment)
        }
    }

    @SuppressLint("CheckResult")
    private fun sendNewTextComment(comment: CommentModel) {
        postCommentsRepository
            .sendComment(comment.ownerId, comment.postId, comment.text)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.e(TAG, "sendNewTextComment got an error: ${it.message}")
                    viewState.value = CommentViewState.ShowError
                },
                onSuccess = {
                    Log.e(TAG,
                        "sendNewTextComment: well done, new comment with id = ${it.commentId}",
                    )
                    fetchCommentsForFeedItem(comment.postId, comment.ownerId, true)
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun fetchCommentsForFeedItem(postId: Int, ownerId: Int, isUpdate: Boolean = false) {
        postCommentsRepository
            .fetchComments(ownerId, postId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.e(TAG, "fetchCommentsForFeedItem: ${it.message}")
                    viewState.value = CommentViewState.ShowError
                },
                onSuccess = {
                    if (it.first.isNotEmpty()) {
                        Log.e(TAG, "fetchCommentsForFeedItem: got ${it.first.size} comments")
                        viewState.value = if (isUpdate) {
                            CommentViewState.UpdateComments(it.first, it.second)
                        } else {
                            CommentViewState.ShowComments(it.first, it.second)
                        }

                    } else {
                        Log.e(TAG, "fetchCommentsForFeedItem: got 0 comments")
                        viewState.value = CommentViewState.ShowNoContent(it.second)
                    }
                }
            )
    }

    companion object {
        private const val TAG = "PostCommentsViewModel"
    }
}