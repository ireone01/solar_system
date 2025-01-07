package com.wavez.trackerwater.feature.myprofile

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lingvo.base_common.ui.BaseDialogFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeGenderWeighBinding
import com.wavez.trackerwater.feature.myprofile.adapter.ViewPagerDialogAdapter

class FragmentDIalogGenderWeight : BaseDialogFragment<FragmentMeGenderWeighBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeGenderWeighBinding {
        return FragmentMeGenderWeighBinding.inflate(inflater,container,false)
    }


    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        binding.viewPagerGenderWeigh.adapter = ViewPagerDialogAdapter(requireActivity())
        val tabText= listOf("Gender","Weight")
        TabLayoutMediator(binding.tabLayoutGenderWeigh, binding.viewPagerGenderWeigh){ tab, position ->
            tab.text =tabText[position]
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.BOTTOM)
            window.setBackgroundDrawableResource(android.R.color.transparent)

        }
    }
}