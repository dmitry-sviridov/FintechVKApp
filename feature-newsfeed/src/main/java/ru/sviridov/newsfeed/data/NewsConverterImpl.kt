package ru.sviridov.newsfeed.data

import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.network.dto.NewsResponse
import ru.sviridov.newsfeed.data.db.entity.NewsItemEntity
import ru.sviridov.newsfeed.domain.NewsConverter
import kotlin.math.abs

class NewsConverterImpl : NewsConverter {
    override fun convertApiResponseToUi(dto: NewsResponse): List<NewsItem> {
        val newsItems = dto.items.asSequence().map {
            NewsItem(
                postId = it.postId,
                sourceId = it.sourceId,
                sourceTitle = it.sourceId.toString(),
                postedAt = it.date,
                sourceAvatar = "",
                imageUrl = "",
                textContent = it.text,
                likesCount = it.likes?.count ?: 0,
                shareCount = it.reposts?.count ?: 0,
                commentCount = it.comments?.count ?: 0,
                viewsCount = it.views?.count ?: 0,
                isLiked = it.likes?.userLikes == 1
            )
        }.toMutableList()

        newsItems.forEach {

            if (dto.groups.firstOrNull { group -> group.id == abs(it.sourceTitle.toInt()) } != null) {
                dto.groups.first { group ->
                    group.id == abs(it.sourceTitle.toInt())
                }.also { group ->
                    it.sourceTitle = group.name
                    it.sourceAvatar = group.photoWithSize50
                }
            } else {
                dto.profiles.first { profile -> profile.id == abs(it.sourceTitle.toInt()) }
                    .also { profile ->
                        it.sourceTitle = "${profile.firstName} ${profile.lastName}"
                        it.sourceAvatar = profile.photoWithSize50
                    }
            }

            it.imageUrl = dto.items.firstOrNull { item ->
                item.text == it.textContent
            }?.attachments?.firstOrNull { attachment ->
                attachment.type == "photo"
            }?.photo?.sizes?.maxByOrNull { by -> by.height }?.url
        }
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
}
