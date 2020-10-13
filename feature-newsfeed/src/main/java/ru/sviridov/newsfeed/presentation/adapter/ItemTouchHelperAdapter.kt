package ru.sviridov.newsfeed.presentation.adapter

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onItemApprove(position: Int)
}
