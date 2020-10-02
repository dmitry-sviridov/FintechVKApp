package ru.sviridov.newsfeed.presentation.layout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.*
import kotlinx.android.synthetic.main.feed_item_layout.view.*
import ru.sviridov.newsfeed.R

class FeedItemLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.feed_item_layout, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)

        var widthConstraints = paddingLeft + paddingRight
        var heightConstraints = paddingTop + paddingBottom

        measureChildWithMargins(
            avatarView,
            widthMeasureSpec,
            widthConstraints,
            heightMeasureSpec,
            heightConstraints
        )

        heightConstraints += (avatarView.measuredHeight + avatarView.marginTop + avatarView.marginBottom)
        widthConstraints += (avatarView.measuredWidth + avatarView.marginLeft + avatarView.marginRight)

        measureChildWithMargins(
            postItemMenu,
            widthMeasureSpec,
            widthConstraints,
            heightMeasureSpec,
            0
        )

        widthConstraints += (postItemMenu.measuredWidth + postItemMenu.marginLeft)

        measureChildWithMargins(
            postWriterTitleTextView,
            widthMeasureSpec,
            widthConstraints,
            heightMeasureSpec,
            0
        )

        measureChildWithMargins(
            postCreatedAgoTextView,
            widthMeasureSpec,
            widthConstraints,
            heightMeasureSpec,
            0
        )

        if (postItemTextView.text.isNotEmpty()) {
            measureChildWithMargins(
                postItemTextView, widthMeasureSpec, 0, heightMeasureSpec, heightConstraints
            )
            heightConstraints += (
                    postItemTextView.measuredHeight +
                            postItemTextView.marginTop +
                            postItemTextView.marginBottom)
        }

        if (postImageContainerView.drawable != null) {
            measureChildWithMargins(
                postImageContainerView, widthMeasureSpec, 0, heightMeasureSpec, heightConstraints
            )
            heightConstraints += (
                    postImageContainerView.measuredHeight +
                            postImageContainerView.marginTop +
                            postImageContainerView.marginBottom)
        }

        measureChildWithMargins(
            socialLikesView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            heightConstraints
        )
        measureChildWithMargins(
            socialCommentsView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            heightConstraints
        )
        measureChildWithMargins(
            socialRepostsView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            heightConstraints
        )
        measureChildWithMargins(
            socialViewsCountView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            heightConstraints
        )

        heightConstraints += (socialLikesView.measuredHeight + socialLikesView.marginTop + socialLikesView.marginBottom)
        setMeasuredDimension(desiredWidth, resolveSize(heightConstraints, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = l + paddingLeft
        var currentTop = t + paddingTop
        val width = r - l - paddingLeft - paddingRight

        avatarView.layout(
            currentLeft + avatarView.marginLeft,
            currentTop + avatarView.marginTop,
            currentLeft + avatarView.measuredWidth + avatarView.marginRight,
            currentTop + avatarView.measuredHeight + avatarView.marginBottom
        )

        currentTop += avatarView.measuredHeight + avatarView.marginTop + avatarView.marginBottom
        currentLeft += avatarView.marginLeft + avatarView.measuredWidth + avatarView.marginRight

        val baseLine =
            (avatarView.measuredHeight + avatarView.marginTop + avatarView.marginBottom) / 2
        val menuBaseLine =
            (baseLine - postWriterTitleTextView.measuredHeight / 2 + postItemMenu.measuredHeight / 2)

        postItemMenu.layout(
            r - paddingRight - postItemMenu.measuredWidth - postItemMenu.marginEnd,
            menuBaseLine - postItemMenu.measuredHeight,
            r - paddingRight + postItemMenu.measuredWidth,
            menuBaseLine
        )

        postWriterTitleTextView.layout(
            currentLeft,
            baseLine - postWriterTitleTextView.measuredHeight,
            currentLeft + postWriterTitleTextView.measuredWidth,
            baseLine
        )

        postCreatedAgoTextView.layout(
            currentLeft,
            baseLine,
            currentLeft + postCreatedAgoTextView.measuredWidth,
            baseLine + postCreatedAgoTextView.measuredHeight
        )

        currentLeft = l + paddingLeft

        if (postItemTextView.text.isNotEmpty()) {
            postItemTextView.layout(
                currentLeft + postItemTextView.marginLeft,
                currentTop,
                currentLeft + postItemTextView.measuredWidth + postItemTextView.marginRight,
                currentTop + postItemTextView.measuredHeight
            )
            currentTop += postItemTextView.measuredHeight + postItemTextView.marginTop + postItemTextView.marginBottom
        }

        if (postImageContainerView.drawable != null) {
            postImageContainerView.layout(
                currentLeft,
                currentTop,
                r,
                currentTop + postImageContainerView.measuredHeight
            )
            currentTop += postImageContainerView.measuredHeight
        }

        val socialBlockWidth = (width - socialLikesView.marginLeft) / 11 * 3
        currentLeft += socialLikesView.marginLeft

        socialLikesView.layout(
            currentLeft,
            currentTop + socialLikesView.marginTop,
            currentLeft + socialLikesView.measuredWidth,
            currentTop + socialLikesView.measuredHeight + socialLikesView.marginTop
        )

        currentLeft += socialBlockWidth
        socialCommentsView.layout(
            currentLeft,
            currentTop + socialCommentsView.marginTop,
            currentLeft + socialCommentsView.measuredWidth,
            currentTop + socialCommentsView.measuredHeight + socialCommentsView.marginTop
        )
        currentLeft += socialBlockWidth
        socialRepostsView.layout(
            currentLeft,
            currentTop + socialRepostsView.marginTop,
            currentLeft + socialRepostsView.measuredWidth,
            currentTop + socialRepostsView.measuredHeight + socialRepostsView.marginTop
        )

        val viewsCountTop =
            (socialLikesView.measuredHeight + socialLikesView.marginTop + socialLikesView.marginBottom) / 2 - socialViewsCountView.measuredHeight / 2

        currentTop += viewsCountTop
        socialViewsCountView.layout(
            r - paddingRight - socialViewsCountView.measuredWidth - socialViewsCountView.marginEnd,
            currentTop,
            r - paddingRight + socialViewsCountView.measuredWidth,
            currentTop + socialViewsCountView.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)

    override fun generateLayoutParams(p: LayoutParams?) = MarginLayoutParams(p)

    override fun checkLayoutParams(p: LayoutParams?) = p is MarginLayoutParams
}