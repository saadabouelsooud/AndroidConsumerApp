package com.istnetworks.hivesdk.data.utils.extensions

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Response
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by khairy on ث, 21/ماي/2019 at 07:00 م.
 * mohamed.khairy@apptcom.com
 */


fun Response<ResponseBody>.getFileName(): String {
    val replaced = headers()["content-disposition"]?.replace("attachment; filename=", "")
    return replaced?.split(";")?.get(0) ?: "eqlha_file.pdf"
}

fun Any.toJson(): String? = Gson().toJson(this)

fun Int?.isZeroOrNull(): Boolean = this == null || this == 0

inline fun ViewGroup.forEach(action: (View) -> Unit) {
    for (i in 0 until childCount)
        action(getChildAt(i))
}

fun Float.roundToTwoDecimals(): Float {
    return BigDecimal(toDouble()).setScale(2, RoundingMode.HALF_UP).toFloat()
}


fun delayExecutionFor80Sec(func: () -> Unit) {
    Handler().postDelayed({
        func()
    }, 80)
}