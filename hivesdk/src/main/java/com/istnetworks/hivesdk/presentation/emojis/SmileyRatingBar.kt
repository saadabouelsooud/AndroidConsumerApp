package com.istnetworks.hivesdk.presentation.emojis

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.istnetworks.hivesdk.R


class SmileyRatingBar : LinearLayout, RatingClickListener {

    /**
     * Rating Enum
     */
    enum class Rating(private val rating: Int) {
        TERRIBLE(0),
        BAD(1),
        OKAY(2),
        GOOD(3),
        GREAT(4),
        NONE(-1);

        fun getRating(): Int = rating
    }

    private var ratingList: ArrayList<RatingEmoji> = ArrayList()
    private var oldRating: Rating = Rating.NONE

    private var ratingSelectListener: RatingSelectListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    /**
     * set Rating Listener
     * @param ratingSelectListener RatingSelectListener
     */
    fun setRatingSelectListener(ratingSelectListener: RatingSelectListener?) {
        this.ratingSelectListener = ratingSelectListener
    }


    /**
     * get current rating value
     * @return Integer
     */
    fun getRating(): Int = oldRating.getRating() + 1

    private fun init() {
        orientation = LinearLayout.HORIZONTAL
        layoutDirection = if (Utils.isRTL()) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR
        setUpSmileys()
    }

    private fun setUpSmileys() {
        removeAllViews()
        ratingList.clear()
        var ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.TERRIBLE, R.drawable.emoji_terrible)
        ratingEmoji.setRatingSelectListener(this)
        ratingList.add(ratingEmoji)
        ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.BAD, R.drawable.emoji_bad)
        ratingEmoji.setRatingSelectListener(this)
        ratingList.add(ratingEmoji)
        ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.OKAY, R.drawable.emoji_ok)
        ratingEmoji.setRatingSelectListener(this)
        ratingList.add(ratingEmoji)
        ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.GOOD, R.drawable.emoji_good)
        ratingEmoji.setRatingSelectListener(this)
        ratingList.add(ratingEmoji)
        ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.GREAT, R.drawable.emoji_great)
        ratingEmoji.setRatingSelectListener(this)
        ratingList.add(ratingEmoji)
        ratingList.forEach { addView(it) }
    }

    override fun ratingClicked(rating: Rating) {
        if (oldRating != Rating.NONE) {
            ratingList[oldRating.getRating()].unSelectRatingEmoji()
        }
        ratingList[rating.getRating()].selectRatingEmoji()
        oldRating = rating
        ratingSelectListener?.ratingSelected(rating.getRating() + 1)
    }
}
