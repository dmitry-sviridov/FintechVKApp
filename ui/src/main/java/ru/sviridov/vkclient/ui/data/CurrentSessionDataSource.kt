package ru.sviridov.vkclient.ui.data

import io.reactivex.subjects.BehaviorSubject
import ru.sviridov.component.feeditem.model.NewsItem

object CurrentSessionDataSource {
    val newsListSubject = BehaviorSubject.create<List<NewsItem>>()
    var nextFrom: String? = null

    fun insertNewsItemsToSubjectBefore(newList: List<NewsItem>) {
        this.newsListSubject.value?.let { existingList ->
            val holder = existingList.toMutableList()
            holder.addAll(newList)
            newsListSubject.onNext(holder)
        } ?: this.newsListSubject.onNext(newList)
    }

    fun insertNewsItemsToSubjectAfter(newList: List<NewsItem>) {
        this.newsListSubject.value?.let { existingList ->
            newList.toMutableList().addAll(existingList)
        }
        this.newsListSubject.onNext(newList.distinct())
    }

    fun updateNewsItemWithDislike(dislikedItem: NewsItem) {
        this.newsListSubject.value?.let { list ->
            val newList = list.toMutableList()
            newList
                .find { item -> item.postId == dislikedItem.postId }
                ?.apply {
                    this.isLiked = false
                    this.likesCount--
                }
            this.newsListSubject.onNext(newList)
        }
    }

    fun removeItem(item: NewsItem) {
        this.newsListSubject.value?.let { list ->
            val newList = list.toMutableList()
            newList.remove(newList.find { newsItem -> newsItem.postId == item.postId })
            this.newsListSubject.onNext(newList)
        }
    }
}
