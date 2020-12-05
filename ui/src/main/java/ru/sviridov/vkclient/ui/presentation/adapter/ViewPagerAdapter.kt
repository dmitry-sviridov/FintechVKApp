package ru.sviridov.vkclient.ui.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.sviridov.vkclient.ui.presentation.enums.FeedType
import ru.sviridov.vkclient.ui.presentation.fragments.newsfeed.FeedFragment
import ru.sviridov.vkclient.ui.presentation.fragments.profile.ProfileFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: MutableList<Fragment> =
        mutableListOf(
            ProfileFragment.newInstance(),
            FeedFragment.newInstance(FeedType.REGULAR_FEED)
        )

    fun addOptionalFavouritesFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    fun removeOptionalFavouritesFragment() {
        if (fragments.size > 2) {
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
