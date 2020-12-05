package ru.sviridov.component.comment.model

data class PostCommentItem(
    val sourceOwnerId: Int,
    val ownerId: Int,
    val ownerName: String,
    val ownerProfileImageUrl: String?,
    val commentId: Int,
    val textContent: String,
    val likesCount: Int,
    var isLiked: Boolean = false
)