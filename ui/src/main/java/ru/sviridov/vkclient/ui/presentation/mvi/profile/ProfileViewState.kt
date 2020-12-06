package ru.sviridov.vkclient.ui.presentation.mvi.profile

import ru.sviridov.vkclient.component.profile.model.ProfileModel

sealed class ProfileViewState {
    class RenderProfileInfo(val profileModel: ProfileModel): ProfileViewState()
    object RenderError: ProfileViewState()
    object RenderWallItems: ProfileViewState()
}