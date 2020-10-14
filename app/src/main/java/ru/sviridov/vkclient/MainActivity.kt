package ru.sviridov.vkclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.sviridov.newsfeed.presentation.FeedFragment
import ru.sviridov.newsfeed.presentation.NewsFeedGroupFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (savedInstanceState == null) {
            fragmentTransaction.replace(R.id.fragmentContainer, NewsFeedGroupFragment()).commit()
        }
    }
}
