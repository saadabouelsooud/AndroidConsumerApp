package com.istnetworks.hivesdk.presentation.surveyExtension

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat

fun applyColorToDrawable(context: Context,drawableRes: Int, color: Int) {
    val unwrappedDrawable =
        AppCompatResources.getDrawable(context, drawableRes)
    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
    DrawableCompat.setTint(wrappedDrawable, color)
}