package com.wavez.trackerwater.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wavez.trackerwater.fragment.HistoryFragment
import com.wavez.trackerwater.fragment.InsightsFragment
import com.wavez.trackerwater.fragment.MeFragment
import com.wavez.trackerwater.fragment.TodayFragment

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