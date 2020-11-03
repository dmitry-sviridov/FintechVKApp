package ru.sviridov.newsfeed.data

import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.newsfeed.data.db.item.NewsItem

object FakeDataSource {
    var newsItems = mutableListOf<NewsItem>()
    val newsListSubject = BehaviorSubject.create<MutableList<NewsItem>>()

    var nextFrom: String? = null
}
