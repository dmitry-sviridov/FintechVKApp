package ru.sviridov.vkclient.feature.profile.domain

import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.component.profile.model.ProfileModel
import ru.sviridov.vkclient.network.response.GetWallResponse
import ru.sviridov.vkclient.network.response.UserInfo

interface ProfileInfoConverter {
    fun convertApiResponseToUi(response: UserInfo): ProfileModel
}

interface ProfileWallConverter {
    fun convertApiResponseToUi(response: GetWallResponse): List<NewsItem>
}