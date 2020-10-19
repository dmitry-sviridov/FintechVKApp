package ru.sviridov.newsfeed.data

import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

object FakeDataSource {
    var newsItems = mutableListOf<NewsItem>()
    val newsListSubject = BehaviorSubject.create<MutableList<NewsItem>>()
}
