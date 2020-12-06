package ru.sviridov.vkclient.feature.newsfeed.domain.implementation

import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature.newsfeed.data.db.entity.NewsItemEntity
import ru.sviridov.vkclient.feature.newsfeed.domain.NewsConverter
import ru.sviridov.vkclient.network.response.NewsResponse
import ru.sviridov.vkclient.network.model.EntityModel
import java.lang.Math.abs

internal class NewsConverterImpl : NewsConverter {
    override fun convertApiResponseToUi(dto: NewsResponse): List<NewsItem> {

        val newsItems = dto.items
            .filter { it.likes?.canLike == 1 && it.markedAsAds == 0 }
            .map { feed ->
                val isFromGroup: Boolean
                val index: Int

                if (checkListContainsId(abs(feed.sourceId), dto.groups)) {
                    isFromGroup = true
                    index = dto.groups.indexOfFirst { group -> group.id == Math.abs(feed.sourceId)}
                } else {
                    isFromGroup = false
                    index = dto.profiles.indexOfFirst { profile -> profile.id == abs(feed.sourceId) }
                }

                return@map NewsItem(
                    postId = feed.postId,
                    sourceId = feed.sourceId,
                    sourceTitle = if (isFromGroup) {
                        dto.groups[index].name
                    } else {
                        "${dto.profiles[index].firstName} ${dto.profiles[index].lastName}"
                    },
                    postedAt = feed.date,
                    sourceAvatar = if (isFromGroup) {
                        dto.groups[index].photoWithSize100
                    } else {
                        dto.profiles[index].photoWithSize100
                    },
                    imageUrl = feed.attachments?.firstOrNull { attachment ->
                        attachment.type == "photo"
                    }?.photo?.sizes?.maxByOrNull { by -> by.height }?.url,
                    textContent = feed.text,
                    likesCount = feed.likes?.count ?: 0,
                    shareCount = feed.reposts?.count ?: 0,
                    commentCount = feed.comments?.count ?: 0,
                    viewsCount = feed.views?.count ?: 0,
                    isLiked = feed.likes?.userLikes == 1
                )
            }.toList()
        return newsItems
    }

    override fun convertDbToUi(entity: NewsItemEntity) = NewsItem(
        postId = entity.postId,
        sourceId = entity.sourceId,
        sourceTitle = entity.sourceTitle,
        sourceAvatar = entity.sourceAvatar,
        postedAt = entity.postedAt,
        imageUrl = entity.imageUrl,
        textContent = entity.textContent,
        likesCount = entity.likesCount,
        shareCount = entity.shareCount,
        commentCount = entity.commentCount,
        viewsCount = entity.viewsCount,
        isLiked = entity.isLiked
    )

    override fun convertUiToDb(item: NewsItem) = NewsItemEntity(
        postId = item.postId,
        sourceId = item.sourceId,
        sourceTitle = item.sourceTitle,
        sourceAvatar = item.sourceAvatar,
        postedAt = item.postedAt,
        imageUrl = item.imageUrl,
        textContent = item.textContent,
        likesCount = item.likesCount,
        shareCount = item.shareCount,
        commentCount = item.commentCount,
        viewsCount = item.viewsCount,
        isLiked = item.isLiked
    )

    private fun checkListContainsId(requestedId: Int, entityList: List<EntityModel>): Boolean {
        return entityList.firstOrNull { entity -> entity.id == requestedId } != null
    }
}
