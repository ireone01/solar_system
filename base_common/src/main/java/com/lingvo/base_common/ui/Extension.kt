package com.lingvo.base_common.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager

inline fun <reified T : java.io.Serializable> Intent.getSerializableExtraCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(key) as? T
    }
}

fun Context.readFromAssets(fileName: String): String {
    return assets.open(fileName).bufferedReader().use { it.readText() }
}

fun View.showKeyboard() {
    this.requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}