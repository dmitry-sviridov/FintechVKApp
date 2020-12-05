package ru.sviridov.vkclient.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.EditText
import ru.sviridov.component.feeditem.model.NewsItem
import ru.sviridov.vkclient.ui.presentation.adapter.NewsFeedViewType
import java.text.SimpleDateFormat
import java.util.*

fun NewsItem.getItemType(): NewsFeedViewType {
    when {
        this.textContent.isNullOrEmpty() -> {
            if (!this.imageUrl.isNullOrBlank()) return NewsFeedViewType.VIEW_WITH_SINGLE_PICTURE_ONLY
        }
        else -> {
            return if (this.imageUrl.isNullOrEmpty()) {
                NewsFeedViewType.VIEW_WITH_TEXT_ONLY
            } else {
                NewsFeedViewType.VIEW_WITH_SINGLE_PICTURE_AND_TEXT
            }
        }
    }
    return NewsFeedViewType.UNKNOWN
}

@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun NewsItem.getDateTime(): String? {
    val sdf = SimpleDateFormat("HH:mm MM/dd/yy", Locale.getDefault())
    try {
        val netDate = Date(postedAt * 1000)
        return sdf.format(netDate)
    } catch (e: Exception) {
        e.printStackTrace()
        return sdf.format(Date())
    }
}