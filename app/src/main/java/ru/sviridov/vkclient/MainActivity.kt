package ru.sviridov.vkclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.sviridov.newsfeed.presentation.FeedFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, FeedFragment()).commit()
    }
}