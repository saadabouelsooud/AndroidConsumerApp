package com.istnetworks.hivesdk.presentation.emojis

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.istnetworks.hivesdk.R

internal class RatingEmoji : ConstraintLayout {

    private var tvText: TextView? = null
    private var ivEmoji: ImageView? = null
    private var unSelectedEmoji: Int? = null
    private var rating: SmileyRatingBar.Rating = SmileyRatingBar.Rating.NONE
    private var ratingClickListener: RatingClickListener? = null

    private val unselectedImageSize: Int by lazy {
        context.resources.getDimension(R.dimen.unselected_size).toInt()
    }

    private val selectedImageSize: Int by lazy {
        context.resources.getDimension(R.dimen.selected_size).toInt()
    }


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        if (isInEditMode) {
            return
        }
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.rating_emoji_layout, this, true)
        tvText = findViewById(R.id.tv_text)
        ivEmoji = findViewById(R.id.iv_emoji)
        tvText?.setBackgroundResource(Color.TRANSPARENT)
        val params: LayoutParams? = ivEmoji?.layoutParams as LayoutParams?
        params?.width = unselectedImageSize
        params?.height = unselectedImageSize
        ivEmoji?.layoutParams = params
        setOnClickListener { ratingSelected() }
    }

    internal fun setRatingEmoji(rating: SmileyRatingBar.Rating,  unSelectedEmoji: Int) {
        this.rating = rating
        this.unSelectedEmoji = unSelectedEmoji
        ivEmoji?.setImageDrawable(getDrawable(unSelectedEmoji))
    }

    internal fun selectRatingEmoji() {
        tvText?.setBackgroundResource(R.color.black)
        //ivEmoji?.setImageDrawable(getDrawable(selectedEmoji!!))
        val params: LayoutParams? = ivEmoji?.layoutParams as LayoutParams?
        params?.width = selectedImageSize
        params?.height = selectedImageSize
        ivEmoji?.layoutParams = params
    }

    internal fun unSelectRatingEmoji() {
        tvText?.setBackgroundResource(Color.TRANSPARENT)
        ivEmoji?.setImageDrawable(getDrawable(unSelectedEmoji!!))
        val params: LayoutParams? = ivEmoji?.layoutParams as LayoutParams?
        params?.width = unselectedImageSize
        params?.height = unselectedImageSize
        ivEmoji?.layoutParams = params
    }

    internal fun setRatingSelectListener(ratingClickListener: RatingClickListener) {
        this.ratingClickListener = ratingClickListener
    }

    private fun ratingSelected() {
        ratingClickListener?.ratingClicked(rating)
    }

    private fun getDrawable(resourceId: Int): Drawable? = ContextCompat.getDrawable(context, resourceId)


}
