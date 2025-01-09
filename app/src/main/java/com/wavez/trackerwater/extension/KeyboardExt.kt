package com.wavez.trackerwater.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboardFrom(context: Context, view: View) {
    try {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

fun showKeyboardFrom(context: Context, view: View?) {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}