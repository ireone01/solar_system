package com.wavez.trackerwater.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter (fragmentActivity: FragmentActivity,
                    private val fragmentList: List<Fragment>)
    : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return FragmentWheelPickerPair()
    }
}