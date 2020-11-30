package ru.sviridov.vkclient.ui.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.core.extension.viewModels
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.di.NewsFeedInjector
import ru.sviridov.vkclient.ui.presentation.adapter.AdapterActionHandler
import ru.sviridov.vkclient.ui.presentation.adapter.FeedAdapter
import ru.sviridov.vkclient.ui.presentation.adapter.swipe.FeedItemCustomTouchHelperCallback
import ru.sviridov.vkclient.ui.presentation.enums.FeedType
import ru.sviridov.vkclient.ui.presentation.mvi.FeedViewActions
import ru.sviridov.vkclient.ui.presentation.mvi.FeedViewState
import ru.sviridov.vkclient.ui.presentation.viewmodel.FeedViewModel
import javax.inject.Inject
import javax.inject.Provider

class FeedFragment : Fragment(), AdapterActionHandler {

    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(this) }
    private val feedType: FeedType by lazy { requireArguments().get(FEED_TYPE) as FeedType }

    @Inject
    internal lateinit var vmProvider: Provider<FeedViewModel>
    private val feedViewModel: FeedViewModel by viewModels { vmProvider.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        NewsFeedInjector.getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshLayout.isEnabled = false

        feedViewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            render(viewState)
        })

        if (feedType == FeedType.REGULAR_FEED) {
            setUpRefreshLayout()
            feedViewModel.handleAction(FeedViewActions.GetFreshNews)
        } else {
            feedViewModel.handleAction(FeedViewActions.GetLikedNews)
        }
        initRecycler()
    }

    private fun initRecycler() {
        val context = requireContext()

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(getDrawable(context, R.drawable.divider)!!)

        val linearLayoutManager = LinearLayoutManager(this.context)

        feedRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = feedAdapter
            addItemDecoration(dividerItemDecoration)
            itemAnimator?.changeDuration = ITEM_ANIMATOR_DURATION
        }

        val itemTouchHelperCallback = FeedItemCustomTouchHelperCallback(feedAdapter, context)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(feedRecycler)

        feedRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!refreshLayout.isRefreshing && feedType == FeedType.REGULAR_FEED) {
                    if (linearLayoutManager
                            .findLastVisibleItemPosition() == feedAdapter.itemCount - 1
                    ) {
                        feedViewModel.handleAction(FeedViewActions.GetPreviousNews)
                    }
                }
            }
        })
    }

    private fun render(state: FeedViewState) {
        when (state) {
            FeedViewState.Loading -> renderLoadingState()
            is FeedViewState.ShowApiError -> renderError(state.apiError)
            is FeedViewState.ShowNewsFeed -> renderUpdatedFeed(
                state.newsList,
                state.scrollRecyclerUp
            )
        }
    }

    private fun setUpRefreshLayout() {
        refreshLayout.isEnabled = true
        refreshLayout.setOnRefreshListener {
            feedViewModel.handleAction(FeedViewActions.GetFreshNews)
        }
    }

    override fun onImageViewClicked(url: String) {
        (requireActivity() as FeedFragmentHost).openDetails(url)
    }

    override fun onItemHided(item: NewsItem) {
        feedViewModel.handleAction(FeedViewActions.HideCurrentItem(item))
    }

    override fun onItemLiked(item: NewsItem, shouldBeLiked: Boolean) {
        if (shouldBeLiked) {
            feedViewModel.handleAction(FeedViewActions.SetCurrentItemAsLiked(item))
        } else {
            feedViewModel.handleAction(FeedViewActions.SetCurrentItemAsDisliked(item))
        }
    }

    private fun renderLoadingState() {
        refreshLayout.isRefreshing = true
    }

    private fun renderError(apiError: Throwable) {
        (requireActivity() as FeedFragmentHost).showErrorDialog(apiError.message)
        refreshLayout.isRefreshing = false
    }

    private fun renderUpdatedFeed(newList: List<NewsItem>, scrollRecyclerUp: Boolean) {
        feedAdapter.newsList = newList
        refreshLayout.isRefreshing = false
        if (scrollRecyclerUp) {
            feedRecycler.handler.postDelayed({
                try {
                    feedRecycler.scrollToPosition(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 500)
        }
    }

    companion object {
        const val ITEM_ANIMATOR_DURATION = 50L

        private const val FEED_TYPE = "feed_type"

        fun newInstance(feedType: FeedType) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(FEED_TYPE, feedType)
                }
            }
    }
}
