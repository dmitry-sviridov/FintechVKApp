package ru.sviridov.vkclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.sviridov.newsfeed.presentation.DetailsFragmentHost
import ru.sviridov.newsfeed.presentation.FeedFragment
import ru.sviridov.newsfeed.presentation.FeedItemDetailsFragment
import ru.sviridov.newsfeed.presentation.NewsFeedGroupFragment

class MainActivity : AppCompatActivity(), DetailsFragmentHost {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (savedInstanceState == null) {
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
}
