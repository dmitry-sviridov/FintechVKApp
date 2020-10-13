package ru.sviridov.newsfeed.data

import androidx.lifecycle.MutableLiveData
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import java.util.*
import kotlin.collections.LinkedHashSet

object FakeDataSource {

    var newsItems = MutableLiveData<MutableList<NewsItem>>()

    var hiddenItems = MutableLiveData<LinkedHashSet<NewsItem>>()

    var likedItems = MutableLiveData<LinkedHashSet<NewsItem>>()

    init {
        newsItems.value = ArrayList()
        hiddenItems.value = LinkedHashSet()
        likedItems.value = LinkedHashSet()
    }
}
