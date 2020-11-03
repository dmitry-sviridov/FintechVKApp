package ru.sviridov.newsfeed.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_feed_item_details.*
import ru.sviridov.newsfeed.R
import java.io.ByteArrayOutputStream


class FeedItemDetailsFragment : Fragment() {

    private val imageURL: String by lazy { requireArguments().get(IMAGE_URL) as String }

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

        shareImageButton.setOnClickListener {
            // не работает
            val baos = ByteArrayOutputStream()
            detailsImageView.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageInByte = baos.toByteArray()

            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(Intent.EXTRA_STREAM, imageInByte)
            intent.type = "image/png"
            startActivity(Intent.createChooser(intent, "Share with"))

        }
    }

    companion object {
        const val IMAGE_URL = "image_url"

        fun newInstance(imageURL: String) =
            FeedItemDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(IMAGE_URL, imageURL)
                }
            }
    }
}
