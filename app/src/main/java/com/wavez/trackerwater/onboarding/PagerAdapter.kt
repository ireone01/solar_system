package com.wavez.trackerwater.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter (fragment: Fragment
                    ,private val listener: OnTimeSelectedListener) : FragmentStateAdapter(fragment)
{
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment  {
        return FragmentWheelPickerPair.newInstance(position , listener)
    }
}