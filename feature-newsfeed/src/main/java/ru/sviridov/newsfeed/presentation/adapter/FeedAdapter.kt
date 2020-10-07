package ru.sviridov.newsfeed.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.feed_item_layout.view.*
import ru.sviridov.newsfeed.getItemType
import ru.sviridov.newsfeed.presentation.adapter.NewsFeedViewType.*
import ru.sviridov.newsfeed.presentation.adapter.item.NewsItem
import ru.sviridov.newsfeed.presentation.layout.FeedItemLayout


class FeedAdapter(val callback: AdapterCallback) :
    RecyclerView.Adapter<FeedAdapter.BaseViewHolder>(),
    ItemTouchHelperAdapter {

    private val differ = AsyncListDiffer(this, FeedDiffUtilsCallback())
    var newsList: List<NewsItem>
        set(value) {
            differ.submitList(value)
        }
        get() = differ.currentList

    fun submitList(data: List<NewsItem>, onSuccess: () -> Unit) {
        differ.submitList(data, onSuccess)
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

    override fun getItemCount(): Int = newsList.size

    override fun getItemViewType(position: Int): Int {
        return newsList[position].getItemType().value
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    // Using payload broke the behaviour of swipe-to-like
    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!payloads.isNullOrEmpty()) {
            if (payloads.first() is Boolean) {
                holder.bind(newsList[position], payloads.first() as Boolean)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    interface AdapterCallback {
        fun onItemHided(item: NewsItem)
    }

    abstract inner class BaseViewHolder(view: FeedItemLayout) : RecyclerView.ViewHolder(view) {
        // Single method for each view holder
        fun bind(item: NewsItem) {
            (itemView as FeedItemLayout).apply {
                postWriterTitleTextView.text = item.sourceTitle
                postCreatedAgoTextView.text = item.postedAt
                socialLikesView.text = item.likesCount.toString()
                socialRepostsView.text = item.shareCount.toString()
                socialCommentsView.text = item.commentCount.toString()
                socialViewsCountView.text = item.viewsCount.toString()
                item.isLiked?.let {
                    if (it) {
                        this.setLikeButtonEnabled()
                    } else {
                        this.setLikeButtonDisabled()
                    }
                }
                Glide.with(context)
                    .load(item.sourceAvatar)
                    .circleCrop()
                    .into(avatarView)

                item.textContent?.let {
                    postItemTextView.text = it
                }

                item.imageUrl?.let {
                    Glide.with(context)
                        .load(it)
                        .into(postImageContainerView)
                }
            }
        }

        // Using payload broke the behaviour of swipe-to-like: card don't return back after swipe
        fun bind(item: NewsItem, becameLiked: Boolean) {
            (itemView as FeedItemLayout).apply {
                if (becameLiked) {
                    this.setLikeButtonEnabled()
                } else {
                    this.setLikeButtonDisabled()
                }
            }
        }
    }

    // Multiple child classes for demonstration.
    inner class TextPostViewHolder(view: FeedItemLayout) : BaseViewHolder(view)
    inner class ImageWithTextPostViewHolder(view: FeedItemLayout) : BaseViewHolder(view)
    inner class ImagePostViewHolder(view: FeedItemLayout) : BaseViewHolder(view)

    override fun onItemDismiss(position: Int) {
        // Couldn't remove item from original list
        callback.onItemHided(newsList[position])
        val newList = newsList.toMutableList()
        newList.removeAt(position)
        differ.submitList(newList)
    }

    override fun onItemApprove(position: Int) {
        // There will be an api call to notify backend about user likes this item
        if (newsList[position].isLiked != true) {
            newsList[position].isLiked = true
            newsList[position].likesCount++
        } else {
            newsList[position].isLiked = false
            newsList[position].likesCount--
        }

        notifyItemChanged(position)
//        Using payload brokes the behaviour of swipe-to-like: card don't return back after swipe
//        notifyItemChanged(position, listOf(newsList[position].isLiked))
    }
}