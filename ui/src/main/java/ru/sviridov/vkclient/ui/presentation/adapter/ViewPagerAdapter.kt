package ru.sviridov.vkclient.ui.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.sviridov.vkclient.ui.presentation.enums.FeedType
import ru.sviridov.vkclient.ui.presentation.fragments.FeedFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    // Init adapter with first tab, always required
    private val fragments: MutableList<Fragment> =
        mutableListOf(FeedFragment.newInstance(FeedType.REGULAR_FEED))

    // Two methods for dynamically managing Favourites tab
    fun addOptionalFavouritesFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    fun removeOptionalFavouritesFragment() {
        if (fragments.size > 1) {
            fragments.removeLast()
            notifyDataSetChanged()
        }
    }

    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}

/*

 */