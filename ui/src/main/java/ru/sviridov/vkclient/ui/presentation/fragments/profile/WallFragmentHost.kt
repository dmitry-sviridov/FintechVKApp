package ru.sviridov.vkclient.ui.presentation.fragments.profile

interface WallFragmentHost {
    fun openWallFragment(profileId: Int): Unit
}