package ru.sviridov.vkclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import ru.sviridov.vkclient.network.auth.TokenHolder
import ru.sviridov.vkclient.ui.presentation.AlertDialogBuilder
import ru.sviridov.vkclient.ui.presentation.fragments.FeedFragmentHost
import ru.sviridov.vkclient.ui.presentation.fragments.FeedItemDetailsFragment
import ru.sviridov.vkclient.ui.presentation.fragments.NewsFeedGroupFragment

class MainActivity : AppCompatActivity(), FeedFragmentHost {

    private var savedStateIsExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedStateIsExists = savedInstanceState != null
        if (!savedStateIsExists) {
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS, VKScope.GROUPS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                TokenHolder.token = token
                showNewsGroupFragment()
                Log.d("VKAUTH/LOGIN", "onLogin: success")
            }

            override fun onLoginFailed(errorCode: Int) {
                Snackbar
                    .make(findViewById(R.id.content), "onLoginFailed", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showNewsGroupFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (!savedStateIsExists) {
            fragmentTransaction.replace(R.id.fragmentContainer, NewsFeedGroupFragment()).commit()
        }
    }

    override fun openDetails(url: String) {
        val fragmentDetails = FeedItemDetailsFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragmentDetails)
            .addToBackStack(null)
            .commit()
    }

    override fun showErrorDialog(message: String?) {
        AlertDialogBuilder.showDialog(
            this, getString(R.string.newsfeed_error_dialog_title),
            msg = message ?: getString(R.string.newsfeed_error_dialog_text),
            positiveBtnText = "Ok",
            negativeBtnText = null,
            positiveBtnClickListener = { dialog, _ -> dialog.cancel() },
            negativeBtnClickListener = null
        )
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
