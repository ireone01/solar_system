package com.wavez.trackerwater.extension

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import java.util.Timer
import kotlin.concurrent.schedule


fun View.setAnimation(context: Activity, anim: Int?, duration: Long, onAnimation: (View) -> Unit) {
    this.startAnimation(anim?.let { AnimationUtils.loadAnimation(context, it) })
    Timer().schedule(duration) {
        context.runOnUiThread {
            onAnimation(this@setAnimation)
        }

    }
}

fun ImageView.setTint(colorRes: Int) {
    try {
        ImageViewCompat.setImageTintList(
            this,
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
        )
    } catch (e: Exception) {
        ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorRes))
    }
}

fun ImageView.tintImage(color: Int) {
    setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}


fun View.onAvoidDoubleClick(
    throttleDelay: Long = 600L,
    onClick: (View) -> Unit,
) {
    setOnClickListener {
        onClick(this)
        isClickable = false
        postDelayed({ isClickable = true }, throttleDelay)
    }
}

fun getSpanString(strHasColor: String, strNeedFind: String, color: Int): SpannableString {
    val spanName = SpannableString(strHasColor)
    val fileNameSub = strHasColor.normalText().lowercase()
    val str = strNeedFind.normalText().lowercase()
    if (str != "") {
        try {
            val pattern = str.toRegex()
            val found = pattern.findAll(fileNameSub)
            found.forEach {
                spanName.setSpan(
                    ForegroundColorSpan(color), it.range.first, it.range.last + 1, 0
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
    return spanName
}