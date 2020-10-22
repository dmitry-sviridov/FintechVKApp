package ru.sviridov.newsfeed.domain

import io.reactivex.Observable

interface FavouriteNewsRepository {

    fun fetchLikedNewsNotEmpty(): Observable<Boolean>
}