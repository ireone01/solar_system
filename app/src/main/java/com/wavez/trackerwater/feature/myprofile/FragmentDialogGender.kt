package com.wavez.trackerwater.feature.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeGenderBinding

class FragmentDialogGender : BaseFragment<FragmentMeGenderBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMeGenderBinding
        get() = FragmentMeGenderBinding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val genderList = arrayOf("Male","Female","Others")
        pickerWheel(binding.numberPickerGender,0,genderList.size -1 , genderList)
    }

    private fun pickerWheel(
        picker: NumberPicker,
        min: Int,
        max: Int,
        list: Array<String>
    ) {
        picker.minValue = min
        picker.maxValue = max
        picker.displayedValues = list
        picker.wrapSelectorWheel = false
    }
}