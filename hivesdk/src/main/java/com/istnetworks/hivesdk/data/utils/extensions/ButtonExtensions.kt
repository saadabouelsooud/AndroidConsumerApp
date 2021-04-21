package com.istnetworks.hivesdk.data.utils.extensions

import android.widget.Button

fun Button.disable() {
    isEnabled = false
    alpha = .6f
}

fun Button.enable() {
    isEnabled = true
    alpha = 1f
}