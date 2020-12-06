package ru.sviridov.vkclient.feature.profile.domain.impl

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.sviridov.vkclient.component.profile.model.ProfileModel
import ru.sviridov.vkclient.feature.profile.domain.ProfileInfoConverter
import ru.sviridov.vkclient.feature.profile.domain.ProfileScreenRepository
import ru.sviridov.vkclient.network.service.ProfileInfoService
import javax.inject.Inject

internal class ProfileScreenRepositoryImpl @Inject constructor(
    private val profileInfoService: ProfileInfoService,
    private val profileInfoConverter: ProfileInfoConverter
) : ProfileScreenRepository {

    override fun getProfileInfo(): Single<ProfileModel> {
        return profileInfoService
            .getBaseProfileInfo()
            .subscribeOn(Schedulers.io())
            .flatMap {
                profileInfoService.getUserInfo(it.response.id)
            }
            .map { r -> profileInfoConverter.convertApiResponseToUi(r.response[0]) }
    }
}