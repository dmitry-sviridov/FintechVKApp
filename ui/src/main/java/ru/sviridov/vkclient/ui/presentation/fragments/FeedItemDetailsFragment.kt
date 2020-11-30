package ru.sviridov.vkclient.ui.presentation.fragments

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_feed_item_details.*
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.di.UiComponentInjector
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class FeedItemDetailsFragment : Fragment() {

    private val imageURL: String by lazy { requireArguments().get(IMAGE_URL) as String }
    private val contentResolver: ContentResolver by lazy { requireActivity().contentResolver }

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
            shareImage()
        }
    }

    private fun shareImage() {
        val applicationContext = activity?.applicationContext ?: return

        val bitmap = detailsImageView.drawToBitmap()

        val fileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpg"

        saveImageToStream(bitmap, applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE))

        val filesDir = applicationContext.filesDir
        val imagePath = File(filesDir, ".")
        val authority = UiComponentInjector.appId
        val newFile = File(imagePath, fileName)
        val uri = FileProvider.getUriForFile(applicationContext, authority, newFile)

        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            type = "image/jpeg"
        }

        activity?.startActivity(Intent.createChooser(intent, getText(R.string.share_image_with)))
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream) {
        try {
            outputStream.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            }
        } catch (e: Exception) {
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                getString(R.string.image_share_error_text),
                Snackbar.LENGTH_SHORT
            ).show()
            Log.d(TAG, "saveImageToStream: ${e.message}")
            return
        }

    }


    companion object {
        const val IMAGE_URL = "image_url"
        private const val TAG = "FeedItemDetailsFragment"

        fun newInstance(imageURL: String) =
            FeedItemDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(IMAGE_URL, imageURL)
                }
            }
    }
}
