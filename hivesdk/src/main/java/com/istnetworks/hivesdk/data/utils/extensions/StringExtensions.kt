package com.istnetworks.hivesdk.data.utils.extensions
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan

fun String?.isLengthLessThan(minLength: Int): Boolean {
    return if (this == null) true
    else length < minLength
}

infix fun String?.isLessThan(value: Int): Boolean {
    return if (this == null) true
    else {
        val int = this.toInt()
        int < value
    }
}
fun String.convertArabicDecimalToEnglish(): String {
    return replace("١","1")
        .replace("٢","2")
        .replace("٣","3")
        .replace("٤","4")
        .replace("٥","5")
        .replace("٦","6")
        .replace("٧","7")
        .replace("٨","8")
        .replace("٩","9")
        .replace("٠","0")
        .replace("٫",".")
}
fun String?.isEmptyOrNull(): Boolean = this == null || this.isEmpty()

fun String.spanDrawable(replacedChar: Char, drawable:Drawable):SpannableString{
    val str = SpannableString(this)
    val index = str.indexOf(replacedChar)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    str.setSpan(ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM),index,index+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    return str
}