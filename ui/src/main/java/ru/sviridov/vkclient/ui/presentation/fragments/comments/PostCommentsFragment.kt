package ru.sviridov.vkclient.ui.presentation.fragments.comments

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.post_comments_fragment.*
import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.core.extension.viewModels
import ru.sviridov.vkclient.feature_newsfeed.model.CommentModel
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.di.UiComponentInjector
import ru.sviridov.vkclient.ui.onRightDrawableClicked
import ru.sviridov.vkclient.ui.presentation.adapter.CommentAdapter
import ru.sviridov.vkclient.ui.presentation.adapter.CommentAdapterActionHandler
import ru.sviridov.vkclient.ui.presentation.mvi.comments.CommentViewAction
import ru.sviridov.vkclient.ui.presentation.mvi.comments.CommentViewState
import ru.sviridov.vkclient.ui.presentation.viewmodel.comments.PostCommentsViewModel
import javax.inject.Inject
import javax.inject.Provider

class PostCommentsFragment : Fragment(), CommentAdapterActionHandler {

    private val TAG = "FeedFragment" + "@" + this.hashCode()

    private val commentsAdapter: CommentAdapter by lazy { CommentAdapter(this) }

    @Inject
    internal lateinit var vmProvider: Provider<PostCommentsViewModel>
    private val commentsViewModel: PostCommentsViewModel by viewModels { vmProvider.get() }

    private val postId: Int by lazy { requireArguments().get(POST_ID) as Int }
    private val sourceId: Int by lazy { requireArguments().get(SOURCE_ID) as Int }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_comments_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach postId = $postId, sourceId = $sourceId")
        UiComponentInjector.getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        progressCircular.visibility = View.VISIBLE

        commentsViewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            Log.e(TAG, "observing viewstate: $viewState ")
            render(viewState)
        })

        initRecycler()

        commentsViewModel.handleAction(CommentViewAction.FetchComments(postId, sourceId))

        textInput.onRightDrawableClicked {
            val newComment = CommentModel(
                text = it.text.toString(),
                ownerId = sourceId,
                postId = postId
            )
            commentsViewModel.handleAction(CommentViewAction.SendComment(newComment))
            it.text.clear()
        }
    }

    private fun initRecycler() {
        val linearLayoutManager = LinearLayoutManager(this.context)

        commentsRecycler.apply {
            layoutManager = linearLayoutManager
            adapter = commentsAdapter
        }
    }

    private fun render(viewState: CommentViewState) {
        when (viewState) {
            CommentViewState.ShowError -> renderError()
            is CommentViewState.ShowNoContent -> renderNoComments(viewState.isAbleToSendComment)
            is CommentViewState.ShowComments -> renderComments(
                comments = viewState.comments,
                canPostComment = viewState.isAbleToSendComment,
                isUpdate = false
            )
            is CommentViewState.UpdateComments -> renderComments(
                comments = viewState.comments,
                canPostComment = viewState.isAbleToSendComment,
                isUpdate = true
            )
        }
    }

    override fun onCommentMarkedAsLiked() {
        TODO("Not yet implemented")
    }

    private fun renderNoComments(canPostComment: Boolean) {
        progressCircular.visibility = View.INVISIBLE
        if (!canPostComment) {
            disableCommentInput()
        }
        emptyView.visibility = View.VISIBLE
    }

    private fun renderError() {
        progressCircular.visibility = View.INVISIBLE
        disableCommentInput()
        commentsRecycler.visibility = View.INVISIBLE
        errorView.visibility = View.VISIBLE
        textInput.visibility = View.INVISIBLE
    }

    private fun renderComments(
        comments: List<PostCommentItem>,
        canPostComment: Boolean,
        isUpdate: Boolean
    ) {
        if (!canPostComment) {
            disableCommentInput()
        }
        progressCircular.visibility = View.INVISIBLE
        commentsAdapter.commentsList = comments

        if (isUpdate) {
            commentsRecycler.smoothScrollToPosition(comments.size)
        }
    }

    private fun disableCommentInput() {
        textInput.inputType = InputType.TYPE_NULL
        textInput.hint = getString(R.string.comment_ability_disabled_edittext_hint)
    }

    companion object {
        private const val POST_ID = "POST_ID"
        private const val SOURCE_ID = "SOURCE_ID"

        fun newInstance(postId: Int, sourceId: Int) =
            PostCommentsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(POST_ID, postId)
                    putSerializable(SOURCE_ID, sourceId)
                }
            }
    }
}