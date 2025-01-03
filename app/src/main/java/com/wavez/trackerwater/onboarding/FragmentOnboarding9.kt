package com.wavez.trackerwater.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentOnboading8Binding
import com.wavez.trackerwater.databinding.FragmentOnboading9Binding

class FragmentOnboarding9 : BaseFragment<FragmentOnboading9Binding>() , OnTimeSelectedListener{
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading9Binding
        get() = FragmentOnboading9Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val pagerAdapter = PagerAdapter(this, this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Breakfast"
                1 -> tab.text = "Lunch"
                else -> tab.text = "Dinner"
            }
        }.attach()
    }
     override fun onTimeSelected(position: Int, timeString: String){
         val tab = binding.tabLayout.getTabAt(position) ?: return

         when (position) {
             0 ->  tab.text = "Breakfast\n$timeString"
             1 -> tab.text = "Lunch\n$timeString"
             2 -> tab.text = "Dinner\n$timeString"
         }
    }

}