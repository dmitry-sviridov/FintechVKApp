package ru.sviridov.newsfeed.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.feed_item_layout.*
import kotlinx.android.synthetic.main.feed_item_layout.view.*
import kotlinx.android.synthetic.main.fragment_feed_item_details.*
import ru.sviridov.newsfeed.R

const val IMAGE_URL = "image_url"

class FeedItemDetailsFragment : Fragment() {

    private lateinit var imageURL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageURL = it.get(IMAGE_URL) as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity())
            .load(imageURL)
            .into(detailsImageView)

        detailsImageView.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(imageURL: String) =
            FeedItemDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(IMAGE_URL, imageURL)
                }
            }
    }
}
