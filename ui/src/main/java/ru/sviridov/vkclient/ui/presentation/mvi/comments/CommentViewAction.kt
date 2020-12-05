package ru.sviridov.vkclient.ui.presentation.mvi.comments

import ru.sviridov.vkclient.feature_newsfeed.model.CommentModel

sealed class CommentViewAction {
    class FetchComments(val postId: Int, val ownerId: Int): CommentViewAction()
    class SetCommentAsLiked(val commentId: Int, val ownerId: Int): CommentViewAction()
    class SendComment(val comment: CommentModel): CommentViewAction()
}