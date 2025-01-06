package com.wavez.trackerwater.feature.myprofile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wavez.trackerwater.feature.myprofile.FragmentDIalogWeight
import com.wavez.trackerwater.feature.myprofile.FragmentDialogGender

class ViewPagerDialogAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){

    private val listfragment = listOf<Fragment>(
        FragmentDialogGender(),
        FragmentDIalogWeight()
    )

    override fun getItemCount(): Int = listfragment.size

    override fun createFragment(position: Int): Fragment  = listfragment[position]

}