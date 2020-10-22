package ru.sviridov.newsfeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_news_feed_group.*
import ru.sviridov.newsfeed.FeedType
import ru.sviridov.newsfeed.R
import ru.sviridov.newsfeed.presentation.adapter.ViewPagerAdapter
import ru.sviridov.newsfeed.presentation.viewmodel.NewsFeedGroupViewModel

class NewsFeedGroupFragment : Fragment() {

    private val viewModel by viewModels<NewsFeedGroupViewModel>()

    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var menu: Menu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news_feed_group, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpPagerAdapter()

        setUpInitialMenuState()

        setScreenState(NewsFeedGroupState.REGULAR_ONLY)

        setUpBottomNavListener()

        viewModel.favouriteTabEnabled.observe(viewLifecycleOwner, { tabEnabled ->
            if (tabEnabled) {
                setScreenState(NewsFeedGroupState.REGULAR_AND_FAVOURITE)
            } else {
                setScreenState(NewsFeedGroupState.REGULAR_ONLY)
            }
        })
    }

    private fun setUpInitialMenuState() {
        menu = bottomNavigationView.menu
        menu.add(
            Menu.NONE,
            MENU_ITEM_ID_ONE,
            MENU_ORDER_FIRST,
            getString(R.string.menu_item_newsfeed)
        )
            .setIcon(R.drawable.ic_nav_news)
    }

    private fun setUpPagerAdapter() {
        pagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        fragmentViewPager.adapter = pagerAdapter
        fragmentViewPager.isUserInputEnabled = false
    }

    private fun setScreenState(state: NewsFeedGroupState) {

        if (state == NewsFeedGroupState.REGULAR_ONLY) {
            pagerAdapter.removeOptionalFavouritesFragment()
            menu.removeItem(MENU_ITEM_ID_TWO)
        }

        if (state == NewsFeedGroupState.REGULAR_AND_FAVOURITE) {
            pagerAdapter.addOptionalFavouritesFragment(FeedFragment.newInstance(FeedType.FAVOURITE))

            menu.add(
                Menu.NONE,
                MENU_ITEM_ID_TWO,
                MENU_ORDER_SECOND,
                getString(R.string.menu_item_favourite_news)
            )
                .setIcon(R.drawable.ic_nav_favourite)
        }
    }

    private fun setUpBottomNavListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                MENU_ITEM_ID_ONE -> {
                    fragmentViewPager.currentItem = MENU_ORDER_FIRST
                }
                MENU_ITEM_ID_TWO -> {
                    fragmentViewPager.currentItem = MENU_ORDER_SECOND
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    companion object {
        const val MENU_ITEM_ID_ONE = 1111
        const val MENU_ITEM_ID_TWO = 2222
        const val MENU_ORDER_FIRST = 0
        const val MENU_ORDER_SECOND = 1
    }
}
