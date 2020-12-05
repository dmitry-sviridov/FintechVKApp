package ru.sviridov.vkclient.feature_newsfeed.domain.implementation

import android.util.Log
import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.vkclient.feature_newsfeed.domain.CommentsConverter
import ru.sviridov.vkclient.network.dto.PostCommentsResponse

class CommentsConverterImpl : CommentsConverter {

    // TODO: убрать ограничение - сейчас
    //  используем только комментарии с текстом и только от пользователей
    override fun convertApiResponseToUi(dto: PostCommentsResponse): Pair<List<PostCommentItem>, Boolean> {

        val profilesList = dto.profiles.map { profile -> profile.id }
        val postCommentsList = dto.items.asSequence()
            .filter { !it.text.isNullOrBlank() && profilesList.contains(it.fromId) && it.likes != null }
            .also { Log.e(TAG, "convertApiResponseToUi count after filter: ${it.count()}", ) }
            .map { comment ->
                val commentOwner = dto.profiles.first { it.id == comment.fromId }
                Log.e(TAG, "convertApiResponseToUi: ${commentOwner.firstName} ${commentOwner.lastName} send comment")
                return@map PostCommentItem(
                    sourceOwnerId = comment.ownerId,
                    ownerId = comment.fromId,
                    ownerName = "${commentOwner.firstName} ${commentOwner.lastName}",
                    ownerProfileImageUrl = commentOwner.photo100,
                    commentId = comment.id,
                    textContent = comment.text!!,
                    likesCount = comment.likes!!.count,
                    isLiked = comment.likes!!.userLikes == 1
                )
            }.toList()

        Log.e(TAG, "convertApiResponseToUi: converted new list with size ${postCommentsList.size}", )
        return Pair(postCommentsList, dto.canPost)
    }

    companion object {
        private const val TAG = "CommentsConverterImpl"
    }
}