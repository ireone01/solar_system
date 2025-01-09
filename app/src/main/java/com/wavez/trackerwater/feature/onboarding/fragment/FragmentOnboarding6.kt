package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentOnboading6Binding

class FragmentOnboarding6 : BaseFragment<FragmentOnboading6Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading6Binding
        get() = FragmentOnboading6Binding::inflate

    override fun initListener() {
        super.initListener()

        var btnClicked : LinearLayout ?= null

        binding.llayoutBtn1.setOnClickListener {
            checkClicked(btnClicked, binding.llayoutBtn1)
            btnClicked = binding.llayoutBtn1
        }
        binding.llayoutBtn2.setOnClickListener {
            checkClicked(btnClicked, binding.llayoutBtn2)
            btnClicked = binding.llayoutBtn2
        }
        binding.llayoutBtn3.setOnClickListener {
            checkClicked(btnClicked, binding.llayoutBtn3)
            btnClicked = binding.llayoutBtn3
        }
        binding.llayoutBtn4.setOnClickListener {
            checkClicked(btnClicked, binding.llayoutBtn4)
            btnClicked = binding.llayoutBtn4
        }
    }

    private fun checkClicked(btnOld: LinearLayout?=null, btnNew: LinearLayout) {
           btnNew.isSelected = true
         btnOld?.isSelected = false
    }

}