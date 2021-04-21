package com.istnetworks.hivesdk.data.utils.extensions
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat

fun TextView.setTextWithValue(
    @NonNull originalString: String, strToReplace: String,
    value: String?
) {
    if (value == null) return
    val replaced = originalString.replace(strToReplace, value, true)
    text = replaced
}

fun TextView.replaceHshWithValue(@NonNull originalString: String, value: String?) {
    if (value == null) return
    val replaced = originalString.replace("##", value, true)
    text = replaced
}
fun TextView.setColor(@ColorRes colorId:Int) {
    setTextColor(
        ContextCompat.getColor(
            context!!,
            colorId
        )
    )
}