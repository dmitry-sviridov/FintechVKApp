package ru.sviridov.vkclient.feature.profile.domain

import io.reactivex.Single
import ru.sviridov.vkclient.component.profile.model.ProfileModel

interface ProfileScreenRepository {
    fun getProfileInfo(): Single<ProfileModel>
}