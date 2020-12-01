package ru.sviridov.vkclient.ui.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.presentation.viewmodel.PostCommentsViewModel

class PostCommentsFragment : Fragment() {

    private lateinit var viewModel: PostCommentsViewModel
    private val postId: Int by lazy { requireArguments().get(POST_ID) as Int }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_comments_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostCommentsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    companion object {
        private const val POST_ID = "POST_ID"

        fun newInstance(postId: Int) =
            PostCommentsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(POST_ID, postId)
                }
            }
    }
}