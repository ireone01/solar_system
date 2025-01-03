package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading8Binding

class FragmentOnboarding8 : BaseFragment<FragmentOnboading8Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading8Binding
        get() = FragmentOnboading8Binding::inflate


    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val listHour = (0..12).map { it.toString() }.toTypedArray()
        val listMin = (0..59).map { it.toString() }.toTypedArray()
        pickerWheel(binding.wheelPickerWeigh,0 , 12 , listHour)
        pickerWheel(binding.wheelPickerWeigh2,0 , 59 , listMin)
        val list2  = listOf("PM","AM").toTypedArray()
        pickerWheel(binding.wheelPickerWeigh3,0 ,1,list2)
    }



    private fun pickerWheel(picker: NumberPicker, min : Int, max : Int, list :Array<String>){
        picker.minValue = min
        picker.maxValue = max
        picker.displayedValues  = list
        picker.value = 0
    }

}