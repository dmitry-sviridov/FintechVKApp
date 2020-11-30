package ru.sviridov.vkclient.feature_newsfeed.domain

import io.reactivex.Observable

interface FavouriteNewsRepository {

    fun fetchLikedNewsNotEmpty(): Observable<Boolean>
}