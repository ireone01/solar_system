package com.wavez.trackerwater.feature.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wavez.trackerwater.feature.fragment.HistoryFragment
import com.wavez.trackerwater.feature.fragment.InsightsFragment
import com.wavez.trackerwater.feature.myprofile.MeFragment
import com.wavez.trackerwater.feature.fragment.TodayFragment

class MainViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        TodayFragment(),
        HistoryFragment(),
        InsightsFragment(),
        MeFragment()
        )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getFragment(position: Int): Fragment? {
        return fragments.getOrNull(position)
    }


}