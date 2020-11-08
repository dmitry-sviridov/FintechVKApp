package ru.sviridov.newsfeed.presentation

interface FeedFragmentHost {
    fun openDetails(url: String)
    fun showErrorDialog(message: String?)
}