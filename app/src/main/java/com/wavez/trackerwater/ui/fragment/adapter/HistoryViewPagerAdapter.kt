package com.wavez.trackerwater.ui.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wavez.trackerwater.ui.home.fragmentHistory.DayFragment
import com.wavez.trackerwater.ui.home.fragmentHistory.MonthFragment
import com.wavez.trackerwater.ui.home.fragmentHistory.WeekFragment

class HistoryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = listOf(
        DayFragment(),
        WeekFragment(),
        MonthFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}