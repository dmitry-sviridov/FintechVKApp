package ru.sviridov.vkclient.network.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.sviridov.vkclient.network.response.GetUserResponse
import ru.sviridov.vkclient.network.response.ProfileInfoResponseContainer

interface ProfileInfoService {

    @GET("account.getProfileInfo")
    fun getBaseProfileInfo(): Single<ProfileInfoResponseContainer>

    @GET("users.get?fields=domain,bdate,city,country,photo_max_orig, followers_count,last_seen")
    fun getUserInfo(
        @Query("user_ids") userId: Int,
    ): Single<GetUserResponse>

}