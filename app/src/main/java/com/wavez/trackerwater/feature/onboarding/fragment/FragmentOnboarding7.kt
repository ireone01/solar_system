package com.wavez.trackerwater.feature.onboarding.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentOnboading7Binding

class FragmentOnboarding7 : BaseFragment<FragmentOnboading7Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading7Binding
        get() = FragmentOnboading7Binding::inflate


    override fun initListener() {
        super.initListener()

        var btnClicked: LinearLayout? = null

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

    }

    private fun checkClicked(btnOld: LinearLayout? = null, btnNew: LinearLayout) {
        btnNew.isSelected = true
        btnOld?.isSelected = false
    }

}