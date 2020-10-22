package ru.sviridov.newsfeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.sviridov.newsfeed.FeedType
import ru.sviridov.newsfeed.R
import ru.sviridov.newsfeed.presentation.adapter.FeedAdapter
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import ru.sviridov.newsfeed.presentation.adapter.swipe.FeedItemCustomTouchHelperCallback
import ru.sviridov.newsfeed.presentation.viewmodel.FeedViewModel
import ru.sviridov.newsfeed.presentation.viewmodel.FeedViewModelFactory

class FeedFragment : Fragment(), AdapterActionHandler {

    private lateinit var feedAdapter: FeedAdapter
    private lateinit var feedType: FeedType

    private val viewModel by viewModels<FeedViewModel> {
        FeedViewModelFactory(requireActivity().assets)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            feedType = it.get(FEED_TYPE) as FeedType
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.updateFeed()
        setUpRefreshLayout()
        initRecycler()
    }

    private fun initRecycler() {
        feedAdapter = FeedAdapter(this)
        val context = requireContext()

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(getDrawable(context, R.drawable.divider)!!)

        feedRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = feedAdapter
            addItemDecoration(dividerItemDecoration)
            itemAnimator?.changeDuration = ITEM_ANIMATOR_DURATION
        }

        setUpFeedListener()

        val itemTouchHelperCallback = FeedItemCustomTouchHelperCallback(feedAdapter, context)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(feedRecycler)
    }

    private fun setUpFeedListener() {
        if (feedType == FeedType.REGULAR_FEED) {
            viewModel.newsItems.observe(viewLifecycleOwner, {
                feedAdapter.newsList = it
                refreshLayout.isRefreshing = false
            })
        } else {
            viewModel.likedItems.observe(viewLifecycleOwner, {
                feedAdapter.newsList = it.toList()
            })
        }
    }

    private fun setUpRefreshLayout() {
        // setUp refresh layout only for regular feed - favourites uses LiveData for updates
        if (feedType == FeedType.REGULAR_FEED) {
            refreshLayout.setOnRefreshListener {
                viewModel.updateFeed()
            }
        } else {
            refreshLayout.isEnabled = false
        }
    }

    override fun onImageViewClicked(url: String) {
        (requireActivity() as DetailsFragmentHost).openDetails(url)
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
