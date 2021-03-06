package ru.sviridov.vkclient.ui.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.component.feeditem.model.NewsItem

class FeedDiffUtilsCallback() :
    DiffUtil.ItemCallback<NewsItem>() {

    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.postId == newItem.postId
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.postId == newItem.postId &&
                oldItem.isLiked == newItem.isLiked &&
                oldItem.likesCount == newItem.likesCount
    }
}

class CommentDiffUtilsCallback() :
    DiffUtil.ItemCallback<PostCommentItem>() {

    override fun areItemsTheSame(oldItem: PostCommentItem, newItem: PostCommentItem): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: PostCommentItem, newItem: PostCommentItem): Boolean {
        return oldItem.commentId == newItem.commentId &&
                oldItem.isLiked == newItem.isLiked &&
                oldItem.likesCount == newItem.likesCount
    }
}