package ru.sviridov.newsfeed.domain.implementation

import android.content.res.AssetManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.sviridov.newsfeed.domain.NewsFeedRepository
import ru.sviridov.newsfeed.domain.dto.NewsResponse
import ru.sviridov.newsfeed.fromFile

internal class NewsFeedRepositoryFakeImpl(private val assetManager: AssetManager) : NewsFeedRepository {

    override fun fetchNews(filter: Any?): NewsResponse {
        val jsonString = fromFile("posts.json", assetManager = assetManager)
        val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)

        return mapper.readValue(jsonString)
    }
}
