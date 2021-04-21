package com.istnetworks.hivesdk.data.utils.extensions
import android.view.View.*
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.setLayoutAndTextDirections(isRtlLang: Boolean) {
    if (isRtlLang) {
        layoutDirection = LAYOUT_DIRECTION_RTL
        textDirection = TEXT_DIRECTION_ANY_RTL
    } else {
        layoutDirection = LAYOUT_DIRECTION_LTR
        textDirection = TEXT_DIRECTION_LTR
    }
    textAlignment= TEXT_ALIGNMENT_VIEW_START
}

fun TextInputEditText.getStringValue() = text.toString().trim()

fun TextInputEditText.removeWhiteSpaceAndGetStringValue() = text.toString().replace(" ", "").trim()