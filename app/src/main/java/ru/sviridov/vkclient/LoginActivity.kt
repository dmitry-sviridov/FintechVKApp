package ru.sviridov.vkclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as VkClientApp).restoreTokenFromPrefs()

        if (VK.isLoggedIn()) {
            MainActivity.startFrom(this)
            finish()
            return
        }
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS, VKScope.GROUPS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                (applicationContext as VkClientApp).saveTokenToPrefs(token)
                MainActivity.startFrom(this@LoginActivity)
                finish()
            }

            override fun onLoginFailed(errorCode: Int) {
                Snackbar
                    .make(findViewById(R.id.content), "onLoginFailed", Snackbar.LENGTH_LONG)
                    .show()
                Log.d(TAG, "onLoginFailed: error code = $errorCode")
            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val TAG = "LoginActivity"

        fun startFrom(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}