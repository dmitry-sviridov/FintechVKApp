package ru.sviridov.newsfeed.domain.implementation

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.sviridov.newsfeed.data.FakeDataSource
import ru.sviridov.newsfeed.domain.NewsFeedRepository
import ru.sviridov.newsfeed.domain.dto.NewsResponse
import ru.sviridov.newsfeed.fromFile
import ru.sviridov.newsfeed.mapResponseToItem
import ru.sviridov.newsfeed.notifyObserver
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

internal class NewsFeedRepositoryFakeImpl(private val assetManager: AssetManager) :
    NewsFeedRepository {

    private val dataSource = FakeDataSource

    var newsItems = dataSource.newsItems
        private set

    var hiddenItems = dataSource.hiddenItems
        private set

    var likedItems = dataSource.likedItems
        private set


    override fun fetchNews(filter: Any?) {
        if (newsItems.value.isNullOrEmpty()) {
            val jsonString = fromFile("posts.json", assetManager = assetManager)
            val mapper = jacksonObjectMapper()
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)

            val readValue = mapper.readValue<NewsResponse>(jsonString)
            val newsList = mapResponseToItem(readValue) as MutableList
            val hiddenList = hiddenItems.value

            newsList.removeAll(hiddenList!!)

            newsItems.value = newsList
            updateLikedList()
        } else {
            newsItems.notifyObserver()
        }
    }

    override fun setNewsItemLiked(item: NewsItem) {
        newsItems.value!!.find { it.postId == item.postId }?.let {
            it.likesCount++
            it.isLiked = true
        }
        newsItems.notifyObserver()
        updateLikedList()
    }

    override fun setNewsItemDisliked(item: NewsItem) {
        newsItems.value?.find { it.postId == item.postId }?.let {
            it.likesCount--
            it.isLiked = false
        }
        newsItems.notifyObserver()
        updateLikedList()
    }

    override fun setItemAsHidden(item: NewsItem) {
        hiddenItems.value?.add(item)
        newsItems.value?.remove(item)
        likedItems.value?.remove(item)

        newsItems.notifyObserver()
        hiddenItems.notifyObserver()
    }

    private fun updateLikedList() {
        likedItems.value =
            newsItems.value?.filter { it.isLiked == true }?.toCollection(LinkedHashSet())
    }
}
