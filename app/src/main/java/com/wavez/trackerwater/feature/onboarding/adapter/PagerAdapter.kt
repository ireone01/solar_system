package com.wavez.trackerwater.feature.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentWheelPickerPair
import com.wavez.trackerwater.feature.onboarding.MealType
import com.wavez.trackerwater.feature.onboarding.OnTimeSelectedListener

class PagerAdapter(
    fragment: Fragment, private val listener: OnTimeSelectedListener
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentWheelPickerPair.newInstance(position, listener, MealType.BREAKFAST)
            1 -> FragmentWheelPickerPair.newInstance(position, listener, MealType.LUNCH)
            2 -> FragmentWheelPickerPair.newInstance(position, listener, MealType.DINNER)
            else -> throw IllegalArgumentException("ERROR")
        }
    }
}