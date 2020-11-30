package ru.sviridov.vkclient.feature_auth.handler

import com.vk.api.sdk.auth.VKAccessToken

object TokenHolder {
    var token: VKAccessToken? = null
}