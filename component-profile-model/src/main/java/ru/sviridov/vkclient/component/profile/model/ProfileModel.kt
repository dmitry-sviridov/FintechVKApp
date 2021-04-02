package ru.sviridov.vkclient.component.profile.model

class ProfileModel(
    val userId: Int,
    val domain: String,
    val userName: String,
    val photoUrl: String,
    val birthdayDate: String?,
    val cityAndCountry: String?,
    val followersCount: Int,
    val lastSeen: Long
)