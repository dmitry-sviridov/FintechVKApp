package ru.sviridov.vkclient.feature_newsfeed.domain

import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.feature_newsfeed.data.db.entity.NewsItemEntity
import ru.sviridov.vkclient.network.dto.NewsResponse
import ru.sviridov.vkclient.network.dto.PostCommentsResponse

interface CommentsConverter {
    fun convertApiResponseToUi(dto: PostCommentsResponse): Pair<List<PostCommentItem>, Boolean>
}

interface NewsConverter {
    fun convertApiResponseToUi(dto: NewsResponse): List<NewsItem>
    fun convertDbToUi(entity: NewsItemEntity): NewsItem
    fun convertUiToDb(item: NewsItem): NewsItemEntity
}
