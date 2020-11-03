package ru.sviridov.newsfeed.presentation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.sviridov.newsfeed.FeedType
import ru.sviridov.newsfeed.R
import ru.sviridov.newsfeed.data.db.item.NewsItem
import ru.sviridov.newsfeed.domain.FeedItemsDirection
import ru.sviridov.newsfeed.presentation.adapter.FeedAdapter
import ru.sviridov.newsfeed.presentation.adapter.swipe.FeedItemCustomTouchHelperCallback
import ru.sviridov.newsfeed.presentation.viewmodel.FeedViewModel
import ru.sviridov.newsfeed.presentation.viewmodel.FeedViewModelFactory

class FeedFragment : Fragment(), AdapterActionHandler {

    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(this) }
    private val feedType: FeedType by lazy { requireArguments().get(FEED_TYPE) as FeedType }

    private val viewModel by viewModels<FeedViewModel> {
        FeedViewModelFactory(requireActivity().application)
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
        if (feedType == FeedType.REGULAR_FEED) {
            viewModel.uploadFeedItems(FeedItemsDirection.FRESH)
            setUpRefreshLayout()
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

        setUpFeedListener()

        val itemTouchHelperCallback = FeedItemCustomTouchHelperCallback(feedAdapter, context)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(feedRecycler)

        feedRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!refreshLayout.isRefreshing) {
                    if (linearLayoutManager
                            .findLastVisibleItemPosition() == feedAdapter.itemCount - 1
                    ) {
                        viewModel.uploadFeedItems(FeedItemsDirection.PREVIOUS)
                        refreshLayout.isRefreshing = true
                    }
                }
            }
        })
    }

    private fun setUpFeedListener() {
        if (feedType == FeedType.REGULAR_FEED) {
            viewModel.newsItems.observe(viewLifecycleOwner, {
                feedAdapter.newsList = it
                refreshLayout.isRefreshing = false
            })

            viewModel.isErrorState.observe(viewLifecycleOwner, { errorShouldBeShown ->
                if (errorShouldBeShown) {
                    (requireActivity() as FeedFragmentHost).showErrorDialog()
                    refreshLayout.isRefreshing = false
                }
            })
            viewModel.recyclerScrollUp.observe(viewLifecycleOwner, { recyclerShouldBeScrolled ->
                if (recyclerShouldBeScrolled) {
                    Handler().postDelayed({
                        try {
                            feedRecycler.scrollToPosition(0)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, 500)
                }
            })
        } else {
            viewModel.likedItems.observe(viewLifecycleOwner, {
                feedAdapter.newsList = it.toList()
            })
        }
    }

    private fun setUpRefreshLayout() {
        refreshLayout.isEnabled = true
        refreshLayout.setOnRefreshListener {
            viewModel.uploadFeedItems(FeedItemsDirection.FRESH)
        }
    }

    override fun onImageViewClicked(url: String) {
        (requireActivity() as FeedFragmentHost).openDetails(url)
    }

    override fun onItemHided(item: NewsItem) {
        viewModel.markItemAsHidden(item)
    }

    override fun onItemLiked(item: NewsItem, shouldBeLiked: Boolean) {
        viewModel.changeItemLikeStatus(item, shouldBeLiked)
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
