package ru.sviridov.vkclient.ui.presentation.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.feed_item_layout.view.*
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.ui.R
import ru.sviridov.vkclient.ui.getDateTime
import ru.sviridov.vkclient.ui.getItemType
import ru.sviridov.vkclient.ui.presentation.adapter.NewsFeedViewType.*
import ru.sviridov.vkclient.ui.presentation.layout.FeedItemLayout

class PostAdapter(val actionHandler: PostAdapterActionHandler) :
    RecyclerView.Adapter<PostAdapter.BaseViewHolder>(),
    ItemTouchHelperAdapter {

    private val differ = AsyncListDiffer(this, FeedDiffUtilsCallback())
    var postList: List<NewsItem>
        set(value) {
            Log.d(TAG, "oldList has size: ${postList.size}")
            differ.submitList(value, (Runnable {
                Log.d(TAG, "newList has size: ${postList.size}")
                notifyDataSetChanged()
            }))
        }
        get() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = FeedItemLayout(parent.context)
        return when (viewType) {
            VIEW_WITH_TEXT_ONLY.value -> TextPostViewHolder(view)
            VIEW_WITH_SINGLE_PICTURE_ONLY.value -> ImagePostViewHolder(view)
            VIEW_WITH_SINGLE_PICTURE_AND_TEXT.value -> ImageWithTextPostViewHolder(view)
            else -> TextPostViewHolder(view)
        }
    }

    override fun getItemCount(): Int = postList.size

    override fun getItemViewType(position: Int): Int {
        return postList[position].getItemType().value
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(postList[position])
        Log.d(TAG, "onBindViewHolder: isLiked = ${postList[position].isLiked}")
    }

    abstract inner class BaseViewHolder(view: FeedItemLayout) : RecyclerView.ViewHolder(view) {
        // Single method for each view holder
        fun bind(item: NewsItem) {
            (itemView as FeedItemLayout).apply {
                postWriterTitleTextView.apply {
                    text = item.sourceTitle
                    if (item.isRepost) {
                        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_repost)
                        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    }
                }
                postCreatedAgoTextView.text = item.getDateTime()
                socialLikesView.text = item.likesCount.toString()
                socialRepostsView.text = item.shareCount.toString()
                socialCommentsView.text = item.commentCount.toString()
                socialViewsCountView.text = item.viewsCount.toString()
                item.isLiked?.let { isLiked ->
                    if (isLiked) {
                        this.setLikeButtonEnabled()
                    } else {
                        this.setLikeButtonDisabled()
                    }
                } ?: this.setLikeButtonDisabled()

                Glide.with(context)
                    .load(item.sourceAvatar)
                    .circleCrop()
                    .into(avatarView)

                item.textContent?.let {
                    postItemTextView.text = it
                }

                item.imageUrl?.let { url ->
                    Glide.with(context)
                        .load(url)
                        .into(postImageContainerView)

                    postImageContainerView.setOnClickListener {
                        actionHandler.onImageViewClicked(url)
                    }
                }

                socialLikesView.setOnClickListener {
                    actionHandler.onItemLiked(item, item.isLiked != true)
                }

                socialCommentsView.setOnClickListener {
                    actionHandler.onCommentsClicked(item)
                }
            }
        }
    }

    // Multiple child classes for demonstration.
    inner class TextPostViewHolder(view: FeedItemLayout) : BaseViewHolder(view)
    inner class ImageWithTextPostViewHolder(view: FeedItemLayout) : BaseViewHolder(view)
    inner class ImagePostViewHolder(view: FeedItemLayout) : BaseViewHolder(view)

    override fun onItemDismiss(position: Int) {
        actionHandler.onItemHided(postList[position])
    }

    override fun onItemApprove(position: Int) {
        actionHandler.onItemLiked(postList[position], postList[position].isLiked != true)
    }

    companion object {
        private const val TAG = "FeedAdapter"
    }
}
