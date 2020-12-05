package ru.sviridov.vkclient.ui.presentation.mvi.comments

import ru.sviridov.component.comment.model.PostCommentItem

sealed class CommentViewState {
    class ShowComments(val comments: List<PostCommentItem>, val isAbleToSendComment: Boolean) :
        CommentViewState()
    class UpdateComments(val comments: List<PostCommentItem>, val isAbleToSendComment: Boolean) :
        CommentViewState()
    class ShowNoContent(val isAbleToSendComment: Boolean) : CommentViewState()
    object ShowError : CommentViewState()
}

