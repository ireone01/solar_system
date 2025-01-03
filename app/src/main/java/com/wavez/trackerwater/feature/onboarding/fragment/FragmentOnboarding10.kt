package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading10Binding

class FragmentOnboarding10 : BaseFragment<FragmentOnboading10Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading10Binding
        get() = FragmentOnboading10Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        initPickers()
    }

    private fun initPickers() {
        binding.npHour.minValue = 1

        binding.npHour.maxValue = 12
        binding.npHour.value = 9

        binding.npMinute.minValue = 0
        binding.npMinute.maxValue = 59
        binding.npMinute.value = 11

        val amPmValues = arrayOf("AM", "PM")
        binding.npAm.minValue = 0
        binding.npAm.maxValue = amPmValues.size - 1
        binding.npAm.displayedValues = amPmValues
        binding.npAm.value = 0
    }

}