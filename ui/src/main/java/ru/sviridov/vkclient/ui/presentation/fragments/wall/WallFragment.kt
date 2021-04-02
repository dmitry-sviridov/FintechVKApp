package ru.sviridov.vkclient.ui.presentation.fragments.wall

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.wall_fragment.*
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.core.extension.viewModels
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.di.UiComponentInjector
import ru.sviridov.vkclient.ui.onRightDrawableClicked
import ru.sviridov.vkclient.ui.presentation.adapter.PostAdapter
import ru.sviridov.vkclient.ui.presentation.adapter.PostAdapterActionHandler
import ru.sviridov.vkclient.ui.presentation.mvi.wall.WallViewAction
import ru.sviridov.vkclient.ui.presentation.mvi.wall.WallViewState
import ru.sviridov.vkclient.ui.presentation.viewmodel.wall.WallViewModel
import javax.inject.Inject
import javax.inject.Provider

class WallFragment : Fragment(), PostAdapterActionHandler {

    private val profileId: Int by lazy { requireArguments().get(PROFILE_ID) as Int }

    @Inject
    internal lateinit var vmProvider: Provider<WallViewModel>
    private val wallViewModel: WallViewModel by viewModels { vmProvider.get() }

    private val wallAdapter: PostAdapter by lazy { PostAdapter(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wall_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        UiComponentInjector.getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wallViewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            Log.e(TAG, "observing viewstate: $viewState ")
            render(viewState)
        })

        initRecycler()
        wallViewModel.handleAction(WallViewAction.FetchWallPosts(profileId))

        newWallPostInput.onRightDrawableClicked {
            wallViewModel.handleAction(
                WallViewAction.CreateNewTextWallPost(
                    profileId,
                    it.text.toString()
                )
            )
            it.text.clear()
        }
    }

    private fun initRecycler() {
        val context = requireContext()

        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(
            AppCompatResources.getDrawable(
                context,
                R.drawable.divider
            )!!
        )

        val linearLayoutManager = LinearLayoutManager(this.context)

        wallRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = wallAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun render(viewState: WallViewState) {
        when (viewState) {
            is WallViewState.ShowWallItems -> renderWallItems(viewState.items)
            is WallViewState.ShowError -> renderError()
            is WallViewState.ShowEmptyState -> renderEmpty()
        }
    }

    private fun renderEmpty() {
        errorView.visibility = View.INVISIBLE
        wallPostLoader.visibility = View.INVISIBLE

        emptyView.visibility = View.VISIBLE
    }

    private fun renderError() {
        wallPostLoader.visibility = View.INVISIBLE
        emptyView.visibility = View.INVISIBLE

        errorView.visibility = View.VISIBLE
    }

    private fun renderWallItems(fetchedList: List<NewsItem>) {
        wallPostLoader.visibility = View.INVISIBLE
        emptyView.visibility = View.INVISIBLE
        errorView.visibility = View.INVISIBLE

        Log.d(TAG, "renderWallItems")
        wallAdapter.postList = fetchedList
    }


    companion object {
        private const val TAG = "WallFragment"
        private const val PROFILE_ID = "PROFILE_ID"

        fun newInstance(profileId: Int) =
            WallFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(PROFILE_ID, profileId)
                }
            }
    }

    override fun onImageViewClicked(url: String) {
        return
        //TODO
    }

    override fun onCommentsClicked(item: NewsItem) {
        return
        //TODO
    }

    override fun onItemHided(item: NewsItem) {
        return
        //TODO
    }

    override fun onItemLiked(item: NewsItem, shouldBeLiked: Boolean) {
        return
        //TODO
    }

}