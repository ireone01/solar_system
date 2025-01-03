package com.wavez.trackerwater.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading4Binding


class FragmentOnboarding4 : BaseFragment<FragmentOnboading4Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading4Binding
        get() = FragmentOnboading4Binding::inflate


    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val list1 = (1..100).map { it.toString() }.toTypedArray()
        binding.numberPicker.minValue = 0
        binding.numberPicker.maxValue = 99
        binding.numberPicker.displayedValues  = list1
        binding.numberPicker.value = 0
    }
}
