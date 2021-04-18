package com.istnetworks.hivesdk.presentation.emojis

import android.text.TextUtils
import android.view.View
import java.util.*


internal object Utils {

    /**
     * to check if the current layout direction is LTR or RTL
     *
     * @return boolean
     */
    internal fun isRTL(): Boolean = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
}
