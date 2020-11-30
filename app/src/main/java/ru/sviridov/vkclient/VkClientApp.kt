package ru.sviridov.vkclient

import android.app.Application
import android.widget.Toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import ru.sviridov.vkclient.network.di.AppInjector

class VkClientApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDi()

        VK.addTokenExpiredHandler(tokenTracker)
    }

    private fun initDi() {
        AppInjector.createAppComponent(this)
    }

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // Should be handled with force logout and redirecting to auth screen
            Toast.makeText(this@VkClientApp, "Token expired", Toast.LENGTH_LONG).show()
        }
    }
}