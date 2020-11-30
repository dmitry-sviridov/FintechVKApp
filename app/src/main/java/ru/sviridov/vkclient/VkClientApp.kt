package ru.sviridov.vkclient

import android.app.Application
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import ru.sviridov.vkclient.di.AppInjector
import ru.sviridov.vkclient.feature_auth.handler.TokenHolder

class VkClientApp : Application() {

    private lateinit var vkPrefs: VKPreferencesKeyValueStorage

    override fun onCreate() {
        super.onCreate()

        initTokenPreferences()

        initDi()

        VK.addTokenExpiredHandler(tokenTracker)
    }

    private fun initTokenPreferences() {
        vkPrefs = VKPreferencesKeyValueStorage(this, BuildConfig.VK_TOKEN_PREFS_NAME)
    }

    fun saveTokenToPrefs(token: VKAccessToken) {
        token.save(vkPrefs)
        TokenHolder.token = token
    }

    fun restoreTokenFromPrefs(): VKAccessToken? {
        TokenHolder.token = VKAccessToken.restore(vkPrefs)
        return TokenHolder.token
    }

    private fun initDi() {
        AppInjector.createAppComponent(this)
    }

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            VKAccessToken.remove(vkPrefs)
            LoginActivity.startFrom(this@VkClientApp)
            TokenHolder.token = null
        }
    }
}