package ru.sviridov.newsfeed.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.sviridov.newsfeed.FeedType
import ru.sviridov.newsfeed.presentation.FeedFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // Init adapter with first tab, always required
    private val fragments: MutableList<Fragment> = mutableListOf(FeedFragment.newInstance(FeedType.REGULAR_FEED))

    // Two methods for dynamically managing Favourites tab
    fun addOptionalFavouritesFragment(fragment: Fragment) {
        this.fragments.add(fragment)
        notifyItemInserted(fragments.lastIndex)
    }

    fun removeOptionalFavouritesFragment() {
        if (fragments.size > 1) {
            this.fragments.removeLast()
            notifyItemRangeRemoved(0, fragments.size)
        }
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}