package ru.sviridov.vkclient.network.model

data class CopyHistory(
    val attachments: List<Attachment>?,
    val date: Long,
    val from_id: Int,
    val id: Int,
    val owner_id: Int,
    val post_source: PostSource,
    val post_type: String,
    val text: String?
)