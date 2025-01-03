package com.wavez.trackerwater.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading10Binding

class FragmentOnboarding10 : BaseFragment<FragmentOnboading10Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading10Binding
        get() = FragmentOnboading10Binding::inflate
    private lateinit var pickerHour: NumberPicker
    private lateinit var pickerMinute: NumberPicker
    private lateinit var pickerAmPm: NumberPicker

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        pickerHour =binding.wheelPickerWeigh
        pickerMinute = binding.wheelPickerWeigh2
        pickerAmPm = binding.wheelPickerWeigh3

        initPickers()
    }
    private fun initPickers() {
        pickerHour.minValue = 1
        pickerHour.maxValue = 12
        pickerHour.value = 9

        pickerMinute.minValue = 0
        pickerMinute.maxValue = 59
        pickerMinute.value = 11

        val amPmValues = arrayOf("AM", "PM")
        pickerAmPm.minValue = 0
        pickerAmPm.maxValue = amPmValues.size - 1
        pickerAmPm.displayedValues = amPmValues
        pickerAmPm.value = 0
    }

}