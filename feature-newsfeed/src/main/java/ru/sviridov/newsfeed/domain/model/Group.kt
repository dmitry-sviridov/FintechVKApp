package ru.sviridov.newsfeed.domain.model

data class Group(
    val description: String,
    val id: Int,
    val is_admin: Int,
    val admin_level: Int?,
    val is_closed: Int?,
    val is_member: Int,
    val name: String,
    val photo_100: String,
    val photo_200: String,
    val photo_50: String,
    val screen_name: String,
    val type: String
)