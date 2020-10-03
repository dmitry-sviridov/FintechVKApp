package ru.sviridov.newsfeed.domain.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Group(
    var description: String?,
    val id: Int,
    val is_admin: Int,
    var admin_level: Int? = 0,
    val is_closed: Int,
    val is_member: Int,
    val name: String,
    val photo_100: String,
    val photo_200: String,
    val photo_50: String,
    val screen_name: String,
    val type: String
)