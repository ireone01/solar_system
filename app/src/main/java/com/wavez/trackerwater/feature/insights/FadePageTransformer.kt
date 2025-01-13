package com.wavez.trackerwater.feature.insights

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class FadePageTransformer :ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.alpha = 1 - abs(position*3)
    }

}