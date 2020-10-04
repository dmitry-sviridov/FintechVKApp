package ru.sviridov.newsfeed.presentation.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.feed_item_layout.view.*
import ru.sviridov.newsfeed.R
import ru.sviridov.newsfeed.getItemType
import ru.sviridov.newsfeed.presentation.adapter.NewsFeedViewType.*
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import ru.sviridov.newsfeed.presentation.layout.FeedItemLayout
import java.lang.RuntimeException

class FeedAdapter : ListAdapter<NewsItem, FeedAdapter.BaseViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return (oldItem.postedAt == newItem.postedAt &&
                        oldItem.textContent == newItem.textContent &&
                        oldItem.imageUrl == newItem.imageUrl)
            }
        }

        private val TAG = this.javaClass.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = FeedItemLayout(parent.context)
        return when (viewType) {
            VIEW_WITH_TEXT_ONLY.value -> TextPostViewHolder(
                view
            )
            VIEW_WITH_SINGLE_PICTURE_ONLY.value -> ImagePostViewHolder(
                view
            )
            VIEW_WITH_SINGLE_PICTURE_AND_TEXT.value -> ImageWithTextPostViewHolder(
                view
            )
            else -> throw RuntimeException("Unknown view type = $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemType().value
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Single method for each view holder
        fun bind(item: NewsItem) {
            Log.d(TAG, "bind: $item")
            itemView.apply {
                postWriterTitleTextView.text = item.sourceTitle
                postCreatedAgoTextView.text = item.postedAt
                socialLikesView.text = item.likesCount.toString()
                socialRepostsView.text = item.shareCount.toString()
                socialCommentsView.text = item.commentCount.toString()
                socialViewsCountView.text = item.viewsCount.toString()

                Glide.with(context)
                    .load(item.sourceAvatar)
                    .circleCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "onLoadFailed: ")
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "onResourceReady: ")
                            return false
                        }

                    })
                    .into(avatarView)

                item.textContent?.let {
                    postItemTextView.text = it
                }

                item.imageUrl?.let {
                    Glide.with(context)
                        .load(it)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.d(TAG, "onLoadFailed: ")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.d(TAG, "onResourceReady: ")
                                return false
                            }

                        })
                        .into(postImageContainerView)
                }
            }
        }
    }

    // Multiple child classes for demonstration.
    class TextPostViewHolder(view: View) : BaseViewHolder(view)
    class ImageWithTextPostViewHolder(view: View) : BaseViewHolder(view)
    class ImagePostViewHolder(view: View) : BaseViewHolder(view)
}