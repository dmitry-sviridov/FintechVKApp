package ru.sviridov.newsfeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.DiffUtil.calculateDiff
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.sviridov.newsfeed.R
import ru.sviridov.newsfeed.presentation.adapter.FeedAdapter
import ru.sviridov.newsfeed.presentation.adapter.FeedDiffUtilsCallback
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import ru.sviridov.newsfeed.presentation.adapter.swipe.FeedItemCustomTouchHelperCallback

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment(), FeedAdapter.AdapterCallback {

    private lateinit var feedAdapter: FeedAdapter

    private val viewModel by viewModels<FeedViewModel> {
        FeedViewModelFactory(requireActivity().assets)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        feedAdapter = FeedAdapter(this)
        viewModel.updateNewsFeed()
        setUpRefreshLayout()
        initRecycler()
    }

    private fun initRecycler() {
        /*
            Denis, I don't know how to get context for providing to
                ItemTouchHelper in better way (without !!)
         */
        val context = requireContext()

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(getDrawable(context, R.drawable.divider)!!)

        feedRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = feedAdapter
            addItemDecoration(dividerItemDecoration)
        }

        viewModel.getNewsItems().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                feedAdapter.submitList(it) {
                    feedRecycler.scrollToPosition(0)
                }
            }

        })

        val itemTouchHelperCallback = FeedItemCustomTouchHelperCallback(feedAdapter, context)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(feedRecycler)
    }

    private fun setUpRefreshLayout() {
        refreshLayout.setOnRefreshListener {
            viewModel.updateNewsFeed()
            refreshLayout.isRefreshing = false
        }
    }

    override fun onItemHided(item: NewsItem) {
        viewModel.markItemAsHidden(item)
    }
}