package com.istnetworks.hivesdk.data.utils.extensions
import android.text.Editable
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout


fun EditText.avoidPastingToIt() {
    setTextIsSelectable(true)
    setTextIsSelectable(false)
    isFocusableInTouchMode = false
    isFocusable = false
    setOnLongClickListener { true }
}

fun EditText.removeWhiteSpaceAndGetStringValue() = text.toString().replace(" ", "").trim()
