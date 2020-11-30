package ru.sviridov.vkclient.ui.domain

import io.reactivex.Observable

interface FavouriteNewsRepository {

    fun fetchLikedNewsNotEmpty(): Observable<Boolean>
}