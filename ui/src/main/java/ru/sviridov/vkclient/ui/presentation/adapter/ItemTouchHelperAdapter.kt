package ru.sviridov.vkclient.ui.presentation.adapter

interface ItemTouchHelperAdapter {
    fun onItemDismiss(position: Int)
    fun onItemApprove(position: Int)
}
