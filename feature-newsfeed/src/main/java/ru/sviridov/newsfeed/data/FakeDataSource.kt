package ru.sviridov.newsfeed.data

import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.component.feeditem.model.NewsItem

object FakeDataSource {
    val newsListSubject = BehaviorSubject.create<List<NewsItem>>()
    var nextFrom: String? = null
}
