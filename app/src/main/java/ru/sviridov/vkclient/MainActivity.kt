package ru.sviridov.vkclient

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import ru.sviridov.network.auth.TokenHolder
import ru.sviridov.newsfeed.presentation.AlertDialogBuilder
import ru.sviridov.newsfeed.presentation.FeedFragmentHost
import ru.sviridov.newsfeed.presentation.FeedItemDetailsFragment
import ru.sviridov.newsfeed.presentation.NewsFeedGroupFragment

class MainActivity : AppCompatActivity(), FeedFragmentHost {

    // Denis, don't know how to handle savedInstantState != null outside onCreate method
    private var savedStateIsExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedStateIsExists = savedInstanceState != null
        VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS, VKScope.GROUPS))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                TokenHolder.token = token
                Snackbar
                    .make(fragmentContainer, "onLogin", Snackbar.LENGTH_LONG).show()
                showNewsGroupFragment()
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

    override fun showErrorDialog() {
        AlertDialogBuilder.showDialog(
            this, getString(R.string.newsfeed_error_dialog_title),
            msg = getString(R.string.newsfeed_error_dialog_text),
            positiveBtnText = "Ok",
            negativeBtnText = null,
            positiveBtnClickListener = { dialog, _ -> dialog.cancel() },
            negativeBtnClickListener = null
        )
    }
}
