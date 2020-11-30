package ru.sviridov.vkclient.ui.presentation.fragments

interface FeedFragmentHost {
    fun openDetails(url: String)
    fun showErrorDialog(message: String?)
}