package com.wavez.trackerwater.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.cheonjaeung.powerwheelpicker.android.WheelPicker
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentOnboading5Binding

class FragmentOnboarding5 : BaseFragment<FragmentOnboading5Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading5Binding
        get() = FragmentOnboading5Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val listKg = (30..100).map { it.toString() }.toTypedArray()
        pickerWheel(binding.wheelPickerWeigh,0 , 70 , listKg)
        val list2  = listOf("Kg","Lb").toTypedArray()
        pickerWheel(binding.wheelPickerWeigh2,0 ,1,list2)
    }

    override fun initListener() {
        super.initListener()
        binding.wheelPickerWeigh2.setOnValueChangedListener { _, _, newVal ->
            val listKg = (30..100).map { it.toString() }.toTypedArray()
            val listLb = (60 .. 230).map { it.toString() }.toTypedArray()
            if(newVal ==1){
                pickerWheel(binding.wheelPickerWeigh,0 , 180 , listLb)
            }else{
                pickerWheel(binding.wheelPickerWeigh,0 , 70,listKg)
            }
        }
    }

    private fun pickerWheel(picker: NumberPicker, min : Int , max : Int, list :Array<String>){
        picker.minValue = min
        picker.maxValue = max
        picker.displayedValues  = list
        picker.value = 0
    }


}