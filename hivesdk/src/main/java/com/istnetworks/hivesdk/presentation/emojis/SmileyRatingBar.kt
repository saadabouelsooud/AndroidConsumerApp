package com.istnetworks.hivesdk.presentation.emojis

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.istnetworks.hivesdk.R


class SmileyRatingBar : LinearLayout, RatingClickListener {

    /**
     * Rating scale
     */
     var ratingScale:Int = 5
    set(value) {
        field = value
        setUpSmileys()
        requestLayout()
    }
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

        when(ratingScale){
            2 -> {
                ratingList.add(getBadRating())
                ratingList.add(getGoodRating())
            }
            3->{
                ratingList.add(getBadRating())
                ratingList.add(getOkayRating())
                ratingList.add(getGreatRating())
            }
            5->{
                ratingList.add(getTerribleRating())
                ratingList.add(getBadRating())
                ratingList.add(getOkayRating())
                ratingList.add(getGoodRating())
                ratingList.add(getGreatRating())
            }
        }

        ratingList.forEach { addView(it) }
    }

    private fun getTerribleRating():RatingEmoji{
        val ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.TERRIBLE, R.drawable.emoji_terrible)
        ratingEmoji.setRatingSelectListener(this)
        return ratingEmoji
    }

    private fun getBadRating():RatingEmoji{
        val  ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.BAD, R.drawable.emoji_bad)
        ratingEmoji.setRatingSelectListener(this)
        return ratingEmoji
    }

    private fun getOkayRating():RatingEmoji{
        val  ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.OKAY, R.drawable.emoji_ok)
        ratingEmoji.setRatingSelectListener(this)
        return ratingEmoji
    }

    private fun getGoodRating():RatingEmoji{
        val  ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.GOOD, R.drawable.emoji_good)
        ratingEmoji.setRatingSelectListener(this)
        return ratingEmoji
    }

    private fun getGreatRating():RatingEmoji{
        val  ratingEmoji = RatingEmoji(context)
        ratingEmoji.setRatingEmoji(Rating.GREAT, R.drawable.emoji_great)
        ratingEmoji.setRatingSelectListener(this)
        return ratingEmoji
    }



    override fun ratingClicked(rating: Rating) {
        if (oldRating != Rating.NONE) {
            ratingList.find { it.rating == oldRating }!!.unSelectRatingEmoji()
        }
        ratingList.find { it.rating == rating }!!.selectRatingEmoji()

        oldRating = rating
        ratingSelectListener?.ratingSelected(rating.getRating() + 1)
    }


}
