package ru.sviridov.vkclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.sviridov.vkclient.ui.presentation.AlertDialogBuilder
import ru.sviridov.vkclient.ui.presentation.fragments.BottomNavContainerFragment
import ru.sviridov.vkclient.ui.presentation.fragments.comments.PostCommentsFragment
import ru.sviridov.vkclient.ui.presentation.fragments.newsfeed.FeedFragmentHost
import ru.sviridov.vkclient.ui.presentation.fragments.details.FeedItemDetailsFragment
import ru.sviridov.vkclient.ui.presentation.fragments.profile.WallFragmentHost
import ru.sviridov.vkclient.ui.presentation.fragments.wall.WallFragment

class MainActivity : AppCompatActivity(), FeedFragmentHost, WallFragmentHost {

    private var savedStateIsExists = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showNewsGroupFragment()
    }

    private fun showNewsGroupFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragmentContainer, BottomNavContainerFragment()).commit()

    }

    override fun openDetails(url: String) {
        val fragmentDetails = FeedItemDetailsFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragmentDetails)
            .addToBackStack(null)
            .commit()
    }

    override fun openCommentFragment(sourceId: Int, postId: Int) {
        val commentFragment = PostCommentsFragment
            .newInstance(
                postId,
                sourceId
            )
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, commentFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun openWallFragment(profileId: Int) {
        val wallFragment = WallFragment
            .newInstance(profileId)

        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, wallFragment)
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

        fun startFrom(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
