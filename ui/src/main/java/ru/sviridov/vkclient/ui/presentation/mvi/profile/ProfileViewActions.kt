package ru.sviridov.vkclient.ui.presentation.mvi.profile

sealed class ProfileViewActions {
    object FetchProfileInfo: ProfileViewActions()
    object FetchProfileWall: ProfileViewActions()
    object SendNewWallPost: ProfileViewActions()
}