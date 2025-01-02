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

class FragmentOnboarding9 : BaseFragment<FragmentOnboading9Binding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading9Binding
        get() = FragmentOnboading9Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        pagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab , position ->
            if(position == 0 ){
                tab.text = "Breakfast"
            }else if(position == 1){
                tab.text ="Lunch"
            }else{
                tab.text = "Dinner"
            }

        }.attach()
    }

}