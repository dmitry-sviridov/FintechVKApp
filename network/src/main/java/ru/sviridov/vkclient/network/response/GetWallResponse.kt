package ru.sviridov.vkclient.network.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName
import ru.sviridov.vkclient.network.model.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "response")
data class GetWallResponse(
    val count: Int,
    val groups: List<Group>,
    val items: List<Item>,
    val profiles: List<Profile>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Item(
    val comments: Comments,
    val attachments: List<Attachment>?,
    val copy_history: List<CopyHistory>?,
    val date: Long,
    val from_id: Int,
    val id: Int,
    val is_archived: Boolean,
    val is_favorite: Boolean,
    val likes: Likes?,
    val owner_id: Int,
    val post_source: PostSource,
    val post_type: String,
    val reposts: Reposts?,
    val short_text_rate: Double,
    val text: String,
    val views: Views?
)