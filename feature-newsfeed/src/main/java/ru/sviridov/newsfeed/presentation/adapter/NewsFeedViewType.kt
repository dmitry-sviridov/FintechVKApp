package ru.sviridov.newsfeed.presentation.adapter

enum class NewsFeedViewType(val value: Int) {
    VIEW_WITH_SINGLE_PICTURE_AND_TEXT(0),
    VIEW_WITH_TEXT_ONLY(1),
    VIEW_WITH_SINGLE_PICTURE_ONLY(2),
    UNKNOWN(3);
}