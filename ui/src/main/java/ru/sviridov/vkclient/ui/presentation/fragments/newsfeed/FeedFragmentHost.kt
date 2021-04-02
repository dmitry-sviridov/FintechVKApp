package ru.sviridov.vkclient.ui.presentation.fragments.newsfeed

interface FeedFragmentHost {
    fun openDetails(url: String)
    fun showErrorDialog(message: String?)
    fun openCommentFragment(sourceId: Int, postId: Int)
}