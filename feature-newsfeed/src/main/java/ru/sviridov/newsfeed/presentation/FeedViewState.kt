package ru.sviridov.newsfeed.presentation

sealed class FeedState {
    object Loading: FeedState()
    class NetworkError(val message: String): FeedState()
    
}