package com.istnetworks.hivesdk.data.utils.extensions

import android.content.Context
import android.util.TypedValue




fun Float.toPx(mContext:Context):Int{
    val r = mContext.resources
   return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        r.displayMetrics
    ).toInt()
}