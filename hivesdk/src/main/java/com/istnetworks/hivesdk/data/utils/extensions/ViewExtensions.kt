package com.istnetworks.hivesdk.data.utils.extensions
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


fun View.disableFor(millis: Long) {
    isEnabled = false
    Handler().postDelayed({
        isEnabled = true
    }, millis)
}

fun View.onClick(func: () -> Unit) = setOnClickListener { func() }

fun <T :View>View.find(id:Int):T{
   return findViewById<T>(id)
}

fun View.onTouchDown(func: () -> Unit) {
    setOnTouchListener { _, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            func()
        }
        return@setOnTouchListener false
    }
}
fun View.makeVisibleWhen(b: Boolean) {
    visibility = if (b)
        View.VISIBLE
    else
        View.GONE
}

fun View.fadeIn(time: Long = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()) {
    apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(1f)
            .setDuration(time)
            .setListener(null)
    }
}

fun View.fadeOut() {
    animate()
        .alpha(0f)
        .setDuration(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.shouldShow(show: Boolean?) {
    visibility = if (show!=null && show==true)
        View.VISIBLE
    else
        View.GONE
}

fun View.shouldShowWithFading(show: Boolean) {
    if (show)
        fadeIn()
    else
        fadeOut()
}

fun View.expand() {
    val matchParentMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight: Int = measuredHeight
    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    show()
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    // Expansion speed of 1dp/ms
    a.duration = 100
    startAnimation(a)
}
fun View.collapse() {
    val initialHeight: Int = measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                hide()
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    // Collapse speed of 1dp/ms
    a.duration = 100
    startAnimation(a)
}