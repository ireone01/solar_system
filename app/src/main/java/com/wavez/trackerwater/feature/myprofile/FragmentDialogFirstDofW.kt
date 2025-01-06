package com.wavez.trackerwater.feature.myprofile

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.lingvo.base_common.ui.BaseDialogFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeFirstDayOfweekBinding

class FragmentDialogFirstDofW : BaseDialogFragment<FragmentMeFirstDayOfweekBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeFirstDayOfweekBinding {
        return FragmentMeFirstDayOfweekBinding.inflate(inflater,container,false)
    }


    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        val listDay = arrayOf("Sunday","Monday")
        pickerWheel(view.findViewById(R.id.number_picker_firstday_ofweek),0,listDay.size -1,listDay)

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