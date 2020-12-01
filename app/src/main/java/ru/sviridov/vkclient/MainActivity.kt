package ru.sviridov.vkclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.sviridov.vkclient.ui.presentation.AlertDialogBuilder
import ru.sviridov.vkclient.ui.presentation.fragments.BottomNavContainerFragment
import ru.sviridov.vkclient.ui.presentation.fragments.FeedFragmentHost
import ru.sviridov.vkclient.ui.presentation.fragments.FeedItemDetailsFragment

class MainActivity : AppCompatActivity(), FeedFragmentHost {

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
