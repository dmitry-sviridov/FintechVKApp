package ru.sviridov.vkclient.feature.profile.domain.impl

import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature.profile.domain.ProfileWallConverter
import ru.sviridov.vkclient.network.model.EntityModel
import ru.sviridov.vkclient.network.response.GetWallResponse

internal class ProfileWallConverterImpl : ProfileWallConverter {

    override fun convertApiResponseToUi(dto: GetWallResponse): List<NewsItem> {

        val newsItems = dto.items
            .filter {
                it.text.isNotBlank() || it.attachments?.firstOrNull { attachment ->
                    attachment.type == "photo"
                } != null || it.copy_history?.get(0)?.attachments?.firstOrNull { attachment ->
                    attachment.type == "photo"
                } != null
            }
            .map { feed ->
                val isFromGroup: Boolean
                val index: Int

                val isRepost = !feed.copy_history.isNullOrEmpty()

                if (isRepost) {
                    if (checkListContainsId(
                            Math.abs(feed.copy_history!![0].owner_id),
                            dto.groups
                        )
                    ) {
                        isFromGroup = true
                        index =
                            dto.groups.indexOfFirst { group -> group.id == Math.abs(feed.copy_history!![0].owner_id) }
                    } else {
                        isFromGroup = false
                        index =
                            dto.profiles.indexOfFirst { profile -> profile.id == Math.abs(feed.copy_history!![0].owner_id) }
                    }
                } else {
                    if (checkListContainsId(Math.abs(feed.owner_id), dto.groups)) {
                        isFromGroup = true
                        index =
                            dto.groups.indexOfFirst { group -> group.id == Math.abs(feed.owner_id) }
                    } else {
                        isFromGroup = false
                        index =
                            dto.profiles.indexOfFirst { profile -> profile.id == Math.abs(feed.owner_id) }
                    }
                }

                return@map NewsItem(
                    isRepost = isRepost,
                    postId = feed.id,
                    sourceId = if (isFromGroup) {
                        dto.groups[index].id
                    } else {
                        dto.profiles[index].id
                    },
                    sourceTitle = if (isFromGroup) {
                        dto.groups[index].name
                    } else {
                        "${dto.profiles[index].firstName} ${dto.profiles[index].lastName}"
                    },
                    postedAt = if (isRepost) {
                        feed.copy_history!![0].date
                    } else {
                        feed.date
                    },
                    sourceAvatar = if (isFromGroup) {
                        dto.groups[index].photoWithSize100
                    } else {
                        dto.profiles[index].photoWithSize100
                    },
                    imageUrl = if (isRepost) {
                        feed.copy_history?.get(0)?.attachments?.firstOrNull { attachment ->
                            attachment.type == "photo"
                        }?.photo?.sizes?.maxByOrNull { by -> by.height }?.url
                    } else {
                        feed.attachments?.firstOrNull { attachment ->
                            attachment.type == "photo"
                        }?.photo?.sizes?.maxByOrNull { by -> by.height }?.url
                    },
                    textContent = if (isRepost) {
                        feed.copy_history?.get(0)?.text ?: ""
                    } else {
                        feed.text
                    },
                    likesCount = feed.likes?.count ?: 0,
                    shareCount = feed.reposts?.count ?: 0,
                    commentCount = feed.comments.count,
                    viewsCount = feed.views?.count ?: 0,
                    isLiked = feed.likes?.userLikes == 1
                )
            }.toList()
        return newsItems
    }

    private fun checkListContainsId(requestedId: Int, entityList: List<EntityModel>): Boolean {
        return entityList.firstOrNull { entity -> entity.id == requestedId } != null
    }
}