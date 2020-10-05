package ru.sviridov.newsfeed.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem

class FeedDiffUtilsCallback() :
    DiffUtil.ItemCallback<NewsItem>() {

    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem.postedAt == newItem.postedAt && oldItem.sourceTitle == newItem.sourceTitle
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return oldItem == newItem
    }

}