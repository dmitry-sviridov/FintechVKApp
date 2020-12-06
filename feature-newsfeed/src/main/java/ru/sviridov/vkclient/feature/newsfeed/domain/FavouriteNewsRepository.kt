package ru.sviridov.vkclient.feature.newsfeed.domain

import io.reactivex.Observable

interface FavouriteNewsRepository {

    fun fetchLikedNewsNotEmpty(): Observable<Boolean>
}