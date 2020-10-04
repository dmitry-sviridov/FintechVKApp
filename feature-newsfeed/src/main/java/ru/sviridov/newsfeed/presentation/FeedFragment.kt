package ru.sviridov.newsfeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_feed.*
import ru.sviridov.newsfeed.R
import ru.sviridov.newsfeed.presentation.adapter.FeedAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {

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
        feedAdapter = FeedAdapter()
        viewModel.updateNewsFeed()
        initRecycler()
    }

    private fun initRecycler() {
        feedRecycler.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = feedAdapter
        }

        viewModel.getNewsItems().observe(viewLifecycleOwner, Observer {
            it.let(feedAdapter::submitList)
        })
    }

}