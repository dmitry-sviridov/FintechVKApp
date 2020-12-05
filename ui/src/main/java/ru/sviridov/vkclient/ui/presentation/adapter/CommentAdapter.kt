package ru.sviridov.vkclient.ui.presentation.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.text_comment_item.view.*
import ru.sviridov.component.comment.model.PostCommentItem
import ru.sviridov.vkclient.ui.R

class CommentAdapter(val actionHandler: CommentAdapterActionHandler) :
    RecyclerView.Adapter<CommentAdapter.TextSingleCommentViewHolder>() {

    private val differ = AsyncListDiffer(this, CommentDiffUtilsCallback())
    var commentsList: List<PostCommentItem>
        set(value) {
            Log.d(TAG, "oldList has size: ${commentsList.size}")
            differ.submitList(value, (Runnable {
                Log.d(TAG, "newList has size: ${commentsList.size}")
                notifyDataSetChanged()
            }))
        }
        get() = differ.currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TextSingleCommentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.text_comment_item, parent, false)
    )

    override fun getItemCount(): Int = commentsList.size

    override fun onBindViewHolder(holder: TextSingleCommentViewHolder, position: Int) {
        holder.bind(commentsList[position])
    }

    class TextSingleCommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PostCommentItem) {
            itemView.apply {
                Glide.with(context)
                    .load(item.ownerProfileImageUrl)
                    .circleCrop()
                    .into(itemView.avatarView)

                itemView.commentWriterTitleTextView.text = item.ownerName
                itemView.commentItemTextView.text = item.textContent
                itemView.socialLikesView.text = item.likesCount.toString()

            }
        }
    }

    companion object {
        private const val TAG = "CommentAdapter"
    }
}