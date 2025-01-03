package com.wavez.trackerwater.feature.onboarding.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity
                       ,private val fragmentList: List<Fragment>) : FragmentStateAdapter(fragmentActivity)
    {
        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment  {
            val fragment = fragmentList[position]
            Log.d("ViewPagerAdapter", "createFragment called for position: $position, fragment: $fragment")
            return fragment
        }
    }