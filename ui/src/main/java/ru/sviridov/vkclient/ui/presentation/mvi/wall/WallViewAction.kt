package ru.sviridov.vkclient.ui.presentation.mvi.wall

sealed class WallViewAction {
    class CreateNewTextWallPost(val userId: Int, val message: String): WallViewAction()
    class FetchWallPosts(val userId: Int): WallViewAction()
}