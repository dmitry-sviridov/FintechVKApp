package ru.sviridov.vkclient.feature.profile.domain.impl

import ru.sviridov.vkclient.component.profile.model.ProfileModel
import ru.sviridov.vkclient.feature.profile.domain.ProfileInfoConverter
import ru.sviridov.vkclient.network.response.UserInfo

internal class ProfileInfoConverterImpl : ProfileInfoConverter {

    override fun convertApiResponseToUi(
        userInfo: UserInfo
    ): ProfileModel {
        val cityCountry = StringBuilder()
        if (userInfo.country != null) {
            cityCountry.append(userInfo.country!!.title)
            if (userInfo.city != null) {
                cityCountry.append(", ", userInfo.city!!.title)
            }
        } else {
            if (userInfo.city != null) {
                cityCountry.append(userInfo.city!!.title)
            }
        }

        return ProfileModel(
            userId = userInfo.id,
            domain = userInfo.domain,
            userName = "${userInfo.firstName} ${userInfo.lastName}",
            photoUrl = userInfo.photoMaxOrig,
            birthdayDate = userInfo.birthdayDate,
            cityAndCountry = cityCountry.toString(),
            followersCount = userInfo.followersCount,
            lastSeen = userInfo.lastSeen.time
        )
    }
}