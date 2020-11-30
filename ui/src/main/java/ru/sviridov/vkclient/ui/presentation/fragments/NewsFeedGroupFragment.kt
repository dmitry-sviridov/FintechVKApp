package ru.sviridov.vkclient.ui.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_news_feed_group.*
import ru.sviridov.core.extension.viewModels
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.di.UiComponentInjector
import ru.sviridov.vkclient.ui.presentation.adapter.ViewPagerAdapter
import ru.sviridov.vkclient.ui.presentation.enums.FeedType
import ru.sviridov.vkclient.ui.presentation.enums.NewsFeedGroupState
import ru.sviridov.vkclient.ui.presentation.viewmodel.NewsFeedGroupViewModel
import javax.inject.Inject
import javax.inject.Provider

class NewsFeedGroupFragment : Fragment() {

    @Inject
    internal lateinit var vmProvider: Provider<NewsFeedGroupViewModel>
    private val viewModel: NewsFeedGroupViewModel by viewModels { vmProvider.get() }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UiComponentInjector.getComponent().inject(this)
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
