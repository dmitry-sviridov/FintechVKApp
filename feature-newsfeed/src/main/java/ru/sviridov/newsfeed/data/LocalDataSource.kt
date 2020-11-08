package ru.sviridov.newsfeed.data

import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.component.feeditem.model.NewsItem

object LocalDataSource {
    val newsListSubject = BehaviorSubject.create<List<NewsItem>>()
    var nextFrom: String? = null

    fun insertNewsItemsToSubjectBefore(newList: List<NewsItem>) {
        newsListSubject.value?.let { existingList ->
            val holder = existingList.toMutableList()
            holder.addAll(newList)
            newsListSubject.onNext(holder)
        } ?: newsListSubject.onNext(newList)
    }

    fun insertNewsItemsToSubjectAfter(newList: List<NewsItem>) {
        newsListSubject.value?.let { existingList ->
            newList.toMutableList().addAll(existingList)
        }
        newsListSubject.onNext(newList.distinct())
    }
}
