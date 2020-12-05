package ru.sviridov.vkclient.ui.presentation.fragments.newsfeed

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import ru.sviridov.vkclient.ui.di.UiComponentInjector
import ru.sviridov.vkclient.ui.presentation.adapter.FeedAdapterActionHandler
import ru.sviridov.vkclient.ui.presentation.adapter.FeedAdapter
import ru.sviridov.vkclient.ui.presentation.adapter.swipe.FeedItemCustomTouchHelperCallback
import ru.sviridov.vkclient.ui.presentation.enums.FeedType
import ru.sviridov.vkclient.ui.presentation.mvi.newsfeed.FeedViewActions
import ru.sviridov.vkclient.ui.presentation.mvi.newsfeed.FeedViewState
import ru.sviridov.vkclient.ui.presentation.viewmodel.newsfeed.FeedViewModel
import javax.inject.Inject
import javax.inject.Provider

class FeedFragment : Fragment(), FeedAdapterActionHandler {

    private val TAG = "FeedFragment" + "@" + this.hashCode()

    private val feedAdapter: FeedAdapter by lazy { FeedAdapter(this) }
    private val feedType: FeedType by lazy { requireArguments().get(FEED_TYPE) as FeedType }

    @Inject
    internal lateinit var vmProvider: Provider<FeedViewModel>
    private val feedViewModel: FeedViewModel by viewModels { vmProvider.get() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach $feedType")
        UiComponentInjector.getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView $feedType")
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: $feedType")
        refreshLayout.isEnabled = false

        feedViewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            Log.e(TAG, "observing viewstate: $viewState ")
            render(viewState)
        })

        initRecycler()

        if (feedType == FeedType.REGULAR_FEED) {
            setUpRefreshLayout()
            feedViewModel.handleAction(FeedViewActions.GetFreshNews)
        } else {
            feedViewModel.handleAction(FeedViewActions.GetLikedNews)
        }
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
            FeedViewState.StopLoading -> stopLoadingState()
            is FeedViewState.ShowError -> renderError(state.error)
            is FeedViewState.ShowNewsFeed -> renderUpdatedFeed(
                state.newsList,
                state.scrollRecyclerUp
            )
        }
    }

    private fun stopLoadingState() {
        Log.e(TAG, "stopLoadingState", )
        refreshLayout.isRefreshing = false
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

    override fun onCommentsClicked(item: NewsItem) {
        (requireActivity() as FeedFragmentHost).openCommentFragment(item.sourceId, item.postId)
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
        Log.e(TAG, "renderLoadingState", )
        refreshLayout.isRefreshing = true
    }

    private fun renderError(apiError: Throwable) {
        Log.e(TAG, "renderError", )
        (requireActivity() as FeedFragmentHost).showErrorDialog(apiError.message)
        refreshLayout.isRefreshing = false
    }

    private fun renderUpdatedFeed(newList: List<NewsItem>, scrollRecyclerUp: Boolean) {
        Log.e(TAG, "renderUpdatedFeed: newList.size = ${newList.size}")

        feedAdapter.newsList = newList
        refreshLayout.isRefreshing = false
        if (scrollRecyclerUp && feedAdapter.newsList.isNotEmpty()) {
            feedRecycler.handler.postDelayed({
                try {
                    feedRecycler.scrollToPosition(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 500)
        } else if (feedType == FeedType.FAVOURITE && feedAdapter.newsList.isNotEmpty()) {
            /*
              Грязный хак для обработки ситуации, когда вкладка с избранным появляется,
              но сначала переходим на таб с профилем и из него - на таб с избранным.
              До прокрутки / тач-евента на ресайклере не вызывается onBindViewHolder - хз почему
              ---
              Можете бить меня палками, если причина очевидна и должна быть понятна ежу, но я честно
              часов 6 потратил и не нашел причины.
             */
            feedRecycler.smoothScrollBy(0,1)
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: FEED_TYPE = $feedType")
        super.onDestroy()
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
